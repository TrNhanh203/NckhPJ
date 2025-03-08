package com.example.facilitiesmanagementpj.data.dao


import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 1. LoaiThietBiDao
@Dao
interface LoaiThietBiDao {
    @Query("SELECT * FROM loai_thiet_bi")
    fun getAll(): Flow<List<LoaiThietBi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(loaiThietBi: LoaiThietBi)

    @Update
    suspend fun update(loaiThietBi: LoaiThietBi)

    @Delete
    suspend fun delete(loaiThietBi: LoaiThietBi)

}