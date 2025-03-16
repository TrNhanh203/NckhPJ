package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

data class ThietBiWithLoai(
    val id: Int,
    val tenThietBi: String,
    val trangThai: String,
    val tenLoai: String
)

// 8. ThietBiDao
@Dao
interface ThietBiDao {
    @Query("""
    SELECT thiet_bi.id, thiet_bi.tenThietBi, thiet_bi.trangThai, loai_thiet_bi.tenLoai
    FROM thiet_bi
    INNER JOIN loai_thiet_bi ON thiet_bi.loaiThietBiId = loai_thiet_bi.id
    WHERE thiet_bi.phongId = :phongId
    """)
    fun getThietBiByPhong(phongId: Int): Flow<List<ThietBiWithLoai>>


    @Query("SELECT * FROM thiet_bi")
    fun getAll(): Flow<List<ThietBi>>

    @Query("SELECT * FROM thiet_bi WHERE ngayBaoDuongTiepTheo <= :ngayHienTai")
    fun getThietBiCanBaoDuong(ngayHienTai: Long): Flow<List<ThietBi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(thietBi: ThietBi)

    @Update
    suspend fun update(thietBi: ThietBi)

    @Delete
    suspend fun delete(thietBi: ThietBi)
}