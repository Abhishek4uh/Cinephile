package com.example.dump.network



import com.example.dump.model.TvShowResponse
import com.example.dump.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface TvShowEndPoints{

    @GET("tv/airing_today?api_key=$API_KEY&language=en-US&page=1")
    suspend fun getAiringTodayTvShows(): Response<TvShowResponse>

    @GET("tv/on_the_air?api_key=$API_KEY&language=en-US&page=1")
    suspend fun getOnTheAirTvShows(): Response<TvShowResponse>

    @GET("tv/top_rated?api_key=$API_KEY&language=en-US&page=1")
    suspend fun getTopRatedTvShows(): Response<TvShowResponse>

    @GET("tv/popular?api_key=$API_KEY&language=en-US&page=1")
    suspend fun getPopularTvShows(): Response<TvShowResponse>



    //paging endpoints
    @GET("tv/airing_today")
    suspend fun getAiringTodayTVListPaging(   @Query("api_key") apiKey: String,
                                              @Query("language") language: String,
                                              @Query("page") page: Int):Response<TvShowResponse>

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTVListPaging(      @Query("api_key") apiKey: String,
                                              @Query("language") language: String,
                                              @Query("page") page: Int):Response<TvShowResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedTVListPaging(      @Query("api_key") apiKey: String,
                                              @Query("language") language: String,
                                              @Query("page") page: Int):Response<TvShowResponse>

    @GET("tv/popular")
    suspend fun getPopularTVListPaging(       @Query("api_key") apiKey: String,
                                              @Query("language") language: String,
                                              @Query("page") page: Int):Response<TvShowResponse>

}