package com.example.dump.model.tvdetailsModel


import com.google.gson.annotations.SerializedName

data class SpokenLanguage(
    @SerializedName("english_name")
    var englishName: String?,
    @SerializedName("iso_639_1")
    var iso6391: String?,
    @SerializedName("name")
    var name: String?
)