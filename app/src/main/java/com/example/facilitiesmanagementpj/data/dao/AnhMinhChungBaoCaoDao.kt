package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 16. AnhMinhChungDao
@Dao
interface AnhMinhChungBaoCaoDao {
    @Query("SELECT * FROM anh_minh_chung_bao_cao WHERE chiTietBaoCaoId = :chiTietId")
    suspend fun getByChiTietId(chiTietId: Int): List<AnhMinhChungBaoCao>


    @Query("DELETE FROM anh_minh_chung_bao_cao WHERE chiTietBaoCaoId = :chiTietBaoCaoId")
    suspend fun deleteByChiTietBaoCaoId(chiTietBaoCaoId: Int)

    @Query("DELETE FROM anh_minh_chung_bao_cao WHERE urlAnh = :path")
    suspend fun deleteByPath(path: String)

    @Query("SELECT * FROM anh_minh_chung_bao_cao WHERE urlAnh = :path LIMIT 1")
    suspend fun getByPath(path: String): AnhMinhChungBaoCao?

    @Query("SELECT * FROM anh_minh_chung_bao_cao")
    fun getAll(): Flow<List<AnhMinhChungBaoCao>>

    @Query("SELECT * FROM anh_minh_chung_bao_cao WHERE type = 'image' AND chiTietBaoCaoId = :chiTietBaoCaoId")
    suspend fun getImagesByChiTietBaoCaoId(chiTietBaoCaoId: Int): List<AnhMinhChungBaoCao>

    @Query("SELECT * FROM anh_minh_chung_bao_cao WHERE type = 'video' AND chiTietBaoCaoId = :chiTietBaoCaoId")
    suspend fun getVideosByChiTietBaoCaoId(chiTietBaoCaoId: Int): List<AnhMinhChungBaoCao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anhMinhChung: AnhMinhChungBaoCao)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndReturnId(anhMinhChung: AnhMinhChungBaoCao): Long


    @Update
    suspend fun update(anhMinhChung: AnhMinhChungBaoCao)

    @Delete
    suspend fun delete(anhMinhChung: AnhMinhChungBaoCao)

    @Query("DELETE FROM anh_minh_chung_bao_cao WHERE id = :id")
    suspend fun deleteById(id: Int)
}