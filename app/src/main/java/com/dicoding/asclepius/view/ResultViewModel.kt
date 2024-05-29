package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.AppRepository
import com.dicoding.asclepius.data.local.ResultEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(private val repository: AppRepository) : ViewModel() {

    private val _state = MutableStateFlow<ResultEntity?>(null)
    val state = _state.asStateFlow()

    fun getOneData(id: Int) {
        viewModelScope.launch {
            repository.getOneData(id).collect { newResult ->
                _state.update {
                    newResult
                }
            }
        }
    }
}