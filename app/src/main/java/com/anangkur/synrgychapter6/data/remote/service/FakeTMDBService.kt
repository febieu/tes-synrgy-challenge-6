package com.anangkur.synrgychapter6.data.remote.service

import com.anangkur.synrgychapter6.data.remote.response.Response
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException

class FakeTMDBService : TMDBService {
    override suspend fun fetchMovies(apiKey: String, withOriginalLanguage: String): Response {
        return Response(page = 1, results = emptyList(), totalPages = 1, totalResults = 0)
    }

    override suspend fun editProfile(
        token: String,
        editProfileBody: EditProfileBody
    ): EditProfileBody {
        delay(1000)
        if (token.isEmpty()) {
            throw HttpException(
                retrofit2.Response.error<EditProfileBody>(
                    403,
                    ResponseBody.Companion.create("application/json".toMediaTypeOrNull(), "{\"error\": \"unauthentication\""),
                )
            )
        } else {
            return editProfileBody
        }
    }
}