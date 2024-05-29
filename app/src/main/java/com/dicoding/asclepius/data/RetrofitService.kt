package com.dicoding.asclepius.data

import com.dicoding.asclepius.data.remote.ArticleInterface
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitService {
    private const val BASE_URL = "https://newsapi.org/v2/"
    const val API_KEY = "d04d306b3a724d199ac170c42f12b523"
    fun getApiService(): ArticleInterface = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient()
                .newBuilder()
                .also { client ->
                    val loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(loggingInterceptor)

                }.build()
        )
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .build().create(ArticleInterface::class.java)
}