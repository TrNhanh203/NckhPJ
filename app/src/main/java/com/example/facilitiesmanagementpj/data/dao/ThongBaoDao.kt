package com.example.facilitiesmanagementpj.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.facilitiesmanagementpj.data.entity.ThongBao
import kotlinx.coroutines.flow.Flow

@Dao
interface ThongBaoDao {
    @Query("SELECT * FROM thong_bao")
    fun getAll(): Flow<List<ThongBao>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(thongBao: ThongBao)

    @Query("SELECT * FROM thong_bao WHERE nguoiNhanId = :nguoiNhanId ORDER BY thoiGian DESC")
    fun getAllByNguoiNhan(nguoiNhanId: Int): Flow<List<ThongBao>>

    @Query("UPDATE thong_bao SET daDoc = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("DELETE FROM thong_bao WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM thong_bao WHERE id = :id")
    suspend fun deleteById(id: Int)
}
