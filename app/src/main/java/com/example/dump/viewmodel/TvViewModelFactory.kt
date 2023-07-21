package com.example.dump.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dump.repository.TvShowRepository

class TvViewModelFactory(private val repository: TvShowRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TvViewModel(repository) as T
    }
}