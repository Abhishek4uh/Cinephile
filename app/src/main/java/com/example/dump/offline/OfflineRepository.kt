package com.example.dump.offline


import androidx.lifecycle.LiveData


class OfflineRepository(private var offlineDao: OfflineDao){

    //inserting..
    suspend fun insert(offlineModel: MovieOfflineModel){
        offlineDao.insertIntoWatchList(offlineModel)
    }

    //fetching..
    fun getAllNote(): LiveData<List<MovieOfflineModel>> {
        return offlineDao.getWatchListContent()
    }

     //deleting..
     suspend fun delete(contentId:Long){
         offlineDao.deleteFromWatchList(contentId)
    }

    //checking..
    fun getAllTheContentId(contentId: Long):Boolean{
        return offlineDao.checkContentExists(contentId)
    }

}