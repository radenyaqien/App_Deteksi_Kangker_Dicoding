package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.AppRepository
import com.dicoding.asclepius.data.remote.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArticleViewModel(private val repository: AppRepository) : ViewModel() {

    private val _state = MutableStateFlow(ArticleUiState())
    val state get() = _state.asStateFlow()


    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            repository.getArticles().onSuccess {
                onSuccess(it.articles)
            }.onFailure {
                onFailure(it.message)
            }
        }

    }

    fun onFailure(message: String?) {
        _state.update {
            it.copy(
                message = message,
                isLoading = false
            )
        }
    }

    private fun onSuccess(articles: List<Article>) {
        _state.update {
            it.copy(
                data = articles,
                isLoading = false
            )
        }
    }


    data class ArticleUiState(
        val data: List<Article> = emptyList(),
        val message: String? = null,
        val isLoading: Boolean = false
    )
}