package com.anujan.sphassignment.home

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.anujan.sphassignment.entity.MainRecords
import com.anujan.sphassignment.entity.RecordsRoom
import com.anujan.sphassignment.repository.MobileDataRepository
import com.anujan.sphassignment.repository.UserRepository
import com.anujan.sphassignment.util.*
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val mobileDataRepository: MobileDataRepository,
    private var sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
) : ViewModel() {

    fun getMobileData() =  liveData(Dispatchers.IO) {
        emit(Resource.loading( null))
        emit( mobileDataRepository.getMobileData())
    }


    fun getSingleData(value:String) =  liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        emit( mobileDataRepository.getMobileSingleData(value))
    }

    fun loggedInOrNot(): Boolean {
        return sharedPreferences.loadLoginSharedPrefState()
    }
    fun loadThemeMode(): Boolean {
        return sharedPreferences.loadNightModeState()
    }

    fun getListData():List<MainRecords>? {
        return userRepository.getListData()
    }
    fun deleteData():Int{
        return userRepository.deleteList()
    }

    fun saveData(records: MainRecords): Long{
        return userRepository.saveListData(records)
    }


    fun getRecordData(year:String):List<RecordsRoom>? {
        return  userRepository.getRecordData(year)
    }
    fun deleteRecordData(year: String):Int{
        return userRepository.deleteRecord(year)
    }
    fun saveRecordData(records: RecordsRoom):Long{
        return userRepository.saveRecordData(records)
    }

}