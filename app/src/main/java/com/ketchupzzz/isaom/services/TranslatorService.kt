package com.ketchupzzz.isaom.services

import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Query


interface TranslatorService {
    @GET("translate")
     fun translateText(
        @Query("text") text: String,
        @Query("source") source: String,
        @Query("target") target: String
    ): Call<TranslationResponse>
    companion object {
        const val API = "https://walrus-app-sbyty.ondigitalocean.app/translation/"
    }
}
data class TranslationResponse(
    val translation_text: String
)