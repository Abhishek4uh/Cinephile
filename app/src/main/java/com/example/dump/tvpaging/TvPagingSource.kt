package com.example.dump.tvpaging


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dump.model.TvShow
import com.example.dump.model.TvShowResponse
import com.example.dump.network.TvShowEndPoints
import com.example.dump.util.Constants
import retrofit2.Response


class TvPagingSource( private val tvShowEndPoints: TvShowEndPoints,
                      private val whichApiEndPoint:Int ): PagingSource<Int,TvShow>() {

    private var whichEndPoint: Int =whichApiEndPoint

    override fun getRefreshKey(state: PagingState<Int, TvShow>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShow> {
        return try {
            val position= params.key ?:1
            val res: Response<TvShowResponse>


            if(whichEndPoint==1){

                res= tvShowEndPoints.getAiringTodayTVListPaging(Constants.API_KEY,"en-US",position)
            }
            else if(whichEndPoint==2){

                res= tvShowEndPoints.getOnTheAirTVListPaging(Constants.API_KEY,"en-US",position)
            }
            else if(whichEndPoint==3){

                res= tvShowEndPoints.getTopRatedTVListPaging(Constants.API_KEY,"en-US",position)
            }
            else{
                res=tvShowEndPoints.getPopularTVListPaging(Constants.API_KEY,"en-US",position)
            }

            Log.d("DEBUG",res.body()!!.results.toString())
            Log.d("DEBUG",whichEndPoint.toString())

            LoadResult.Page(data = res.body()!!.results, prevKey = if (position==1) null else position-1, nextKey = if (position==res.body()!!.totalPage) null else position+1
            )
        } catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}