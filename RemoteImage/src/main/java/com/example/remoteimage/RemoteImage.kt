package com.example.remoteimage

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RemoteImage private constructor() {

    class Builder(private val context: Context) {

        @DrawableRes
        private var loadingRes = 0

        @DrawableRes
        private var errorRes = 0

        private var imageUrl = ""
        private var targetWidth = 0
        private var targetHeight = 0

        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        fun addLoading(@DrawableRes loadingRes: Int): Builder {
            this.loadingRes = loadingRes
            return this
        }

        fun addError(@DrawableRes errorRes: Int): Builder {
            this.errorRes = errorRes
            return this
        }

        fun setUrl(imageUrl: String): Builder {
            this.imageUrl = imageUrl
            return this
        }

        fun setSize(width: Int, height: Int): Builder {
            this.targetWidth = width
            this.targetHeight = height
            return this
        }

        fun build(imageView: ImageView) {
            setPlaceHolder(loadingRes, imageView)
            loadImage(imageView)
        }

        private fun setPlaceHolder(@DrawableRes drawableRes: Int, imageView: ImageView) {
            if (drawableRes != 0) {
                imageView.setImageResource(drawableRes)
            }
        }

        private fun loadImage(imageView: ImageView) {
            scope.launch {
                when (val result = DownloadImageWorker(context).execute(imageUrl)) {
                    DownloadImageWorker.Result.Failure -> {
                        withContext(Dispatchers.Main) {
                            setPlaceHolder(errorRes, imageView)
                        }
                    }
                    is DownloadImageWorker.Result.Success -> {
                        loadBitmap(result.path, imageView)
                    }
                }
            }
        }

        private suspend fun loadBitmap(path: String, imageView: ImageView) {
            val bitmap = BitmapUtils.loadScaledImage(path, targetWidth, targetHeight)
            withContext(Dispatchers.Main) {
                imageView.setImageBitmap(bitmap)
            }
        }

    }

    companion object {

        @JvmStatic
        fun clearCache(context: Context) {
            CacheHelper.clearCache(context)
        }
    }

}