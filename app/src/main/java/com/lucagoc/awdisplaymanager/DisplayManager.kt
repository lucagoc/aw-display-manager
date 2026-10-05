package com.lucagoc.awdisplaymanager

data class DisplayMode(val id: Int, val name: String) {
    val displayName: String
        get() = formatResolutionName(name)
}

private val pRegex = Regex("""P(?=\s|$)""")
private val iRegex = Regex("""I(?=\s|$)""")

fun formatResolutionName(rawName: String): String {
    if ((rawName == "Unknown") || rawName.startsWith("Mode ID:")) return rawName
    
    // Example: DISP_TV_MOD_1080P_60HZ -> 1080p 60Hz
    // Example: DISP_TV_MOD_3840_2160P_60HZ -> 4K 60Hz
    return rawName
        .replace("DISP_TV_MOD_3840_2160P", "4K")
        .replace("DISP_TV_MOD_", "")
        .replace("_", " ")
        .replace(pRegex, "p")
        .replace(iRegex, "i")
        .replace("HZ", "Hz")
        .trim()
}

object DisplayManager {
    private val modeRegex = Regex("""\[(\d+)]\s+([a-zA-Z0-9_]+)""")
    private val idRegex = Regex("""\d+""")
    private val currentModeRegex = Regex("""type=4\s+mode=(\d+)""")
    private val overrideDensityRegex = Regex("""Override density:\s*(\d+)""")
    private val physDensityRegex = Regex("""Physical density:\s*(\d+)""")
    private val overrideSizeRegex = Regex("""Override size:\s*(\d+x\d+)""")
    private val physSizeRegex = Regex("""Physical size:\s*(\d+x\d+)""")
    private val marginRegex = Regex("""Margin:\s*left=(\d+)\s*right=(\d+)\s*top=(\d+)\s*bottom=(\d+)""")

    private const val VENDOR_DISPCONFIG = "LD_LIBRARY_PATH=/vendor/lib /vendor/bin/dispconfig"
    private var internalMarginBinaryPath: String? = null

    fun init(context: android.content.Context) {
        try {
            val file = java.io.File(context.filesDir, "dispconfig.margin")
            if (!file.exists() || file.length() == 0L) {
                context.assets.open("dispconfig.margin").use { input ->
                    java.io.FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
            }
            file.setExecutable(true, false)
            internalMarginBinaryPath = file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getMarginCommand(margin: Int): String {
        val binPath = internalMarginBinaryPath
        return if (binPath != null && java.io.File(binPath).exists()) {
            "LD_LIBRARY_PATH=/vendor/lib $binPath -m $margin"
        } else {
            "LD_LIBRARY_PATH=/vendor/lib dispconfig -m $margin"
        }
    }

    fun getSupportedResolutions(): List<DisplayMode> {
        val (stdout, _) = RootUtils.execute("$VENDOR_DISPCONFIG -p")
        // Expected format could be something like "  - [10] DISP_TV_MOD_1080P_60HZ"
        val modes = mutableListOf<DisplayMode>()
        stdout.lines().forEach { line ->
            val match = modeRegex.find(line)
            if (match != null) {
                modes.add(DisplayMode(match.groupValues[1].toInt(), match.groupValues[2]))
            } else if (line.contains("1080P") || line.contains("720P") || line.contains("4K")) {
                // simple fallback parse if it just spits out strings and ids somewhere
                idRegex.find(line)?.let { idMatch ->
                    modes.add(DisplayMode(idMatch.value.toInt(), line.trim()))
                }
            }
        }
        
        // Hardcoded fallbacks if nothing parses
        if (modes.isEmpty()) {
            modes.add(DisplayMode(10, "DISP_TV_MOD_1080P_60HZ"))
            modes.add(DisplayMode(4, "DISP_TV_MOD_720P_60HZ"))
            modes.add(DisplayMode(38, "DISP_TV_MOD_3840_2160P_30HZ"))
            modes.add(DisplayMode(39, "DISP_TV_MOD_3840_2160P_60HZ"))
        }
        return modes
    }

    fun getCurrentResolution(): String {
        val (stdout, _) = RootUtils.execute("$VENDOR_DISPCONFIG -d")
        val match = currentModeRegex.find(stdout)
        if (match != null) {
            val modeId = match.groupValues[1].toIntOrNull()
            if (modeId != null) {
                // Try to find the name from supported resolutions
                val modes = getSupportedResolutions()
                return modes.find { it.id == modeId }?.name ?: "Mode ID: $modeId"
            }
        }
        return "Unknown"
    }

    fun setResolution(modeId: Int) {
        // Set HDMI output mode
        RootUtils.execute("$VENDOR_DISPCONFIG -s $modeId")
    }

    fun saveOutputResolutionPersist(modeId: Int) {
        val propValue = "4,$modeId,0,0,4,257"
        RootUtils.execute("setprop persist.disp.device_config.hdmi \"$propValue\"")
        RootUtils.execute("setprop persist.vendor.disp.mode $modeId")
    }

    fun getRenderResolution(): String {
        val (stdout, _) = RootUtils.execute("wm size")
        overrideSizeRegex.find(stdout)?.groupValues?.getOrNull(1)?.let { return it }
        physSizeRegex.find(stdout)?.groupValues?.getOrNull(1)?.let { return it }
        return "1920x1080"
    }

    fun setRenderResolution(size: String) {
        RootUtils.execute("wm size $size")
    }

    fun computeAutoRenderResolution(outputResolutionName: String): String {
        val upper = outputResolutionName.uppercase()
        return when {
            upper.contains("720") -> "1280x720"
            // Cap at 1080p even if 4K / 3840x2160 is chosen for output to avoid performance issues
            upper.contains("3840") || upper.contains("4K") || upper.contains("2160") -> "1920x1080"
            upper.contains("1080") -> "1920x1080"
            upper.contains("480") -> "720x480"
            upper.contains("576") -> "720x576"
            else -> "1920x1080"
        }
    }

    fun computeRecommendedDpi(renderResolution: String): Int {
        val parts = renderResolution.lowercase().split("x")
        val height = parts.getOrNull(1)?.toIntOrNull() ?: 1080
        return when {
            height <= 480 -> 160
            height <= 720 -> 213
            height <= 1080 -> 320
            else -> 640
        }
    }

    fun getDensity(): Int {
        val (stdout, _) = RootUtils.execute("wm density")
        overrideDensityRegex.find(stdout)?.groupValues?.getOrNull(1)?.toIntOrNull()?.let { return it }
        physDensityRegex.find(stdout)?.groupValues?.getOrNull(1)?.toIntOrNull()?.let { return it }
        return 320
    }

    fun setDensity(density: Int) {
        RootUtils.execute("wm density $density")
    }

    fun getOverscan(): List<Int> {
        val (stdout, _) = RootUtils.execute("getprop persist.disp.margin.hdmi")
        val fallback = listOf(100, 100, 100, 100)
        if (stdout.isNotBlank()) {
            val parts = stdout.split(",")
            if (parts.size == 4) {
                return parts.mapNotNull { it.trim().toIntOrNull() }.ifEmpty { fallback }
            }
        }
        
        // Try parsing from dispconfig -d if prop is empty
        val (dispStdout, _) = RootUtils.execute("$VENDOR_DISPCONFIG -d")
        val match = marginRegex.find(dispStdout)
        if (match != null) {
            val left = match.groupValues[1].toIntOrNull() ?: 100
            val right = match.groupValues[2].toIntOrNull() ?: 100
            val top = match.groupValues[3].toIntOrNull() ?: 100
            val bottom = match.groupValues[4].toIntOrNull() ?: 100
            return listOf(left, top, right, bottom)
        }

        return fallback
    }

    fun setOverscan(margin: Int) {
        val marginStr = "$margin,$margin,$margin,$margin"
        RootUtils.execute("setprop persist.disp.margin.hdmi \"$marginStr\"")
        RootUtils.execute(getMarginCommand(margin))
    }

    private val dataspaceRegex = Regex("""dataspace:\s*([A-Za-z]+)""")

    fun getCurrentDataspace(): String {
        val (stdout, _) = RootUtils.execute("$VENDOR_DISPCONFIG -d")
        val match = dataspaceRegex.find(stdout)
        return match?.groupValues?.getOrNull(1)?.uppercase() ?: "AUTO"
    }

    fun setDataspace(mode: String): Boolean {
        val validMode = mode.lowercase().trim()
        val (stdout, _) = RootUtils.execute("$VENDOR_DISPCONFIG -c $validMode")
        return stdout.contains("success", ignoreCase = true)
    }
}
