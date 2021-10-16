package com.anujan.sphassignment.repository

import com.anujan.sphassignment.api.ApiInterface
import com.anujan.sphassignment.response.MainResponse
import com.anujan.sphassignment.response.singledata.MainSingleData
import com.anujan.sphassignment.util.EndPoints
import com.anujan.sphassignment.util.Resource
import retrofit2.HttpException
import javax.inject.Inject

class MobileDataRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    suspend fun getMobileData(): Resource<MainResponse> {
        return try {
            val response = apiInterface.getMobileData(EndPoints.API_KEY)
            return Resource.success(response)
        } catch (e: HttpException) {
            return Resource.error(null,e.message())
        }
    }
    suspend fun getMobileSingleData(value:String): Resource<MainSingleData> {
        return try {
            val response = apiInterface.getSingleData(EndPoints.API_KEY,value)
            return Resource.success(response)
        } catch (e: HttpException) {
            return Resource.error(null,e.message())
        }
    }
}
