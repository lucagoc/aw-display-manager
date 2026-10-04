package com.lucagoc.awdisplaymanager

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

sealed class UpdateState {
    object Idle : UpdateState()
    object Checking : UpdateState()
    data class UpToDate(val currentVersion: String) : UpdateState()
    data class UpdateAvailable(
        val latestVersion: String,
        val releaseNotes: String,
        val downloadUrl: String
    ) : UpdateState()
    data class Downloading(val progress: Int) : UpdateState()
    object Installing : UpdateState()
    data class Error(val message: String) : UpdateState()
}

object UpdateManager {
    private const val DEFAULT_REPO = "lucagoc/aw-display-manager"

    suspend fun checkForUpdates(
        currentVersion: String,
        repository: String = DEFAULT_REPO
    ): UpdateState = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.github.com/repos/$repository/releases/latest")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/vnd.github.v3+json")
                setRequestProperty("User-Agent", "AWDisplayManager-App")
                connectTimeout = 10000
                readTimeout = 10000
            }

            val responseCode = connection.responseCode
            if (responseCode != 200) {
                return@withContext UpdateState.Error("HTTP $responseCode")
            }

            val jsonStr = connection.inputStream.bufferedReader().use { it.readText() }
            val jsonObj = JSONObject(jsonStr)
            val tagName = jsonObj.optString("tag_name", "")
            val body = jsonObj.optString("body", "")
            val assets = jsonObj.optJSONArray("assets")

            var downloadUrl: String? = null
            if (assets != null) {
                for (i in 0 until assets.length()) {
                    val asset = assets.getJSONObject(i)
                    val name = asset.optString("name", "")
                    if (name.endsWith(".apk", ignoreCase = true)) {
                        downloadUrl = asset.optString("browser_download_url")
                        break
                    }
                }
            }

            if (isNewerVersion(currentVersion, tagName) && !downloadUrl.isNullOrEmpty()) {
                UpdateState.UpdateAvailable(
                    latestVersion = tagName,
                    releaseNotes = body,
                    downloadUrl = downloadUrl
                )
            } else {
                UpdateState.UpToDate(currentVersion)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            UpdateState.Error(e.localizedMessage ?: "Connection error")
        }
    }

    suspend fun downloadAndInstall(
        context: Context,
        downloadUrl: String,
        onProgress: (Int) -> Unit
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val updateDir = File(context.cacheDir, "updates").apply { mkdirs() }
            val apkFile = File(updateDir, "app-update.apk")
            if (apkFile.exists()) {
                apkFile.delete()
            }

            var currentUrl = downloadUrl
            var connection: HttpURLConnection
            var redirects = 0
            val maxRedirects = 5

            while (true) {
                val url = URL(currentUrl)
                connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    setRequestProperty("User-Agent", "AWDisplayManager-App")
                    connectTimeout = 15000
                    readTimeout = 15000
                    instanceFollowRedirects = true
                }

                val status = connection.responseCode
                if (status == HttpURLConnection.HTTP_MOVED_TEMP ||
                    status == HttpURLConnection.HTTP_MOVED_PERM ||
                    status == HttpURLConnection.HTTP_SEE_OTHER
                ) {
                    if (redirects++ > maxRedirects) {
                        return@withContext Result.failure(Exception("Too many redirects"))
                    }
                    currentUrl = connection.getHeaderField("Location")
                    connection.disconnect()
                    continue
                }
                break
            }

            val fileLength = connection.contentLength
            connection.inputStream.use { input ->
                FileOutputStream(apkFile).use { output ->
                    val buffer = ByteArray(8192)
                    var downloaded = 0L
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        downloaded += bytesRead
                        if (fileLength > 0) {
                            val progress = ((downloaded * 100) / fileLength).toInt()
                            withContext(Dispatchers.Main) {
                                onProgress(progress)
                            }
                        }
                    }
                }
            }

            installApk(context, apkFile)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private suspend fun installApk(context: Context, apkFile: File) {
        if (RootUtils.checkRootAccess()) {
            val (stdout, _) = RootUtils.execute("pm install -r \"${apkFile.absolutePath}\"")
            if (stdout.contains("Success", ignoreCase = true)) {
                return
            }
        }

        withContext(Dispatchers.Main) {
            val apkUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    fun isNewerVersion(current: String, latest: String): Boolean {
        val cleanCurrent = current.removePrefix("v").removePrefix("V").trim()
        val cleanLatest = latest.removePrefix("v").removePrefix("V").trim()

        val currentParts = cleanCurrent.split(".", "-", "_")
            .mapNotNull { part -> part.takeWhile { it.isDigit() }.toIntOrNull() }
        val latestParts = cleanLatest.split(".", "-", "_")
            .mapNotNull { part -> part.takeWhile { it.isDigit() }.toIntOrNull() }

        val maxLength = maxOf(currentParts.size, latestParts.size)
        for (i in 0 until maxLength) {
            val c = currentParts.getOrElse(i) { 0 }
            val l = latestParts.getOrElse(i) { 0 }
            if (l > c) return true
            if (l < c) return false
        }
        return false
    }
}
