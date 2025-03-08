package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 15. ChiTietBaoCaoDao
@Dao
interface ChiTietBaoCaoDao {
    @Query("SELECT * FROM chi_tiet_bao_cao")
    fun getAll(): Flow<List<ChiTietBaoCao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chiTietBaoCao: ChiTietBaoCao)

    @Update
    suspend fun update(chiTietBaoCao: ChiTietBaoCao)

    @Delete
    suspend fun delete(chiTietBaoCao: ChiTietBaoCao)
}