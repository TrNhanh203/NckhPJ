package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

data class TaiKhoanWithRole(
    val id: Int,
    val tenTaiKhoan: String,
    val matKhau: String,
    val vaiTroId: Int,
    val tenVaiTro: String, // ✅ Lấy tên vai trò từ bảng VaiTro
    val hoTen: String?,  // ✅ Họ và tên
    val email: String?,  // ✅ Email
    val soDienThoai: String?,  // ✅ Số điện thoại
    var trangThai: String,  // ✅ Trạng thái
    var lastLogin: Long?,  // ✅ Lần đăng nhập cuối cùng
    val donViId: Int?  // ✅ Đơn vị trực thuộc (nếu có)
)

data class TaiKhoanChiTiet(
    val id: Int,
    val tenTaiKhoan: String,
    val hoTen: String?,
    val email: String?,
    val soDienThoai: String?,
    val trangThai: String,
    val tenVaiTro: String,
    val tenDonVi: String? // Chỉ có nếu là Quản lý đơn vị
)


// 7. TaiKhoanDao
@Dao
interface TaiKhoanDao {

    @Query("SELECT * FROM tai_khoan WHERE id = :id")
    fun getById(id: Int): Flow<TaiKhoan?>

    @Transaction
    @Query("""
    SELECT tai_khoan.*, vai_tro.tenVaiTro, don_vi.tenDonVi 
    FROM tai_khoan
    INNER JOIN vai_tro ON tai_khoan.vaiTroId = vai_tro.id
    LEFT JOIN don_vi ON tai_khoan.donViId = don_vi.id
    WHERE tai_khoan.id = :taiKhoanId
    """)
    suspend fun getTaiKhoanChiTiet(taiKhoanId: Int): TaiKhoanChiTiet?


    @Query("UPDATE tai_khoan SET trangThai = :trangThai WHERE id = :userId")
    suspend fun updateTrangThai(userId: Int, trangThai: String) // ✅ Cập nhật trạng thái tài khoản

    @Query("SELECT * FROM don_vi ORDER BY tenDonVi ASC")
    fun getAllDonVi(): Flow<List<DonVi>> // ✅ Lấy danh sách đơn vị

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTaiKhoan(taiKhoan: TaiKhoan): Long

    @Transaction
    @Query("""
    SELECT tai_khoan.id, tai_khoan.tenTaiKhoan, tai_khoan.matKhau, tai_khoan.vaiTroId, 
           vai_tro.tenVaiTro, tai_khoan.hoTen, tai_khoan.email, tai_khoan.soDienThoai, 
           tai_khoan.trangThai, tai_khoan.lastLogin, tai_khoan.donViId
    FROM tai_khoan 
    INNER JOIN vai_tro ON tai_khoan.vaiTroId = vai_tro.id
    ORDER BY tai_khoan.hoTen ASC
""")
    fun getAllWithRole(): Flow<List<TaiKhoanWithRole>> // ✅ Lấy danh sách tài khoản có vai trò


    @Query("SELECT DISTINCT trangThai FROM tai_khoan")
    fun getAllTrangThai(): Flow<List<String>> // ✅ Lấy danh sách trạng thái duy nhất



    @Transaction
    @Query("""
        SELECT tai_khoan.*, vai_tro.tenVaiTro 
        FROM tai_khoan 
        INNER JOIN vai_tro ON tai_khoan.vaiTroId = vai_tro.id
        WHERE tai_khoan.tenTaiKhoan = :username AND tai_khoan.matKhau = :password
        LIMIT 1
    """)
    suspend fun getTaiKhoanWithRole(username: String, password: String): TaiKhoanWithRole?

    @Query("SELECT * FROM tai_khoan")
    fun getAll(): Flow<List<TaiKhoan>>

    @Query("SELECT * FROM tai_khoan WHERE tenTaiKhoan = :username AND matKhau = :password LIMIT 1")
    suspend fun getTaiKhoan(username: String, password: String): TaiKhoan?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taiKhoan: TaiKhoan)

    @Update
    suspend fun update(taiKhoan: TaiKhoan)

    @Delete
    suspend fun delete(taiKhoan: TaiKhoan)
}