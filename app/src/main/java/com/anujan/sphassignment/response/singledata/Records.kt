package com.anujan.sphassignment.response.singledata

import com.google.gson.annotations.SerializedName

data class Records (
	@SerializedName("volume_of_mobile_data") val volume_of_mobile_data : String,
	@SerializedName("quarter") val quarter : String,
	@SerializedName("_id") val _id : Int,
	@SerializedName("_full_count") val _full_count : String,
	@SerializedName("rank") val rank : Double
)