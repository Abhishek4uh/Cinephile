package com.example.dump.offline


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// Room-DB schema

@Entity(tableName = "watchlist")
data class MovieOfflineModel(
    @ColumnInfo(name = "contentId") var contentId:Long?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "plot") var plot: String?,
    @ColumnInfo(name = "poster") var poster: String?){
    @PrimaryKey(autoGenerate = true) var id: Int=0
}

