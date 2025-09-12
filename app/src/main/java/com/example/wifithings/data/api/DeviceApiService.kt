package com.example.wifithings.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceApiService @Inject constructor(
    private val client: OkHttpClient
) {
    suspend fun sendRequest(url: String): Result<String> {
        println("🔄 Attempting to connect to: $url")

        return try {
            val request = Request.Builder()
                .url(url)
                .build()

            val result = withContext(Dispatchers.IO) {
                client.newCall(request).execute().use { response ->
                    println("📡 Response code: ${response.code}")

                    if (response.isSuccessful) {
                        val body = response.body?.string() ?: ""
                        println("✅ Success! Response: $body")
                        Result.success(body)
                    } else {
                        println("❌ HTTP error: ${response.code}")
                        Result.failure(Exception("HTTP error: ${response.code}"))
                    }
                }
            }

            result
        } catch (e: Exception) {
            e.printStackTrace()
            println("💥 Exception: ${e.javaClass.name} ${e.message}")
            Result.failure(e)
        }
    }
}