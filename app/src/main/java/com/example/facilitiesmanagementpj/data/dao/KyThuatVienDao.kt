package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 10. KyThuatVienDao
@Dao
interface KyThuatVienDao {

    @Query("UPDATE ky_thuat_vien SET ngayBatDauLam = :ngay WHERE taiKhoanId = :taiKhoanId")
    suspend fun updateNgayBatDauLamByTaiKhoanId(taiKhoanId: Int, ngay: Long)


    @Query("SELECT * FROM ky_thuat_vien WHERE taiKhoanId = :taiKhoanId LIMIT 1")
    fun getByTaiKhoanId(taiKhoanId: Int): Flow<KyThuatVien?>


    @Query("""
    SELECT * FROM ky_thuat_vien 
    WHERE taiKhoanId = :taiKhoanId
    """)
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

    @Update
    suspend fun update(kyThuatVien: KyThuatVien)

    @Delete
    suspend fun delete(kyThuatVien: KyThuatVien)

    @Query("""
        SELECT 
            ky_thuat_vien.id AS ktv_id,
            ky_thuat_vien.taiKhoanId AS ktv_taiKhoanId,
            ky_thuat_vien.kinhNghiem AS ktv_kinhNghiem,
            ky_thuat_vien.ngayBatDauLam AS ktv_ngayBatDauLam,
            ky_thuat_vien.trangThaiHienTai AS ktv_trangThaiHienTai,
            ky_thuat_vien.ghiChu AS ktv_ghiChu,

            tai_khoan.id AS tk_id,
            tai_khoan.tenTaiKhoan AS tk_tenTaiKhoan,
            tai_khoan.matKhau AS tk_matKhau,
            tai_khoan.vaiTroId AS tk_vaiTroId,
            tai_khoan.soDienThoai AS tk_soDienThoai,
            tai_khoan.email AS tk_email,
            tai_khoan.hoTen AS tk_hoTen,
            tai_khoan.trangThai AS tk_trangThai,
            tai_khoan.lastLogin AS tk_lastLogin,
            tai_khoan.donViId AS tk_donViId
        FROM ky_thuat_vien
        INNER JOIN tai_khoan ON ky_thuat_vien.taiKhoanId = tai_khoan.id
        WHERE (:trangThai IS NULL OR ky_thuat_vien.trangThaiHienTai = :trangThai)
    """)
    suspend fun getAllWithTaiKhoanByTrangThai(trangThai: String?): List<KyThuatVienWithTaiKhoanImpl>

    @Query("""
        SELECT 
            ky_thuat_vien.id AS ktv_id,
            ky_thuat_vien.taiKhoanId AS ktv_taiKhoanId,
            ky_thuat_vien.kinhNghiem AS ktv_kinhNghiem,
            ky_thuat_vien.ngayBatDauLam AS ktv_ngayBatDauLam,
            ky_thuat_vien.trangThaiHienTai AS ktv_trangThaiHienTai,
            ky_thuat_vien.ghiChu AS ktv_ghiChu,

            tai_khoan.id AS tk_id,
            tai_khoan.tenTaiKhoan AS tk_tenTaiKhoan,
            tai_khoan.matKhau AS tk_matKhau,
            tai_khoan.vaiTroId AS tk_vaiTroId,
            tai_khoan.soDienThoai AS tk_soDienThoai,
            tai_khoan.email AS tk_email,
            tai_khoan.hoTen AS tk_hoTen,
            tai_khoan.trangThai AS tk_trangThai,
            tai_khoan.lastLogin AS tk_lastLogin,
            tai_khoan.donViId AS tk_donViId
        FROM ky_thuat_vien
        INNER JOIN tai_khoan ON ky_thuat_vien.taiKhoanId = tai_khoan.id
        WHERE (:trangThai IS NULL OR ky_thuat_vien.trangThaiHienTai = :trangThai)
          AND ky_thuat_vien.id IN (
              SELECT kyThuatVienId FROM chuyen_mon_ky_thuat_vien
              WHERE chuyenMonId IN (:chuyenMonIds)
              GROUP BY kyThuatVienId
              HAVING COUNT(DISTINCT chuyenMonId) = :soLuongChuyenMon
          )
    """)
    suspend fun getWithFilter(
        trangThai: String?,
        chuyenMonIds: List<Int>,
        soLuongChuyenMon: Int
    ): List<KyThuatVienWithTaiKhoanImpl>

    data class KyThuatVienWithTaiKhoanImpl(
        @Embedded(prefix = "ktv_") val kyThuatVien: KyThuatVien,
        @Embedded(prefix = "tk_") val taiKhoan: TaiKhoan
    )
}