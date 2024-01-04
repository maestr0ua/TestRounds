package com.example.testrounds.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

private const val IMAGE_URL =
    "https://zipoapps-storage-test.nyc3.digitaloceanspaces.com/image_list.json"

class ImageRepository {

    private var client = OkHttpClient()

    suspend fun fetchImages(): Result<List<ImageEntity>> {
        return withContext(Dispatchers.IO) {
            val request: Request = Request.Builder()
                .url(IMAGE_URL)
                .build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    return@withContext Result.success(parseResponse(response.body!!.string()))
                } else {
                    return@withContext Result.failure(RuntimeException("Code: ${response.code}, message: response.message"))
                }
            }
        }
    }

    private fun parseResponse(response: String): List<ImageEntity> {
        val gson = Gson()
        val myType = object : TypeToken<List<ImageEntity>>() {}.type
        return gson.fromJson(response, myType)
    }


}