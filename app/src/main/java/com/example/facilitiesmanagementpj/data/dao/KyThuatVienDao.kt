package com.example.facilitiesmanagementpj.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 10. KyThuatVienDao
@Dao
interface KyThuatVienDao {

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
}