package com.example.dump.tvpaging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dump.model.TvShow


class TvPagingViewModel(private val tvPagingRepo: TvPagingRepo):ViewModel() {

    val errorMessage = MutableLiveData<String>()

    fun getTvList(pos:Int): LiveData<PagingData<TvShow>> {

        Log.d("DEBUG",pos.toString())

        return tvPagingRepo.getAllMovies(pos).cachedIn(viewModelScope)
    }

}