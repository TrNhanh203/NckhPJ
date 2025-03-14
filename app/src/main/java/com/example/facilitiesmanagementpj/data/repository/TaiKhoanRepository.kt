package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 7. TaiKhoanRepository
@Singleton
class TaiKhoanRepository @Inject constructor(private val taiKhoanDao: TaiKhoanDao) {
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