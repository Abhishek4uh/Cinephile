package com.example.dump.network


import com.example.dump.model.moviedetailsModel.MoviesDetailsData
import com.example.dump.model.tvdetailsModel.TvDetailsData
import com.example.dump.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface DetailsEndPoint {

    @GET("movie/{id}"+"?api_key=${API_KEY}&language=en-US")
    suspend fun getMovieDetails(@Path("id") id:Long):Response<MoviesDetailsData>

    @GET("tv/{id}"+"?api_key=${API_KEY}&language=en-US")
    suspend fun getTvDetails(@Path("id") id:Long):Response<TvDetailsData>

}