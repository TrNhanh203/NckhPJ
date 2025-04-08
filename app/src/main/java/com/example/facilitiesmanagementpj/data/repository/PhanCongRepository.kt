package com.example.facilitiesmanagementpj.data.repository

import androidx.room.Insert
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.utils.TrangThaiChungCuaPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 11. PhanCongRepository
@Singleton
class PhanCongRepository @Inject constructor(private val phanCongDao: PhanCongDao, private val phanCongKtvDao: PhanCongKtvDao
, private val thietBiDao: ThietBiDao) {




    suspend fun getPhanCongIdByPhanCongKtvId(phanCongKtvId: Int): Int {
        return phanCongDao.getPhanCongIdByPhanCongKtvId(phanCongKtvId)
    }

    suspend fun getById(phanCongId: Int): PhanCong? {
        return phanCongDao.getById(phanCongId)
    }

    suspend fun capNhatTrangThaiPhanCong(phanCongId: Int) {
        val dsKtv = phanCongKtvDao.getAllByPhanCongId(phanCongId) // lấy tất cả PhanCongKtv liên quan

        val allTrangThai = dsKtv.map { it.trangThai }
        val trangThaiCoGiaTri = allTrangThai.filter {
            it != TrangThaiPhanCong.DA_TU_CHOI &&
                    it != TrangThaiPhanCong.BI_HUY
        }


        val newTrangThai = when {
            trangThaiCoGiaTri.isEmpty() -> TrangThaiChungCuaPhanCong.CHUA_BAT_DAU

            trangThaiCoGiaTri.all { it == TrangThaiPhanCong.HOAN_THANH } ->
                TrangThaiChungCuaPhanCong.HOAN_THANH

            trangThaiCoGiaTri.all {
                it == TrangThaiPhanCong.CHO_PHAN_HOI ||
                        it == TrangThaiPhanCong.DA_CHAP_NHAN
            } -> TrangThaiChungCuaPhanCong.CHUA_BAT_DAU

            trangThaiCoGiaTri.any { it == TrangThaiPhanCong.DANG_THUC_HIEN } ->
                TrangThaiChungCuaPhanCong.DANG_THUC_HIEN

            trangThaiCoGiaTri.all {
                it == TrangThaiPhanCong.DA_CHAP_NHAN ||
                        it == TrangThaiPhanCong.TAM_NGHI ||
                        it == TrangThaiPhanCong.CHO_PHAN_HOI
            } -> TrangThaiChungCuaPhanCong.DANG_TAM_NGHI

            else -> TrangThaiChungCuaPhanCong.DANG_THUC_HIEN
        }

        // Cập nhật trạng thái của bản ghi phân công gốc
        phanCongDao.updateTrangThai(phanCongId, newTrangThai)

        val pc = phanCongDao.getById(phanCongId)
        val tbId = pc?.thietBiId
        tbId?.let { id ->
            when {
                trangThaiCoGiaTri.any { it == TrangThaiPhanCong.DANG_THUC_HIEN } -> {
                    thietBiDao.updateTrangThai(id, TrangThaiThietBi.DANG_BAO_TRI)
                }

                trangThaiCoGiaTri.all { it == TrangThaiPhanCong.HOAN_THANH } -> {
                    thietBiDao.updateTrangThai(id, TrangThaiThietBi.DANG_HOAT_DONG)
                }

                trangThaiCoGiaTri.none { it == TrangThaiPhanCong.DANG_THUC_HIEN } -> {
                    thietBiDao.updateTrangThai(id, TrangThaiThietBi.CHO_BAO_TRI)
                }
            }
        }
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