package com.example.facilitiesmanagementpj.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.facilitiesmanagementpj.data.entity.BaiViet
import com.example.facilitiesmanagementpj.data.entity.DanhGiaKTV
import kotlinx.coroutines.flow.Flow

@Dao
interface DanhGiaKTVDao {
    @Query("SELECT * FROM danh_gia_ktv")
    fun getAll(): Flow<List<DanhGiaKTV>>

    @Query("SELECT * FROM danh_gia_ktv WHERE phanCongKtvId = :pcKtvId")
    fun getByPhanCongKtvId(pcKtvId: Int): Flow<List<DanhGiaKTV>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(danhGia: DanhGiaKTV)

    @Query("SELECT AVG(diem) FROM danh_gia_ktv WHERE phanCongKtvId = :pcKtvId")
    suspend fun getAverageRating(pcKtvId: Int): Float?
}
