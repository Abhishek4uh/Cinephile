package com.example.dump.offline


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OfflineViewModel(application: Application) : AndroidViewModel(application){

    var allNotes : LiveData<List<MovieOfflineModel>>

    private val _contentExistsLiveData = MutableLiveData<Boolean>()
    val contentExistsLiveData: LiveData<Boolean> = _contentExistsLiveData

    private var repository: OfflineRepository


    init {
        val dao=OfflineDB.getDatabase(application).getItemDao()
        repository= OfflineRepository(dao)
        allNotes=repository.getAllNote()
    }

    //Deleting...
    fun deleteItem(contentId:Long) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.delete(contentId)
        } catch (e: Exception) {
            Log.d("ABHISHEK_6", "Error deleting item from database: ${e.message}")
        }
    }

    //Inserting...
    fun addItem(offlineModel: MovieOfflineModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(offlineModel)
    }

    //Searching...
    fun searching(contentId: Long)=viewModelScope.launch(Dispatchers.IO){
        val contentExists = repository.getAllTheContentId(contentId)
        _contentExistsLiveData.postValue(contentExists)
    }
    //Searching...
    suspend fun longPressCheck(contentId: Long): Boolean = withContext(Dispatchers.IO) {
        return@withContext repository.getAllTheContentId(contentId)
    }

}