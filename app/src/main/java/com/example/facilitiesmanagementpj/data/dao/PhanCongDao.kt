package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 11. PhanCongDao
@Dao
interface PhanCongDao {

    @Query("SELECT phanCongId FROM phan_cong_ktv WHERE id = :phanCongKtvId")
    suspend fun getPhanCongIdByPhanCongKtvId(phanCongKtvId: Int): Int


    @Query("UPDATE phan_cong SET trangThai = :trangThai WHERE id = :id")
    suspend fun updateTrangThai(id: Int, trangThai: String)

    // Truy vấn để lấy PhanCongId từ ChiTietYeuCauId
    @Query("SELECT id FROM phan_cong WHERE chiTietYeuCauId = :chiTietYeuCauId LIMIT 1")
    suspend fun getPhanCongIdByChiTietYeuCauId(chiTietYeuCauId: Int): Int?

    @Query("SELECT EXISTS (SELECT 1 FROM phan_cong WHERE chiTietYeuCauId = :chiTietId)")
    suspend fun hasPhanCongForChiTiet(chiTietId: Int): Boolean


    @Query("SELECT * FROM phan_cong WHERE id = :phanCongId LIMIT 1")
    suspend fun getById(phanCongId: Int): PhanCong?


    @Query("SELECT * FROM phan_cong")
    fun getAll(): Flow<List<PhanCong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(phanCong: PhanCong)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndGetId(phanCong: PhanCong): Long

    @Query("SELECT * FROM phan_cong WHERE chiTietYeuCauId = :chiTietId LIMIT 1")
    suspend fun getByChiTietYeuCauId(chiTietId: Int): PhanCong?


    @Update
    suspend fun update(phanCong: PhanCong)

    @Delete
    suspend fun delete(phanCong: PhanCong)
}