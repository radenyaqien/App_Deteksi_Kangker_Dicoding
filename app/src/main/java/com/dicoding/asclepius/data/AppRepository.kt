package com.dicoding.asclepius.data

import com.dicoding.asclepius.data.local.AppDao
import com.dicoding.asclepius.data.local.ResultEntity
import com.dicoding.asclepius.data.remote.ArticleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AppRepository(
    private val dao: AppDao
) {

    suspend fun insertData(data: ResultEntity) {
        dao.insert(data)
    }

    fun getAllData(): Flow<List<ResultEntity>> {
        return dao.getAllData()
    }

    fun getOneData(id: Int): Flow<ResultEntity> {
        return dao.getOneData(id)
    }

    suspend fun getArticles() : Result<ArticleResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val data = RetrofitService.getApiService()
                    .getArticles(
                        "cancer",
                        "health",
                        "en",
                        RetrofitService.API_KEY
                    )
                Result.success(data)

            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {

                        Result.failure(throwable)
                    }
                    else -> {
                        Result.failure(throwable)
                    }
                }
            }
        }


    }

    companion object {
        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(
            dao: AppDao,
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(dao)
            }.also { instance = it }
    }

}