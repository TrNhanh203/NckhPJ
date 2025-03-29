package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 12. PhanCongKtvRepository
@Singleton
class PhanCongKtvRepository @Inject constructor(private val phanCongKtvDao: PhanCongKtvDao) {

    suspend fun xinGiaHan(phanCongKtvId: Int, thoiGian: Int) {
        phanCongKtvDao.xinGiaHan(phanCongKtvId, thoiGian)
    }

    suspend fun getById(id: Int): PhanCongKtv? {
        return phanCongKtvDao.getById(id)
    }

    suspend fun updateTrangThaiPhanCongKtv(id: Int, trangThai: String) {
        phanCongKtvDao.updateTrangThaiPhanCongKtv(id, trangThai)
    }

    suspend fun updateTrangThaiVaTrangThaiCuoiCung(id: Int, trangThai: String, trangThaiCuoiCung: String) {
        phanCongKtvDao.updateTrangThaiVaTrangThaiCuoiCung(id, trangThai, trangThaiCuoiCung)
    }

    suspend fun updateTrangThaiVaChapNhan(id: Int, trangThai: String, daChapNhan: Boolean) {
        phanCongKtvDao.updateTrangThaiVaChapNhan(id, trangThai, daChapNhan)
    }

    suspend fun updateTuChoiPhanCong(id: Int, trangThai: String, thoiGianTuChoi: Long, lyDo: String?) {
        phanCongKtvDao.updateTuChoiPhanCong(id, trangThai, thoiGianTuChoi, lyDo)
    }

    // Lấy số lượng kỹ thuật viên theo trạng thái
    suspend fun getTechnicianCountByStatus(phanCongId: Int, trangThai: String): Int {
        return phanCongKtvDao.getTechnicianCountByStatus(phanCongId, trangThai)
    }


    fun getAllPhanCongKtv(): Flow<List<PhanCongKtv>> = phanCongKtvDao.getAll()
    suspend fun insert(phanCongKtv: PhanCongKtv) = phanCongKtvDao.insert(phanCongKtv)
    suspend fun update(phanCongKtv: PhanCongKtv) = phanCongKtvDao.update(phanCongKtv)
    suspend fun delete(phanCongKtv: PhanCongKtv) = phanCongKtvDao.delete(phanCongKtv)

}