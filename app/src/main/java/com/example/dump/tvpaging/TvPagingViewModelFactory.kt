package com.example.dump.tvpaging


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class TvPagingViewModelFactory constructor (private val tvPagingRepo : TvPagingRepo): ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TvPagingViewModel::class.java)) {
            TvPagingViewModel(this.tvPagingRepo) as T
        }
        else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}