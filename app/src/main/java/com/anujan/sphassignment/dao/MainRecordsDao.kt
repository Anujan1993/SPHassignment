package com.anujan.sphassignment.dao

import androidx.room.*
import com.anujan.sphassignment.entity.MainRecords

@Dao
interface MainRecordsDao {

    @Query("SELECT * FROM MainRecords")
    fun getListOfMainRecords(): List<MainRecords>?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(mainRecords: MainRecords): Long

    @Query("DELETE FROM MainRecords")
    suspend fun deleteAll(): Int
}