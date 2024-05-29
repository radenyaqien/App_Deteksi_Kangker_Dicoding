package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HistoryViewmodel(
    repository: AppRepository
) : ViewModel() {

    val state = repository.getAllData().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(500L),
            emptyList()
        )


}