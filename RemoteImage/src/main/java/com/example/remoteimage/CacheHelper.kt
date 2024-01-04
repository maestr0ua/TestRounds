package com.example.remoteimage

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.RestrictTo
import java.io.File
import java.io.FileOutputStream

@RestrictTo(RestrictTo.Scope.LIBRARY)
object CacheHelper {

    fun clearCache(context: Context) {
        for (child in context.cacheDir.listFiles()!!) {
            child.delete()
        }
    }

    fun cacheBitmap(file: File, bitmap: Bitmap) {
        try {
            if (file.exists()) {
                file.delete()
            }

            file.createNewFile()

            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getFileByUrl(context: Context, imageUrl: String): File {
        val cacheDir: File = context.cacheDir
        val fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1)
        return File(cacheDir, fileName)
    }

    fun isFileValid(file: File): Boolean {
        val validTime = 1000 * 60 * 60 * 4
        return file.exists() && file.lastModified() + validTime > System.currentTimeMillis()
    }
}