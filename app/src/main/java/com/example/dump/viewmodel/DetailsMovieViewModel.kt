package com.example.dump.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dump.model.moviedetailsModel.MoviesDetailsData
import com.example.dump.network.DetailsEndPoint
import kotlinx.coroutines.launch
import retrofit2.Response


class DetailsMovieViewModel(private val detailsEndPoint : DetailsEndPoint):ViewModel() {

    private val _movieDetailsLiveData = MutableLiveData<MoviesDetailsData>()
    val movieDetailsLiveData : LiveData<MoviesDetailsData>
        get() = _movieDetailsLiveData


    val progress =  MutableLiveData<Boolean>()


    fun fetchData(id:Long) {
        viewModelScope.launch {
            try {

                Log.i("APPDATA", "API call")
                val data: Response<MoviesDetailsData> = detailsEndPoint.getMovieDetails(id)

                if(data.isSuccessful && data.body()!=null) {
                    Log.i("APPDATA", "API RESPONSE SUCCESS")
                    progress.postValue(false)
                    _movieDetailsLiveData.postValue(data.body())
                }
                else{
                    progress.postValue(true)
                    Log.i("APPDATA", "API RESPONSE FAILED")
                }
            } catch (e: Exception) {
                // Handle the error
                e.printStackTrace()
                Log.i("APPDATA", "API ERROR")
            }
        }
    }
}
