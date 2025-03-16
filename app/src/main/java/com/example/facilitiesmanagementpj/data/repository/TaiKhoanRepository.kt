package com.example.facilitiesmanagementpj.data.repository

import android.util.Log
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

// 7. TaiKhoanRepository
@Singleton
class TaiKhoanRepository @Inject constructor(private val taiKhoanDao: TaiKhoanDao) {

    suspend fun getTaiKhoanChiTiet(userId: Int): TaiKhoanChiTiet? {
        return taiKhoanDao.getTaiKhoanChiTiet(userId)
    }

    suspend fun updateTrangThai(userId: Int, trangThai: String) {
        taiKhoanDao.updateTrangThai(userId, trangThai) // ✅ Gọi hàm cập nhật trạng thái từ DAO
    }

    fun getAllDonVi(): Flow<List<DonVi>> = taiKhoanDao.getAllDonVi()

    suspend fun registerTaiKhoan(taiKhoan: TaiKhoan) {
        taiKhoanDao.insertTaiKhoan(taiKhoan)
    }

    fun getAllTaiKhoanVoiVaiTro(): Flow<List<TaiKhoanWithRole>> = taiKhoanDao.getAllWithRole()

    fun getAllTrangThai(): Flow<List<String>> = taiKhoanDao.getAllTrangThai()





    fun getAllTaiKhoan(): Flow<List<TaiKhoan>> = taiKhoanDao.getAll()
    suspend fun insert(taiKhoan: TaiKhoan) = taiKhoanDao.insert(taiKhoan)
    suspend fun update(taiKhoan: TaiKhoan) = taiKhoanDao.update(taiKhoan)
    suspend fun delete(taiKhoan: TaiKhoan) = taiKhoanDao.delete(taiKhoan)

    suspend fun getTaiKhoan(username: String, password: String): TaiKhoan? {
        return taiKhoanDao.getTaiKhoan(username, password)
    }

    suspend fun validateLogin(username: String, password: String): TaiKhoanWithRole? {
        return taiKhoanDao.getTaiKhoanWithRole(username, password)
    }
}