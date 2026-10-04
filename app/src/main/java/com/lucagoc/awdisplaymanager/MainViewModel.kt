package com.lucagoc.awdisplaymanager

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("display_settings", Context.MODE_PRIVATE)

    companion object {
        private const val PREF_AUTO_RENDER = "pref_auto_render_resolution"
        private const val PREF_MANUAL_RENDER = "pref_manual_render_resolution"
        private const val PREF_AUTO_DENSITY = "pref_auto_density"
        private const val PREF_MANUAL_DENSITY = "pref_manual_density"
    }

    private val _supportedResolutions = MutableStateFlow<List<DisplayMode>>(emptyList())
    val supportedResolutions: StateFlow<List<DisplayMode>> = _supportedResolutions

    private val _currentResolution = MutableStateFlow("Loading...")
    val currentResolution: StateFlow<String> = _currentResolution

    private val _formattedCurrentResolution = MutableStateFlow("Loading...")
    val formattedCurrentResolution: StateFlow<String> = _formattedCurrentResolution

    private val _overscan = MutableStateFlow(listOf(100, 100, 100, 100))
    val overscan: StateFlow<List<Int>> = _overscan

    private val _currentDensity = MutableStateFlow(320)
    val currentDensity: StateFlow<Int> = _currentDensity

    private val _isAutoDensity = MutableStateFlow(true)
    val isAutoDensity: StateFlow<Boolean> = _isAutoDensity

    private val _recommendedDensity = MutableStateFlow(320)
    val recommendedDensity: StateFlow<Int> = _recommendedDensity

    private val _currentRenderResolution = MutableStateFlow("1920x1080")
    val currentRenderResolution: StateFlow<String> = _currentRenderResolution

    private val _isAutoRenderResolution = MutableStateFlow(true)
    val isAutoRenderResolution: StateFlow<Boolean> = _isAutoRenderResolution

    private val _countdown = MutableStateFlow<Int?>(null)
    val countdown: StateFlow<Int?> = _countdown

    private val _hasRootAccess = MutableStateFlow<Boolean?>(null)
    val hasRootAccess: StateFlow<Boolean?> = _hasRootAccess

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState

    val appVersionName: String
        get() = try {
            val pInfo = getApplication<Application>().packageManager.getPackageInfo(getApplication<Application>().packageName, 0)
            pInfo.versionName ?: "1.0"
        } catch (_: Exception) {
            "1.0"
        }

    private var previousResolution: DisplayMode? = null
    private var previousRenderResolution: String? = null
    private var previousDensity: Int? = null
    private var countdownJob: Job? = null

    init {
        _isAutoRenderResolution.value = prefs.getBoolean(PREF_AUTO_RENDER, true)
        _isAutoDensity.value = prefs.getBoolean(PREF_AUTO_DENSITY, true)
        fetchInitialData()
        checkForUpdates()
    }

    private fun fetchInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            val hasRoot = RootUtils.checkRootAccess()
            withContext(Dispatchers.Main) {
                _hasRootAccess.value = hasRoot
            }
            if (!hasRoot) return@launch

            val modes = DisplayManager.getSupportedResolutions()
            val savedModeId = prefs.getInt("saved_output_mode_id", -1)
            var currentOutput = DisplayManager.getCurrentResolution()
            if (savedModeId != -1) {
                val currentMode = modes.find { it.name == currentOutput }
                if (currentMode == null || currentMode.id != savedModeId) {
                    DisplayManager.setResolution(savedModeId)
                    DisplayManager.saveOutputResolutionPersist(savedModeId)
                    currentOutput = DisplayManager.getCurrentResolution()
                }
            } else {
                modes.find { it.name == currentOutput }?.let {
                    prefs.edit().putInt("saved_output_mode_id", it.id).putString("saved_output_mode_name", it.name).apply()
                    DisplayManager.saveOutputResolutionPersist(it.id)
                }
            }

            val margins = DisplayManager.getOverscan()

            val autoRender = _isAutoRenderResolution.value
            val autoDensity = _isAutoDensity.value

            var renderRes = DisplayManager.getRenderResolution()
            if (autoRender) {
                val targetAutoRender = DisplayManager.computeAutoRenderResolution(currentOutput)
                if (renderRes != targetAutoRender) {
                    DisplayManager.setRenderResolution(targetAutoRender)
                    renderRes = targetAutoRender
                }
            }

            val recDpi = DisplayManager.computeRecommendedDpi(renderRes)
            var densityVal = DisplayManager.getDensity()
            if (autoDensity) {
                if (densityVal != recDpi) {
                    DisplayManager.setDensity(recDpi)
                    densityVal = recDpi
                }
            }

            withContext(Dispatchers.Main) {
                _supportedResolutions.value = modes
                _currentResolution.value = currentOutput
                _formattedCurrentResolution.value = formatResolutionName(currentOutput)
                _overscan.value = margins
                _currentRenderResolution.value = renderRes
                _recommendedDensity.value = recDpi
                _currentDensity.value = densityVal
            }
        }
    }

    fun applyResolutionTemporarily(mode: DisplayMode) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentName = DisplayManager.getCurrentResolution()
            previousResolution = _supportedResolutions.value.find { it.name == currentName }
                ?: _supportedResolutions.value.firstOrNull()
            previousRenderResolution = _currentRenderResolution.value
            previousDensity = _currentDensity.value

            DisplayManager.setResolution(mode.id)
            val updatedOutput = DisplayManager.getCurrentResolution()

            var newRender = _currentRenderResolution.value
            var newDensity = _currentDensity.value
            var newRecDpi = _recommendedDensity.value

            if (_isAutoRenderResolution.value) {
                newRender = DisplayManager.computeAutoRenderResolution(mode.name)
                DisplayManager.setRenderResolution(newRender)
                newRecDpi = DisplayManager.computeRecommendedDpi(newRender)
                if (_isAutoDensity.value) {
                    newDensity = newRecDpi
                    DisplayManager.setDensity(newDensity)
                }
            }

            withContext(Dispatchers.Main) {
                _currentResolution.value = updatedOutput
                _formattedCurrentResolution.value = formatResolutionName(updatedOutput)
                _currentRenderResolution.value = newRender
                _recommendedDensity.value = newRecDpi
                _currentDensity.value = newDensity
                startCountdown()
            }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (i in 15 downTo 1) {
                _countdown.value = i
                delay(1.seconds)
            }
            rollbackResolution()
        }
    }

    fun confirmResolution() {
        countdownJob?.cancel()
        _countdown.value = null
        previousResolution = null
        previousRenderResolution = null
        previousDensity = null
        viewModelScope.launch(Dispatchers.IO) {
            val current = DisplayManager.getCurrentResolution()
            val mode = _supportedResolutions.value.find { it.name == current }
            if (mode != null) {
                prefs.edit()
                    .putInt("saved_output_mode_id", mode.id)
                    .putString("saved_output_mode_name", mode.name)
                    .apply()
                DisplayManager.saveOutputResolutionPersist(mode.id)
            }
            withContext(Dispatchers.Main) {
                _currentResolution.value = current
                _formattedCurrentResolution.value = formatResolutionName(current)
            }
        }
    }

    fun rollbackResolution() {
        countdownJob?.cancel()
        _countdown.value = null
        val prev = previousResolution
        val prevRender = previousRenderResolution
        val prevDensity = previousDensity

        viewModelScope.launch(Dispatchers.IO) {
            if (prev != null) {
                DisplayManager.setResolution(prev.id)
            }
            if (prevRender != null) {
                DisplayManager.setRenderResolution(prevRender)
            }
            if (prevDensity != null) {
                DisplayManager.setDensity(prevDensity)
            }
            val current = DisplayManager.getCurrentResolution()
            val render = DisplayManager.getRenderResolution()
            val density = DisplayManager.getDensity()
            val recDpi = DisplayManager.computeRecommendedDpi(render)

            withContext(Dispatchers.Main) {
                _currentResolution.value = current
                _formattedCurrentResolution.value = formatResolutionName(current)
                _currentRenderResolution.value = render
                _currentDensity.value = density
                _recommendedDensity.value = recDpi
            }
        }
        previousResolution = null
        previousRenderResolution = null
        previousDensity = null
    }

    fun setRenderResolutionAuto() {
        viewModelScope.launch(Dispatchers.IO) {
            prefs.edit().putBoolean(PREF_AUTO_RENDER, true).apply()
            val autoRender = DisplayManager.computeAutoRenderResolution(_currentResolution.value)
            DisplayManager.setRenderResolution(autoRender)
            val recDpi = DisplayManager.computeRecommendedDpi(autoRender)

            var newDensity = _currentDensity.value
            if (_isAutoDensity.value) {
                newDensity = recDpi
                DisplayManager.setDensity(newDensity)
            }

            withContext(Dispatchers.Main) {
                _isAutoRenderResolution.value = true
                _currentRenderResolution.value = autoRender
                _recommendedDensity.value = recDpi
                _currentDensity.value = newDensity
            }
        }
    }

    fun setRenderResolutionManual(size: String) {
        viewModelScope.launch(Dispatchers.IO) {
            prefs.edit()
                .putBoolean(PREF_AUTO_RENDER, false)
                .putString(PREF_MANUAL_RENDER, size)
                .apply()
            DisplayManager.setRenderResolution(size)
            val recDpi = DisplayManager.computeRecommendedDpi(size)

            var newDensity = _currentDensity.value
            if (_isAutoDensity.value) {
                newDensity = recDpi
                DisplayManager.setDensity(newDensity)
            }

            withContext(Dispatchers.Main) {
                _isAutoRenderResolution.value = false
                _currentRenderResolution.value = size
                _recommendedDensity.value = recDpi
                _currentDensity.value = newDensity
            }
        }
    }

    fun setDensityAuto() {
        viewModelScope.launch(Dispatchers.IO) {
            prefs.edit().putBoolean(PREF_AUTO_DENSITY, true).apply()
            val recDpi = _recommendedDensity.value
            DisplayManager.setDensity(recDpi)

            withContext(Dispatchers.Main) {
                _isAutoDensity.value = true
                _currentDensity.value = recDpi
            }
        }
    }

    fun setDensityManual(density: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            prefs.edit()
                .putBoolean(PREF_AUTO_DENSITY, false)
                .putInt(PREF_MANUAL_DENSITY, density)
                .apply()
            DisplayManager.setDensity(density)

            withContext(Dispatchers.Main) {
                _isAutoDensity.value = false
                _currentDensity.value = density
            }
        }
    }

    private var initialOverscan: List<Int>? = null

    fun startOverscanAdjustment() {
        initialOverscan = _overscan.value
    }

    fun updateOverscan(margin: Int) {
        val coerced = margin.coerceIn(80, 100)
        _overscan.value = listOf(coerced, coerced, coerced, coerced)
        viewModelScope.launch(Dispatchers.IO) {
            DisplayManager.setOverscan(coerced)
        }
    }

    fun cancelOverscan() {
        initialOverscan?.let {
            _overscan.value = it
            viewModelScope.launch(Dispatchers.IO) {
                DisplayManager.setOverscan(it[0])
            }
        }
    }

    fun saveOverscan() {
        initialOverscan = null
        viewModelScope.launch(Dispatchers.IO) {
            val current = _overscan.value
            val margin = current[0]
            prefs.edit().putInt("saved_overscan_margin", margin).apply()
            DisplayManager.setOverscan(margin)
        }
    }

    fun checkForUpdates() {
        viewModelScope.launch {
            _updateState.value = UpdateState.Checking
            val result = UpdateManager.checkForUpdates(appVersionName)
            _updateState.value = result
        }
    }

    fun startUpdate() {
        val currentState = _updateState.value
        if (currentState is UpdateState.UpdateAvailable) {
            viewModelScope.launch {
                _updateState.value = UpdateState.Downloading(0)
                val result = UpdateManager.downloadAndInstall(
                    context = getApplication(),
                    downloadUrl = currentState.downloadUrl,
                    onProgress = { progress ->
                        _updateState.value = UpdateState.Downloading(progress)
                    }
                )
                result.fold(
                    onSuccess = {
                        _updateState.value = UpdateState.Installing
                    },
                    onFailure = { error ->
                        _updateState.value = UpdateState.Error(error.localizedMessage ?: "Download error")
                    }
                )
            }
        }
    }
}

