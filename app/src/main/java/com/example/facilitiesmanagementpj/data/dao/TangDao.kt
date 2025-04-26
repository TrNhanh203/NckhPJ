package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 4. TangDao
@Dao
interface TangDao {
    @Query("SELECT * FROM tang")
    fun getAll(): Flow<List<Tang>>

    @Query("SELECT * FROM tang WHERE id = :id")
    suspend fun getById(id: Int): Tang?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tang: Tang)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndReturnId(entity: Tang): Long

    @Update
    suspend fun update(tang: Tang)

    @Delete
    suspend fun delete(tang: Tang)

    @Query("""
        SELECT tang.id AS tangId, tang.tenTang, tang.dayId, day.tenDay
        FROM tang
        INNER JOIN day ON tang.dayId = day.id
    """)
    fun getTangWithDay(): Flow<List<TangWithDay>>

    @Query("SELECT * FROM tang WHERE dayId = :dayId")
    fun getTangByDayId(dayId: Int): Flow<List<Tang>>
}