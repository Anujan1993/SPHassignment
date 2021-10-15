package com.anujan.sphassignment.api

import com.anujan.sphassignment.response.MainResponse
import com.anujan.sphassignment.util.EndPoints
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET(EndPoints.GET_DATA)
    suspend fun getMobileData(
        @Query("resource_id") key: String?
    ): MainResponse

}