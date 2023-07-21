package com.example.dump.network


import com.example.dump.model.MovieResponse
import com.example.dump.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieEndpoints {

    @GET("movie/now_playing?api_key=$API_KEY&language=en-US&page=1")
    suspend fun getNowPlayingMoviesList(): Response<MovieResponse>

    @GET("movie/upcoming?api_key=$API_KEY&language=en-US&page=1")
    suspend fun getUpComingMoviesList():Response<MovieResponse>

    @GET("movie/top_rated?api_key=$API_KEY&language=en-US&page=1")
    suspend fun getTopRatedMoviesList():Response<MovieResponse>

    @GET("movie/popular?api_key=$API_KEY&language=en-US&page=1")
    suspend fun getPopularMoviesList():Response<MovieResponse>


    //paging endpoints
    @GET("movie/now_playing")
    suspend fun getNowPlayingMoviesListPaging(@Query("api_key") apiKey: String,
                                              @Query("language") language: String,
                                              @Query("page") page: Int):Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpComingMoviesListPaging(  @Query("api_key") apiKey: String,
                                              @Query("language") language: String,
                                              @Query("page") page: Int):Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMoviesListPaging(  @Query("api_key") apiKey: String,
                                              @Query("language") language: String,
                                              @Query("page") page: Int):Response<MovieResponse>

    @GET("movie/popular")
    suspend fun getPopularMoviesListPaging(   @Query("api_key") apiKey: String,
                                              @Query("language") language: String,
                                              @Query("page") page: Int):Response<MovieResponse>
}