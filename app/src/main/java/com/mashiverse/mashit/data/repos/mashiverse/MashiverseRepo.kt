package com.mashiverse.mashit.data.repos.mashiverse

import com.mashiverse.mashit.data.models.mashup.MashupResult
import com.mashiverse.mashit.data.models.mashup.generation.GenerateMashupReq
import com.mashiverse.mashit.data.remote.apis.MashiverseApi
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

class MashiverseRepo @Inject constructor(
    private val mashiverseApi: MashiverseApi
) {
    suspend fun getMashup(wallet: String, imgType: Int = 0): MashupResult {
        val downloadType = if (imgType == 0) {
            "png"
        } else {
            "gif"
        }
        val responseBody: ResponseBody = mashiverseApi.getMashup(wallet, downloadType)

        val contentType = responseBody.contentType()?.toString() ?: "image/png"
        val bytes = responseBody.byteStream().use { it.readBytes() }
        return MashupResult(bytes, contentType)
    }

    suspend fun generateMashup(jsonString: String, imgType: Int = 0, mintedName: String? = ""): MashupResult {
        val downloadType = if (imgType == 0) {
            "png"
        } else {
            "gif"
        }

        val body: GenerateMashupReq = Json.decodeFromString(jsonString)

        val responseBody = mashiverseApi.generateMashup(imgType = downloadType, request = body, mintedName = mintedName)

        val contentType = responseBody.contentType()?.toString() ?: "image/png"
        val bytes = responseBody.byteStream().use { it.readBytes() }
        return MashupResult(bytes, contentType)
    }
}