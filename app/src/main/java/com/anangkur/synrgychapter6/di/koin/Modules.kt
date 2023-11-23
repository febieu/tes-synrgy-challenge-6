package com.anangkur.synrgychapter6.di.koin

import androidx.work.WorkManager
import com.anangkur.synrgychapter6.data.local.DataStoreManager
import com.anangkur.synrgychapter6.data.local.LocalRepository
import com.anangkur.synrgychapter6.data.remote.RemoteRepository
import com.anangkur.synrgychapter6.data.remote.service.TMDBService
import com.anangkur.synrgychapter6.domain.repository.AccountRepository
import com.anangkur.synrgychapter6.domain.repository.AuthRepository
import com.anangkur.synrgychapter6.domain.repository.ImageRepository
import com.anangkur.synrgychapter6.domain.repository.MovieRepository
import com.anangkur.synrgychapter6.presentation.auth.login.usecase.AuthenticateUseCase
import com.anangkur.synrgychapter6.presentation.auth.login.usecase.CheckLoginUseCase
import com.anangkur.synrgychapter6.presentation.auth.login.LoginViewModel
import com.anangkur.synrgychapter6.presentation.auth.login.usecase.SaveTokenUseCase
import com.anangkur.synrgychapter6.presentation.auth.register.RegisterUseCase
import com.anangkur.synrgychapter6.presentation.auth.register.RegisterViewModel
import com.anangkur.synrgychapter6.presentation.blur.BlurViewModel
import com.anangkur.synrgychapter6.presentation.home.HomeViewModel
import com.anangkur.synrgychapter6.presentation.profile.ProfileViewModel
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//ambil dr provider
private val generalModule = module {
    single { ChuckerInterceptor(get()) }
    single { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }
    single { provideOkhttpClient(get(), get()) }
    single { "https://api.themoviedb.org/3/" }
    single { GsonConverterFactory.create() }
    single { provideRetrofit(get(), get(), get()) }
    single { provideTMDBService(get()) }
    single { DataStoreManager(get()) }
    //nambah work manager????
    single { WorkManager.getInstance(get()) }

//    single<LoginRepository> { LocalRepository(get()) }
//    single<HomeRepository> { RemoteRepository(get()) }
//    single<ProfileRepository> { LocalRepository(get()) }
//    single<RegisterRepository> { LocalRepository(get()) }

    //repository
    single<AccountRepository> { LocalRepository(get(),get()) }
    single<AuthRepository> {LocalRepository(get(),get())}
    single<ImageRepository> {LocalRepository(get(),get())}
    //data
    single<MovieRepository> {RemoteRepository(get())}
    //00:43:40

    //usecase
    single<AuthenticateUseCase>{ AuthenticateUseCase(get()) }
    single<CheckLoginUseCase>{ CheckLoginUseCase((get())) }
    single<SaveTokenUseCase>{ SaveTokenUseCase(get()) }
    single<RegisterUseCase>{ RegisterUseCase(get()) }
}

private val viewModelModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { BlurViewModel(get(), get()) }
}

val appModules = listOf(generalModule, viewModelModule)

private fun provideOkhttpClient(
    chuckerInterceptor: ChuckerInterceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor,
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(chuckerInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    baseUrl: String,
    gsonConverterFactory: GsonConverterFactory,
): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(gsonConverterFactory)
        .build()
}

private fun provideTMDBService(
    retrofit: Retrofit,
): TMDBService {
    return retrofit.create(TMDBService::class.java)
}