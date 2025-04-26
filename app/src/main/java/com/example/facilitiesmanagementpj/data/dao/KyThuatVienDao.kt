package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
import org.checkerframework.checker.units.qual.K

@Dao
interface KyThuatVienDao {

    @Query("UPDATE ky_thuat_vien SET ngayBatDauLam = :ngay WHERE taiKhoanId = :taiKhoanId")
    suspend fun updateNgayBatDauLamByTaiKhoanId(taiKhoanId: Int, ngay: Long)

    @Query("SELECT * FROM ky_thuat_vien WHERE taiKhoanId = :taiKhoanId LIMIT 1")
    fun getByTaiKhoanId(taiKhoanId: Int): Flow<KyThuatVien?>

    @Query("SELECT * FROM ky_thuat_vien WHERE taiKhoanId = :taiKhoanId")
    suspend fun getKyThuatVienByTaiKhoanId(taiKhoanId: Int): KyThuatVien?

    @Query("""
        SELECT chuyen_mon.tenChuyenMon
        FROM chuyen_mon_ky_thuat_vien
        INNER JOIN chuyen_mon ON chuyen_mon_ky_thuat_vien.chuyenMonId = chuyen_mon.id
        INNER JOIN ky_thuat_vien ON chuyen_mon_ky_thuat_vien.kyThuatVienId = ky_thuat_vien.id
        WHERE ky_thuat_vien.taiKhoanId = :taiKhoanId
    """)
    fun getChuyenMonCuaKTV(taiKhoanId: Int): Flow<List<String>>

    @Query("SELECT * FROM ky_thuat_vien")
    fun getAll(): Flow<List<KyThuatVien>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kyThuatVien: KyThuatVien)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndReturnId(entity: KyThuatVien): Long


    @Update
    suspend fun update(kyThuatVien: KyThuatVien)

    @Delete
    suspend fun delete(kyThuatVien: KyThuatVien)

    @Query("DELETE FROM ky_thuat_vien WHERE id = :id")
    suspend fun deleteById(id: Int)

    // Dùng @Relation để lấy KyThuatVien kèm TaiKhoan
    @Transaction
    @Query("SELECT * FROM ky_thuat_vien WHERE (:trangThai IS NULL OR trangThaiHienTai = :trangThai)")
    suspend fun getByTrangThaiWithTaiKhoan(trangThai: String?): List<KyThuatVienWithTaiKhoan>

    @Transaction
    @Query("SELECT * FROM ky_thuat_vien")
    suspend fun getAllWithTaiKhoan(): List<KyThuatVienWithTaiKhoan>
}
