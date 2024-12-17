package com.maulana.weathermobile.di.module

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.maulana.weathermobile.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(ViewModelComponent::class)
object RestModule {

    @Provides
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().create()
                )
            )
            .build()
    }

    @Provides
    fun provideRestOkHttpClient(application: Application): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .retryOnConnectionFailure(true).addInterceptor(loggingInterceptor).addInterceptor(
                    ChuckerInterceptor.Builder(application)
                        .maxContentLength(250_000L) // Maximum length of content stored in memory (250KB)
                        .redactHeaders(
                            "Authorization",
                            "Bearer"
                        ) // Hide sensitive headers like "Authorization"
                        .alwaysReadResponseBody(true) // Read the response body even for errors
                        .build()
                )
                .build()
        } else {
            OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build()
        }
    }

}