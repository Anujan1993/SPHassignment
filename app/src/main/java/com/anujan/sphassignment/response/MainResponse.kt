package com.anujan.sphassignment.response

import com.google.gson.annotations.SerializedName

data class MainResponse(
    @SerializedName("result") val result: Result
)