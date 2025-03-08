package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 11. PhanCongDao
@Dao
interface PhanCongDao {
    @Query("SELECT * FROM phan_cong")
    fun getAll(): Flow<List<PhanCong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(phanCong: PhanCong)

    @Update
    suspend fun update(phanCong: PhanCong)

    @Delete
    suspend fun delete(phanCong: PhanCong)
}