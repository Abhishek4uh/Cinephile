package com.example.dump.moviepaging


import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.dump.model.Movies
import com.example.dump.network.MovieEndpoints


class MoviePagingRepo constructor(private val movieEndpoints: MovieEndpoints){

    fun getAllMovies(pos:Int): LiveData<PagingData<Movies>> {
        return Pager( config = PagingConfig( pageSize = 20, maxSize = 100, enablePlaceholders = true),
                      pagingSourceFactory = { MoviePagingSource(movieEndpoints,pos)}, initialKey = 1).liveData
    }
}