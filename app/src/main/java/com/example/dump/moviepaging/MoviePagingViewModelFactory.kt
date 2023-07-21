package com.example.dump.moviepaging


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MoviePagingViewModelFactory constructor(private val moviePagingRepo: MoviePagingRepo): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(MoviePagingViewModel::class.java)) {
            MoviePagingViewModel(this.moviePagingRepo) as T
        }
        else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}