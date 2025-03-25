package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 13. PhanCongKTVDao
@Dao
interface PhanCongKtvDao {
    // Truy vấn số lượng kỹ thuật viên theo trạng thái
    @Query("SELECT COUNT(*) FROM phan_cong_ktv WHERE phanCongId = :phanCongId AND trangThai = :trangThai")
    suspend fun getTechnicianCountByStatus(phanCongId: Int, trangThai: String): Int

    @Query("SELECT * FROM phan_cong_ktv")
    fun getAll(): Flow<List<PhanCongKtv>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(phanCongKTV: PhanCongKtv)

    @Update
    suspend fun update(phanCongKTV: PhanCongKtv)

    @Delete
    suspend fun delete(phanCongKTV: PhanCongKtv)
}