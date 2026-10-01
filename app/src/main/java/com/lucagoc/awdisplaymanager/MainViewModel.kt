package com.lucagoc.awdisplaymanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val _supportedResolutions = MutableStateFlow<List<DisplayMode>>(emptyList())
    val supportedResolutions: StateFlow<List<DisplayMode>> = _supportedResolutions

    private val _currentResolution = MutableStateFlow("Loading...")
    val currentResolution: StateFlow<String> = _currentResolution

    val formattedCurrentResolution: StateFlow<String> = MutableStateFlow("Loading...")

    private val _overscan = MutableStateFlow(listOf(100, 100, 100, 100))
    val overscan: StateFlow<List<Int>> = _overscan

    private val _currentDensity = MutableStateFlow(320)
    val currentDensity: StateFlow<Int> = _currentDensity

    private val _countdown = MutableStateFlow<Int?>(null)
    val countdown: StateFlow<Int?> = _countdown

    private val _hasRootAccess = MutableStateFlow<Boolean?>(null)
    val hasRootAccess: StateFlow<Boolean?> = _hasRootAccess

    private var previousResolution: DisplayMode? = null
    private var countdownJob: Job? = null

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            val hasRoot = RootUtils.checkRootAccess()
            withContext(Dispatchers.Main) {
                _hasRootAccess.value = hasRoot
            }
            if (!hasRoot) return@launch

            val modes = DisplayManager.getSupportedResolutions()
            val current = DisplayManager.getCurrentResolution()
            val margins = DisplayManager.getOverscan()
            val density = DisplayManager.getDensity()

            withContext(Dispatchers.Main) {
                _supportedResolutions.value = modes
                _currentResolution.value = current
                (formattedCurrentResolution as MutableStateFlow).value = formatResolutionName(current)
                _overscan.value = margins
                _currentDensity.value = density
            }
        }
    }

    fun applyResolutionTemporarily(mode: DisplayMode) {
        viewModelScope.launch(Dispatchers.IO) {
            // Find current mode as fallback
            val currentName = DisplayManager.getCurrentResolution()
            previousResolution = _supportedResolutions.value.find { it.name == currentName }
                ?: _supportedResolutions.value.firstOrNull()

            DisplayManager.setResolution(mode.id, mode.name)
            val updated = DisplayManager.getCurrentResolution()
            
            withContext(Dispatchers.Main) {
                _currentResolution.value = updated
                (formattedCurrentResolution as MutableStateFlow).value = formatResolutionName(updated)
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
            // Timer expired, rollback
            rollbackResolution()
        }
    }

    fun confirmResolution() {
        countdownJob?.cancel()
        _countdown.value = null
        previousResolution = null
        viewModelScope.launch(Dispatchers.IO) {
            val current = DisplayManager.getCurrentResolution()
            withContext(Dispatchers.Main) {
                _currentResolution.value = current
                (formattedCurrentResolution as MutableStateFlow).value = formatResolutionName(current)
            }
        }
    }

    fun rollbackResolution() {
        countdownJob?.cancel()
        _countdown.value = null
        val prev = previousResolution
        if (prev != null) {
            viewModelScope.launch(Dispatchers.IO) {
                DisplayManager.setResolution(prev.id, prev.name)
                val current = DisplayManager.getCurrentResolution()
                withContext(Dispatchers.Main) {
                    _currentResolution.value = current
                    (formattedCurrentResolution as MutableStateFlow).value = formatResolutionName(current)
                }
            }
        }
        previousResolution = null
    }

    private var initialOverscan: List<Int>? = null

    fun startOverscanAdjustment() {
        initialOverscan = _overscan.value
    }

    fun updateOverscan(margin: Int) {
        // Limit between 80 and 100 to prevent the screen from becoming unreadable
        val coerced = margin.coerceIn(80, 100)
        _overscan.value = listOf(coerced, coerced, coerced, coerced)
        
        // Apply in real-time
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
            DisplayManager.setOverscan(current[0])
        }
    }

    fun setDensity(density: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            DisplayManager.setDensity(density)
            val newDensity = DisplayManager.getDensity()
            withContext(Dispatchers.Main) {
                _currentDensity.value = newDensity
            }
        }
    }
}
