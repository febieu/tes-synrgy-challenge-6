package com.anangkur.synrgychapter6.data.remote.service

import com.anangkur.synrgychapter6.data.remote.response.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TMDBService {

    /**
     * Find movies using over 30 filters and sort options.
     */
    @GET("discover/movie")
    suspend fun fetchMovies(
        @Query("api_key") apiKey: String = "4b9bfb0e83de2a4afb17c157ccb254f3",
        @Query("with_original_language") withOriginalLanguage: String = "ko",
    ): Response

    @POST("auth/editProfile")
    suspend fun editProfile(
        @Header("Authorization") token: String,
        @Body editProfileBody: EditProfileBody,
    ): EditProfileBody
}

data class EditProfileBody(
    val name: String,
    val image: String,
)