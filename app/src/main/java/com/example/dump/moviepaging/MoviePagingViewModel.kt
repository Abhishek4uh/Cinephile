package com.example.dump.moviepaging


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dump.model.Movies


class MoviePagingViewModel constructor(private val moviePagingRepo: MoviePagingRepo):ViewModel() {

    val errorMessage = MutableLiveData<String>()

    fun getMovieList(pos:Int): LiveData<PagingData<Movies>> {

        return moviePagingRepo.getAllMovies(pos).cachedIn(viewModelScope)
    }
}