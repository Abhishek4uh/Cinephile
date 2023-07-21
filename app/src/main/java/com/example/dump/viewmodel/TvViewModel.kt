package com.example.dump.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dump.model.TvShowResponse
import com.example.dump.repository.TvShowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TvViewModel(private val repository: TvShowRepository): ViewModel() {

    private val _tvLiveData = MutableLiveData<List<TvShowResponse>>()
    val tvShowLiveData : LiveData<List<TvShowResponse>>
    get() = _tvLiveData

    val progress =  MutableLiveData<Boolean>()

    init {
        viewModelScope.launch(Dispatchers.IO){
            //repository.getNowPlayingMoviesList()
            val data=repository.callingApi()

            if (data!=null) {
                progress.postValue(false)
                _tvLiveData.postValue(data)
            }
            else{
                progress.postValue(true)
            }
        }
    }
}