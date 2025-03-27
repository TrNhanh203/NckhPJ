package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.relation.PhanCongKtvWithFullInfo

// 13. PhanCongKTVDao
@Dao
interface PhanCongKtvDao {

    @Transaction
    @Query("""
        SELECT 
            pck.*, 
            pc.id AS pc_id, pc.chiTietYeuCauId, pc.loaiPhanCong, pc.ghiChu, pc.mucDoUuTien, pc.trangThai AS pc_trangThai, pc.thoiGianTaoPhanCong, pc.soLuongKTVThamGia, pc.nguoiTaoPhanCong,
            tb.id AS tb_id, tb.tenThietBi, tb.loaiThietBiId, tb.ngayDaCat, tb.baoDuongDinhKy, tb.ngayBaoDuongGanNhat, tb.phongId, tb.moTa,
            ltb.id AS ltb_id, ltb.tenLoai
        FROM phan_cong_ktv AS pck
        INNER JOIN phan_cong AS pc ON pck.phanCongId = pc.id
        INNER JOIN thiet_bi AS tb ON pc.thietBiId = tb.id
        INNER JOIN loai_thiet_bi AS ltb ON tb.loaiThietBiId = ltb.id
        WHERE pck.taiKhoanKTVId = :ktvId
    """)
    suspend fun getWithFullInfo(ktvId: Int): List<PhanCongKtvWithFullInfo>


    @Query("""
        SELECT COUNT(*) FROM phan_cong_ktv
        WHERE taiKhoanKTVId = :taiKhoanId
        AND trangThai IN (
            'Đã Chấp Nhận',
            'Đang Thực Hiện',
            'Tạm Nghỉ'
        )
    """)
    suspend fun countSoTaskDangLam(taiKhoanId: Int): Int

    @Transaction
    @Query("SELECT * FROM phan_cong_ktv WHERE phanCongId = :phanCongId")
    suspend fun getByPhanCongIdWithTaiKhoan(phanCongId: Int): List<PhanCongKtvWithTaiKhoan>

    // Truy vấn số lượng kỹ thuật viên theo trạng thái
    @Query("SELECT COUNT(*) FROM phan_cong_ktv WHERE phanCongId = :phanCongId AND trangThai = :trangThai")
    suspend fun getTechnicianCountByStatus(phanCongId: Int, trangThai: String): Int

    @Query("SELECT * FROM phan_cong_ktv")
    fun getAll(): Flow<List<PhanCongKtv>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(phanCongKTV: PhanCongKtv)

    @Update
    suspend fun update(phanCongKTV: PhanCongKtv)

    @Delete
    suspend fun delete(phanCongKTV: PhanCongKtv)
}