package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 15. ChiTietBaoCaoDao
@Dao
interface ChiTietYeuCauDao {
    @Query("SELECT * FROM chi_tiet_yeu_cau")
    fun getAll(): Flow<List<ChiTietYeuCau>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chiTietYeuCau: ChiTietYeuCau)

    @Update
    suspend fun update(chiTietYeuCau: ChiTietYeuCau)

    @Delete
    suspend fun delete(chiTietYeuCau: ChiTietYeuCau)
}