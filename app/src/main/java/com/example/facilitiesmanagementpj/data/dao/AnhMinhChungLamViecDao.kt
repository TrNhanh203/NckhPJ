package com.example.facilitiesmanagementpj.data.dao


import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 17. AnhMinhChungLamViecDao
@Dao
interface AnhMinhChungLamViecDao {

    @Query("SELECT * FROM anh_minh_chung_lam_viec WHERE phanCongKTVId = :phanCongKtvId ORDER BY thoiGianTaiLen ASC")
    suspend fun getMediaByPhanCongKtvId(phanCongKtvId: Int): List<AnhMinhChungLamViec>

    @Query("SELECT * FROM anh_minh_chung_lam_viec")
    fun getAll(): Flow<List<AnhMinhChungLamViec>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anhMinhChungLamViec: AnhMinhChungLamViec)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndReturnId(anh: AnhMinhChungLamViec): Long


    @Update
    suspend fun update(anhMinhChungLamViec: AnhMinhChungLamViec)

    @Delete
    suspend fun delete(anhMinhChungLamViec: AnhMinhChungLamViec)

    @Query("DELETE FROM anh_minh_chung_lam_viec WHERE id = :id")
    suspend fun deleteById(id: Int)
}