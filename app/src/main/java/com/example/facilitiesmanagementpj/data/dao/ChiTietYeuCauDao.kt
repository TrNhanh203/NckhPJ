package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 15. ChiTietBaoCaoDao
@Dao
interface ChiTietYeuCauDao {

    @Query("""
        SELECT
            c.id,
            c.yeuCauId,
            c.thietBiId,
            c.loaiYeuCau,
            c.moTa,
            t.loaiThietBiId,
            l.tenLoai AS tenLoaiThietBi,
            t.tenThietBi
        FROM chi_tiet_yeu_cau c
        LEFT JOIN thiet_bi t ON c.thietBiId = t.id
        LEFT JOIN loai_thiet_bi l ON t.loaiThietBiId = l.id
        WHERE c.yeuCauId = :yeuCauId
    """)
    fun getChiTietYeuCauWithThietBiAndLoaiThietBi(yeuCauId: Int): Flow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>>

    @Query(" SELECT id FROM chi_tiet_yeu_cau WHERE yeuCauId = :yeuCauId AND thietBiId = :thietBiId LIMIT 1")
    suspend fun getChiTietYeuCauIdByYeuCauIdVSThietBiId(yeuCauId: Int, thietBiId: Int): Int

    @Query("SELECT * FROM chi_tiet_yeu_cau WHERE id = :chiTietId LIMIT 1")
    suspend fun getChiTietYeuCauByChiTietId(chiTietId: Int): ChiTietYeuCau?

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

    @Query("DELETE FROM anh_minh_chung_bao_cao WHERE chiTietBaoCaoId = :chiTietBaoCaoId")
    suspend fun deleteAnhMinhChungByChiTietBaoCaoId(chiTietBaoCaoId: Int)

//    @Insert
//    suspend fun insertChiTietYeuCau(chiTietYeuCau: ChiTietYeuCau)

    @Insert
    suspend fun insertChiTietYeuCau(chiTietYeuCau: ChiTietYeuCau): Long

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