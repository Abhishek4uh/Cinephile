package com.example.dump.moviepaging


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dump.model.MovieResponse
import com.example.dump.model.Movies
import com.example.dump.network.MovieEndpoints
import com.example.dump.util.Constants.API_KEY
import retrofit2.Response


class MoviePagingSource(private val movieEndpoints: MovieEndpoints,whichApiEndPoint:Int): PagingSource<Int, Movies>() {

    private var whichEndPoint: Int= whichApiEndPoint

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        return try {

            val position= params.key ?:1
            val res: Response<MovieResponse>

           if(whichEndPoint==1){

                res= movieEndpoints.getNowPlayingMoviesListPaging(API_KEY,"en-US",position)
            }
            else if(whichEndPoint==2){

                res= movieEndpoints.getUpComingMoviesListPaging(API_KEY,"en-US",position)
            }
            else if(whichEndPoint==3){

                res= movieEndpoints.getTopRatedMoviesListPaging(API_KEY,"en-US",position)
            }
            else{
                res=movieEndpoints.getPopularMoviesListPaging(API_KEY,"en-US",position)
            }
            LoadResult.Page(data = res.body()!!.results,
                prevKey = if (position==1) null else position-1,
                nextKey = if (position==res.body()!!.totalPage) null else position+1)
        }
        catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}