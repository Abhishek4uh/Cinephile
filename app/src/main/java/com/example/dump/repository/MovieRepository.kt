package com.example.dump.repository


import com.example.dump.model.MovieResponse
import com.example.dump.network.MovieEndpoints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class MovieRepository(private val movieEndpoints: MovieEndpoints){


    private val itemsList = ArrayList<MovieResponse>()


    suspend fun getAllMoviesData(): ArrayList<MovieResponse> {

        return withContext(Dispatchers.IO){

            itemsList.clear()

            val resOne   =  async{movieEndpoints.getNowPlayingMoviesList() }
            val resTwo   =  async{ movieEndpoints.getUpComingMoviesList() }
            val resThree =  async{ movieEndpoints.getTopRatedMoviesList() }
            val resFour  =  async{ movieEndpoints.getPopularMoviesList() }

            val resp1 = resOne.await()
            val resp2 = resTwo.await()
            val resp3 = resThree.await()
            val resp4 = resFour.await()

            if(resp1.isSuccessful && resp1.body() != null) {
                itemsList.add(resp1.body()!!)
            }

            if(resp2.isSuccessful && resp2.body() != null) {
                itemsList.add(resp2.body()!!)
            }

            if(resp3.isSuccessful && resp3.body() != null) {
                itemsList.add(resp3.body()!!)
            }

            if(resp4.isSuccessful && resp4.body() != null) {
                itemsList.add(resp4.body()!!)
            }

            return@withContext itemsList
        }
    }

}

