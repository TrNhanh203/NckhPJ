package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*


// 3. DayDao
@Dao
interface DayDao {
    @Query("SELECT * FROM day")
    fun getAll(): Flow<List<Day>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(day: Day)

    @Update
    suspend fun update(day: Day)

    @Delete
    suspend fun delete(day: Day)
}