package com.anujan.sphassignment.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anujan.sphassignment.entity.RecordsRoom

@Dao
interface RecordsRoomDao {

    @Query("SELECT * FROM RecordsRoom WHERE quarter LIKE :year|| '%'")
    fun getListOfRecords(year:String): List<RecordsRoom>?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertRecord(recordsRoom: RecordsRoom):Long


    @Query("DELETE FROM RecordsRoom WHERE quarter LIKE :year || '%'")
    fun deleteRecord(year: String): Int
}