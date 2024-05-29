package com.dicoding.asclepius.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleInterface {


    @GET("top-headlines")
    suspend fun getArticles(
        @Query("q") q: String,
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String
    ): ArticleResponse
}