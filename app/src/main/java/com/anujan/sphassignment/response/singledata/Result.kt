package com.anujan.sphassignment.response.singledata

import com.google.gson.annotations.SerializedName

data class Result (
    @SerializedName("records") val records : List<Records>,
    @SerializedName("total") val total : Int
)