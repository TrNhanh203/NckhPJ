package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*


// 7. TaiKhoanDao
@Dao
interface TaiKhoanDao {
    @Query("SELECT * FROM tai_khoan")
    fun getAll(): Flow<List<TaiKhoan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taiKhoan: TaiKhoan)

    @Update
    suspend fun update(taiKhoan: TaiKhoan)

    @Delete
    suspend fun delete(taiKhoan: TaiKhoan)
}