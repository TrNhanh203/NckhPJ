package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 15. ChiTietBaoCaoDao
@Dao
interface ChiTietYeuCauDao {
    @Query("DELETE FROM chi_tiet_yeu_cau WHERE yeuCauId = :yeuCauId")
    suspend fun deleteByYeuCauId(yeuCauId: Int)

    @Query("""
    UPDATE chi_tiet_yeu_cau
    SET loaiYeuCau = :loaiYeuCau, moTa = :moTa
    WHERE yeuCauId = :yeuCauId AND thietBiId = :thietBiId
    """)
    suspend fun updateChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String)

    @Query("SELECT * FROM chi_tiet_yeu_cau WHERE yeuCauId = :yeuCauId AND thietBiId = :thietBiId LIMIT 1")
    suspend fun getChiTietYeuCauByYeuCauAndThietBi(yeuCauId: Int, thietBiId: Int): ChiTietYeuCau?

    @Query("DELETE FROM chi_tiet_yeu_cau WHERE id = :chiTietId")
    suspend fun deleteChiTietYeuCau(chiTietId: Int)

    @Insert
    suspend fun insertChiTietYeuCau(chiTietYeuCau: ChiTietYeuCau)

    @Query("SELECT * FROM chi_tiet_yeu_cau WHERE yeuCauId = :yeuCauId")
    fun getChiTietYeuCauByYeuCau(yeuCauId: Int): Flow<List<ChiTietYeuCau>>

    @Query("SELECT * FROM chi_tiet_yeu_cau")
    fun getAll(): Flow<List<ChiTietYeuCau>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chiTietYeuCau: ChiTietYeuCau)

    @Update
    suspend fun update(chiTietYeuCau: ChiTietYeuCau)

    @Delete
    suspend fun delete(chiTietYeuCau: ChiTietYeuCau)
}