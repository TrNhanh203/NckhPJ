package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 10. KyThuatVienDao
@Dao
interface KyThuatVienDao {
    @Query("SELECT * FROM ky_thuat_vien")
    fun getAll(): Flow<List<KyThuatVien>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kyThuatVien: KyThuatVien)

    @Update
    suspend fun update(kyThuatVien: KyThuatVien)

    @Delete
    suspend fun delete(kyThuatVien: KyThuatVien)
}