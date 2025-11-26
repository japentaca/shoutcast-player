package com.example.shoutcastplayer.di

import com.example.shoutcastplayer.data.api.RadioBrowserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Radio Browser requires resolving the base URL first, but for simplicity we'll use a known reliable mirror.
    // Ideally, we should query 'all.api.radio-browser.info' to get a server.
    // For this MVP, we will use 'de1.api.radio-browser.info'.
    private const val BASE_URL = "https://de1.api.radio-browser.info/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRadioBrowserApi(retrofit: Retrofit): RadioBrowserApi {
        return retrofit.create(RadioBrowserApi::class.java)
    }
}
