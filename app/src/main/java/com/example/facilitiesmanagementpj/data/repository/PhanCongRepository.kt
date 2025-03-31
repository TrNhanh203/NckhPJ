package com.example.facilitiesmanagementpj.data.repository

import androidx.room.Insert
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.utils.TrangThaiChungCuaPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 11. PhanCongRepository
@Singleton
class PhanCongRepository @Inject constructor(private val phanCongDao: PhanCongDao, private val phanCongKtvDao: PhanCongKtvDao) {




    suspend fun getPhanCongIdByPhanCongKtvId(phanCongKtvId: Int): Int {
        return phanCongDao.getPhanCongIdByPhanCongKtvId(phanCongKtvId)
    }

    suspend fun capNhatTrangThaiPhanCong(phanCongId: Int) {
        val dsKtv = phanCongKtvDao.getAllByPhanCongId(phanCongId) // lấy tất cả PhanCongKtv liên quan

        val allTrangThai = dsKtv.map { it.trangThai }

        val newTrangThai = when {
            allTrangThai.all { it == TrangThaiPhanCong.HOAN_THANH } -> TrangThaiChungCuaPhanCong.HOAN_THANH
            allTrangThai.all { it == TrangThaiPhanCong.CHO_PHAN_HOI || it == TrangThaiPhanCong.DA_TU_CHOI || it == TrangThaiPhanCong.BI_HUY} ->
                TrangThaiChungCuaPhanCong.CHUA_BAT_DAU
            allTrangThai.any { it == TrangThaiPhanCong.DANG_THUC_HIEN } ->
                TrangThaiChungCuaPhanCong.DANG_THUC_HIEN
            allTrangThai.all { it == TrangThaiPhanCong.DA_CHAP_NHAN || it == TrangThaiPhanCong.TAM_NGHI } ->
                TrangThaiChungCuaPhanCong.DANG_TAM_NGHI
//            allTrangThai.all {
//                it == TrangThaiPhanCong.DA_TU_CHOI || it == TrangThaiPhanCong.BI_HUY || it == TrangThaiPhanCong.THAY_NGUOI
//            } -> TrangThaiChungCuaPhanCong.BI_HUY
            else -> TrangThaiChungCuaPhanCong.DANG_THUC_HIEN // fallback nếu trạng thái trộn
        }

        // Cập nhật trạng thái của bản ghi phân công gốc
        phanCongDao.updateTrangThai(phanCongId, newTrangThai)
    }

    suspend fun getSoTaskDangLam(taiKhoanId: Int): Int {
        return phanCongKtvDao.countSoTaskDangLam(taiKhoanId)
    }

    suspend fun getDsKtvByPhanCongId(phanCongId: Int): List<PhanCongKtvWithTaiKhoan> {
        return phanCongKtvDao.getByPhanCongIdWithTaiKhoan(phanCongId)
    }

    suspend fun hasPhanCongForChiTiet(chiTietId: Int): Boolean {
        return phanCongDao.hasPhanCongForChiTiet(chiTietId)
    }

    // Lấy PhanCongId từ ChiTietYeuCauId
    suspend fun getPhanCongIdByChiTietYeuCauId(chiTietYeuCauId: Int): Int? {
        return phanCongDao.getPhanCongIdByChiTietYeuCauId(chiTietYeuCauId)
    }

    fun getAllPhanCong(): Flow<List<PhanCong>> = phanCongDao.getAll()
    suspend fun insert(phanCong: PhanCong) = phanCongDao.insert(phanCong)
    suspend fun update(phanCong: PhanCong) = phanCongDao.update(phanCong)
    suspend fun delete(phanCong: PhanCong) = phanCongDao.delete(phanCong)

    suspend fun insertAndGetId(phanCong: PhanCong): Long {
        return phanCongDao.insertAndGetId(phanCong)
    }

    suspend fun getPhanCongByChiTietYeuCau(chiTietId: Int): PhanCong? {
        return phanCongDao.getByChiTietYeuCauId(chiTietId)
    }

    suspend fun getPhanCongById(id: Int): PhanCong? {
        return phanCongDao.getById(id)
    }


}