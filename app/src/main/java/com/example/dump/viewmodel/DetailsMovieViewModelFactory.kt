package com.example.dump.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dump.network.DetailsEndPoint


class DetailsMovieViewModelFactory (private val detailsEndPoint : DetailsEndPoint): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsMovieViewModel(detailsEndPoint) as T
    }
}