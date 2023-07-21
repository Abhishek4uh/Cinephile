package com.example.dump.repository



import com.example.dump.model.TvShowResponse
import com.example.dump.network.TvShowEndPoints
import kotlinx.coroutines.*


class TvShowRepository(private val tvShowEndPoints: TvShowEndPoints) {

    private val itemsList = ArrayList<TvShowResponse>()


    suspend fun callingApi(): ArrayList<TvShowResponse> {
        return withContext(Dispatchers.IO){

            itemsList.clear()

            val resOne   =  async{tvShowEndPoints.getAiringTodayTvShows() }
            val resTwo   =  async{ tvShowEndPoints.getOnTheAirTvShows() }
            val resThree =  async{ tvShowEndPoints.getTopRatedTvShows() }
            val resFour  =  async{ tvShowEndPoints.getPopularTvShows() }

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