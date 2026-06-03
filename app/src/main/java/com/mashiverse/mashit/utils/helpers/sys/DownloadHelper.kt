package com.mashiverse.mashit.utils.helpers.sys

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.mashiverse.mashit.sys.workers.UploadWorker
import java.util.concurrent.TimeUnit

fun startImageDownload(jsonString: String, imageType: Int, worker: WorkManager) {
    val inputData = Data.Builder()
        .putString(UploadWorker.JSON_STRING, jsonString)
        .putInt(UploadWorker.IMG_TYPE, imageType)
        .build()

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val uploadRequest = OneTimeWorkRequestBuilder<UploadWorker>()
        .setInputData(inputData)
        .setConstraints(constraints)
        .setBackoffCriteria(
            BackoffPolicy.LINEAR,
            WorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS
        )
        .build()

    worker.enqueueUniqueWork(
        "image_download_work",
        ExistingWorkPolicy.REPLACE,
        uploadRequest
    )
}