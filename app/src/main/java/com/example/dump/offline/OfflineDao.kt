package com.example.dump.offline


import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface OfflineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoWatchList(movieOfflineModel: MovieOfflineModel)

    @Query(value = "Select * from watchlist order by id ASC")
    fun getWatchListContent(): LiveData<List<MovieOfflineModel>>

    @Query("DELETE FROM watchlist WHERE contentId = :contentId")
    suspend fun deleteFromWatchList(contentId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE contentId = :contentId LIMIT 1)")
    fun checkContentExists(contentId: Long): Boolean

}
