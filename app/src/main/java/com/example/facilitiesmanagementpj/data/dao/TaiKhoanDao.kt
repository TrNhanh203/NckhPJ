package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

data class TaiKhoanWithRole(
    val id: Int,
    val tenTaiKhoan: String,
    val matKhau: String,
    val vaiTroId: Int,
    val tenVaiTro: String // ✅ Lấy luôn tên vai trò từ bảng VaiTro
)
// 7. TaiKhoanDao
@Dao
interface TaiKhoanDao {
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