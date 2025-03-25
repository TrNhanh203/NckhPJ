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
    // Lấy số lượng kỹ thuật viên theo trạng thái
    suspend fun getTechnicianCountByStatus(phanCongId: Int, trangThai: String): Int {
        return phanCongKtvDao.getTechnicianCountByStatus(phanCongId, trangThai)
    }


    fun getAllPhanCongKtv(): Flow<List<PhanCongKtv>> = phanCongKtvDao.getAll()
    suspend fun insert(phanCongKtv: PhanCongKtv) = phanCongKtvDao.insert(phanCongKtv)
    suspend fun update(phanCongKtv: PhanCongKtv) = phanCongKtvDao.update(phanCongKtv)
    suspend fun delete(phanCongKtv: PhanCongKtv) = phanCongKtvDao.delete(phanCongKtv)

}