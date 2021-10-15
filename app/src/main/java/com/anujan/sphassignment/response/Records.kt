package com.anujan.sphassignment.response

import com.google.gson.annotations.SerializedName


data class Records (
	@SerializedName("volume_of_mobile_data") val volume_of_mobile_data : String,
	@SerializedName("quarter") val quarter : String,
	@SerializedName("_id") val _id : Int
)