package com.mashiverse.mashit.data.remote.apis

import android.R
import com.mashiverse.mashit.data.models.mashup.generation.GenerateMashupReq
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MashiverseApi {
    @GET("/api/mashi/mashup/{wallet}")
    suspend fun getMashup(
        @Path("wallet") wallet: String,
        @Query("download_type") imgType: String = "png",
    ): ResponseBody

    @POST("/api/mashi/app_mashup")
    suspend fun generateMashup(
        @Query("download_type") imgType: String = "png",
        @Body request: GenerateMashupReq
    ): ResponseBody
}