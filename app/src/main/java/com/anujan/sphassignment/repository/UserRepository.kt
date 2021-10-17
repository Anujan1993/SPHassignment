package com.anujan.sphassignment.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anujan.sphassignment.dao.AppUserDao
import com.anujan.sphassignment.dao.MainRecordsDao
import com.anujan.sphassignment.dao.RecordsRoomDao
import com.anujan.sphassignment.entity.AppUser
import com.anujan.sphassignment.entity.MainRecords
import com.anujan.sphassignment.entity.RecordsRoom
import com.anujan.sphassignment.util.loginSharedPrefState
import com.anujan.sphassignment.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    var userDao: AppUserDao,
    var mainRecordsDao: MainRecordsDao,
    var recordsDao: RecordsRoomDao,
    private val sharedPref: SharedPreferences
) {

    suspend fun login(email: String, passwordHash: String): Result<AppUser>{
        var result : Result<AppUser>
        withContext(Dispatchers.IO) {
            val user = userDao.findByUserNameAndPassword(email, passwordHash)
            if (user != null) {
                sharedPref.loginSharedPrefState(true)
                result= Result.Success(user)
            } else {
                result = Result.Error(Exception("User not exist!"))
            }
        }
        return result
    }

    suspend fun registerUser(
        name: String,
        email: String,
        phone: String,
        country: String,
        passwordHash: String
    ): Result<String> {
        var registerResult : Result<String>
        withContext(Dispatchers.IO) {
            try {
                userDao.insert(AppUser(0, name, phone, country, passwordHash, email))
                registerResult = Result.Success("Registration Success")
            }
            catch (exception : Exception){
                registerResult = Result.Error(Exception("email id already exist!"))
            }
        }
        return registerResult
    }

    fun deleteList(): Int {
        return mainRecordsDao.deleteAll()
    }

    fun saveListData(mainRecords: MainRecords): Long {
        return mainRecordsDao.insert(mainRecords)
    }

    fun getListData(): List<MainRecords>? {
        return mainRecordsDao.getListOfMainRecords()
    }

    fun deleteRecord(year:String): Int {
        return recordsDao.deleteRecord(year)
    }

    fun saveRecordData(recordsRoom: RecordsRoom): Long {
        return recordsDao.insertRecord(recordsRoom)
    }

    fun getRecordData(year: String):List<RecordsRoom>? {
        return recordsDao.getListOfRecords(year)
    }

}
