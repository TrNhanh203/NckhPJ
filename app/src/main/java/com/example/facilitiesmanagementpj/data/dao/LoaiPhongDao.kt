package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 2. LoaiPhongDao
@Dao
interface LoaiPhongDao {
    @Query("SELECT * FROM loai_phong")
    fun getAll(): Flow<List<LoaiPhong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(loaiPhong: LoaiPhong)

    @Update
    suspend fun update(loaiPhong: LoaiPhong)

    @Delete
    suspend fun delete(loaiPhong: LoaiPhong)
}