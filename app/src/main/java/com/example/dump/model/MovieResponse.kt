package com.example.dump.model


import com.google.gson.annotations.SerializedName


data class MovieResponse(

    @SerializedName("page")
    var page: Int?,

    @SerializedName("results")
    var results: List<Movies>,

    @SerializedName("total_pages")
    var totalPage: Int?,

    @SerializedName("total_results")
    var totalResult: Int?

)