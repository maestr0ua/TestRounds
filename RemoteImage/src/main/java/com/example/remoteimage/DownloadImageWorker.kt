package com.example.remoteimage

import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.RestrictTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

@RestrictTo(RestrictTo.Scope.LIBRARY)
class DownloadImageWorker(private val context: Context) {

    suspend fun execute(imageUrl: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                val file = CacheHelper.getFileByUrl(context, imageUrl)

                if (CacheHelper.isFileValid(file)) {
                    return@withContext Result.Success(file.path)
                }

                val request = Request.Builder()
                    .url(imageUrl)
                    .build()

                val response = OkHttpClient()
                    .newCall(request).execute()

                return@withContext if (response.isSuccessful) {
                    val inputStream: InputStream? = response.body?.byteStream()
                    if (inputStream != null) {
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        CacheHelper.cacheBitmap(file, bitmap)
                        Result.Success(file.path)
                    } else {
                        Result.Failure
                    }
                } else {
                    Result.Failure
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext Result.Failure
            }
        }
    }

    sealed class Result {
        data class Success(val path: String) : Result()
        data object Failure : Result()
    }
}