package com.mashiverse.mashit.sys.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.mashiverse.mashit.data.repos.mashiverse.MashiverseRepo
import com.mashiverse.mashit.utils.helpers.nft.saveImageToGallery
import com.mashiverse.mashit.utils.helpers.sys.showNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val mashiverseRepo: MashiverseRepo
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            setProgress(workDataOf("STATE" to "LOADING"))

            val wallet = inputData.getString(JSON_STRING)
            Timber.tag("GGG").d(wallet)
            val imgType = inputData.getInt(IMG_TYPE, 0)
            val mintedName = inputData.getString(MINTED_NAME)

            wallet?.let {
                val mashupResult = mashiverseRepo.generateMashup(wallet, imgType, mintedName)
                val timestamp = System.currentTimeMillis()
                val fileName = if (mashupResult.contentType == "image/png") {
                    "mashup_$timestamp.png"
                } else {
                    "mashup_$timestamp.gif"
                }

                saveImageToGallery(
                    context = applicationContext,
                    imageBytes = mashupResult.bytes,
                    fileName = fileName,
                    mimeType = mashupResult.contentType
                )

                showNotification(applicationContext, "Image saved", fileName)
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                showNotification(
                    applicationContext,
                    "Download failed",
                    "Error: ${e.message}"
                )

                Result.failure()
            }
        }
    }

    companion object {
        const val JSON_STRING = "json_string"
        const val MINTED_NAME = "minted_name"
        const val IMG_TYPE = "img_type"
    }
}