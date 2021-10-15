package com.anujan.sphassignment.response

import com.google.gson.annotations.SerializedName

data class Result (
	@SerializedName("records") val records : List<Records>
)