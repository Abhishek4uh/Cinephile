package com.example.dump.tvpaging


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.dump.model.TvShow
import com.example.dump.network.TvShowEndPoints


class TvPagingRepo(private val tvShowEndPoints : TvShowEndPoints) {


    fun getAllMovies(pos:Int): LiveData<PagingData<TvShow>> {

        Log.d("DEBUG",pos.toString())

        return Pager(config = PagingConfig( pageSize = 20, enablePlaceholders = true, initialLoadSize = 2),
            pagingSourceFactory = { TvPagingSource(tvShowEndPoints,pos) }, initialKey = 1).liveData
    }

}