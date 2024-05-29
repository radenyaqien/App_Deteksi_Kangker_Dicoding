package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.AppRepository
import com.dicoding.asclepius.data.local.ResultEntity
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    fun insertData(data: ResultEntity) {
        viewModelScope.launch {
            repository.insertData(data)
        }
    }

}