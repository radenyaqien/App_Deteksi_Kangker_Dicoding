package com.dicoding.asclepius.data.remote


import com.squareup.moshi.Json

data class ArticleResponse(
    @Json(name = "articles")
    val articles: List<Article>,
    @Json(name = "status")
    val status: String,
    @Json(name = "totalResults")
    val totalResults: Int
)