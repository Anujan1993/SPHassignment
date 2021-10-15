package com.anujan.sphassignment.util

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anujan.sphassignment.entity.AppUser
import com.anujan.sphassignment.dao.AppUserDao
import com.anujan.sphassignment.dao.MainRecordsDao
import com.anujan.sphassignment.entity.MainRecords

@Database(entities = [AppUser::class,MainRecords::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appUserDao(): AppUserDao
    abstract fun mainRecordsDao():MainRecordsDao
}