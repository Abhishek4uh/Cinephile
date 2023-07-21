package com.example.dump.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dump.model.MovieResponse
import com.example.dump.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MovieViewModel(private val repository: MovieRepository): ViewModel() {

    private val _movieLiveData = MutableLiveData<List<MovieResponse>>()
    val movieLiveData :LiveData<List<MovieResponse>>
    get() = _movieLiveData


    val progress =  MutableLiveData<Boolean>()

    init {
        viewModelScope.launch(Dispatchers.IO){

            val data = repository.getAllMoviesData()
            if (data!=null) {
                progress.postValue(false)
                _movieLiveData.postValue(data)
            }
            else{
                progress.postValue(true)
            }
        }
    }
}