package com.lucagoc.awdisplaymanager

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

object RootUtils {
    fun checkRootAccess(): Boolean {
        return try {
            val process = ProcessBuilder("su", "-c", "echo root_test").start()
            val stdout = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }.trim()
            process.waitFor()
            (process.exitValue() == 0) && (stdout == "root_test")
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Executes a command via su and returns Pair(stdout, stderr).
     */
    fun execute(command: String): Pair<String, String> {
        return try {
            val process = ProcessBuilder("su", "-c", command).start()
            val outputStream = DataOutputStream(process.outputStream)
            
            outputStream.flush()
            outputStream.close()
            
            val stdout = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }.trim()
            val stderr = BufferedReader(InputStreamReader(process.errorStream)).use { it.readText() }.trim()
            
            process.waitFor()
            Pair(stdout, stderr)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair("", e.message ?: "Unknown error")
        }
    }
}
