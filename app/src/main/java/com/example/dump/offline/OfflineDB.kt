package com.example.dump.offline


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [MovieOfflineModel::class], version = 5, exportSchema = false)
abstract class OfflineDB: RoomDatabase() {

    abstract fun getItemDao(): OfflineDao

    companion object{
        @Volatile
        private var INSTANCE: OfflineDB? = null
        fun getDatabase(context: Context): OfflineDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, OfflineDB::class.java, "watchlist_db").build()
                    INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}