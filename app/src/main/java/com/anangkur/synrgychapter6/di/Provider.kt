package com.anangkur.synrgychapter6.di

import android.content.Context
import com.anangkur.synrgychapter6.data.local.DataStoreManager
import com.anangkur.synrgychapter6.data.local.LocalRepository
import com.anangkur.synrgychapter6.data.remote.RemoteRepository
import com.anangkur.synrgychapter6.data.remote.service.FakeTMDBService
import com.anangkur.synrgychapter6.data.remote.service.TMDBService
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Provider(
    private val context: Context,
) {

    private val chucker = ChuckerInterceptor(context)

    private val httpLogger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(chucker)
//        .addInterceptor(httpLogger)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tmdbService: TMDBService = retrofit.create(TMDBService::class.java)

//    private val tmdbService: TMDBService = FakeTMDBService()

    private val dataStoreManager = DataStoreManager(context)

    val localRepository: LocalRepository = LocalRepository(
        dataStoreManager = dataStoreManager,
    )

    val remoteRepository: RemoteRepository = RemoteRepository(
        tmdbService = tmdbService,
    )
}