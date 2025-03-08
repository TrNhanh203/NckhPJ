package com.example.facilitiesmanagementpj.data.dao


import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 16. PhanCongThietBiDao
@Dao
interface PhanCongThietBiDao {
    @Query("SELECT * FROM phan_cong_thiet_bi")
    fun getAll(): Flow<List<PhanCongThietBi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(phanCongThietBi: PhanCongThietBi)

    @Update
    suspend fun update(phanCongThietBi: PhanCongThietBi)

    @Delete
    suspend fun delete(phanCongThietBi: PhanCongThietBi)
}