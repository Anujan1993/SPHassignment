package com.anujan.sphassignment.util

sealed class Result<out R>{

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
    data class Canceled(val exception: Exception?) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Canceled -> "Canceled[exception=$exception]"
            else -> "Loading"
        }
    }

}

