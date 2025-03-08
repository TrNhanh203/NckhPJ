package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 8. ThietBiDao
@Dao
interface ThietBiDao {
    @Query("SELECT * FROM thiet_bi")
    fun getAll(): Flow<List<ThietBi>>

    @Query("SELECT * FROM thiet_bi WHERE ngayBaoDuongTiepTheo <= :ngayHienTai")
    fun getThietBiCanBaoDuong(ngayHienTai: Long): Flow<List<ThietBi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(thietBi: ThietBi)

    @Update
    suspend fun update(thietBi: ThietBi)

    @Delete
    suspend fun delete(thietBi: ThietBi)
}