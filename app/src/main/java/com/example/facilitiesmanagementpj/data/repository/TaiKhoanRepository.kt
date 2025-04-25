package com.example.facilitiesmanagementpj.data.repository

import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

// 7. TaiKhoanRepository
//@Singleton
//class TaiKhoanRepository @Inject constructor(private val taiKhoanDao: TaiKhoanDao) {
//
//
//    suspend fun getTkById(id: Int): TaiKhoan? {
//        return taiKhoanDao.getById(id).firstOrNull()
//    }
//
//    fun getTaiKhoanById(id: Int): Flow<TaiKhoan?> = taiKhoanDao.getById(id)
//
//    suspend fun getTaiKhoanChiTiet(userId: Int): TaiKhoanChiTiet? {
//        return taiKhoanDao.getTaiKhoanChiTiet(userId)
//    }
//
//    suspend fun updateTrangThai(userId: Int, trangThai: String) {
//        taiKhoanDao.updateTrangThai(userId, trangThai) // ✅ Gọi hàm cập nhật trạng thái từ DAO
//    }
//
//    fun getAllDonVi(): Flow<List<DonVi>> = taiKhoanDao.getAllDonVi()
//
//    suspend fun registerTaiKhoan(taiKhoan: TaiKhoan): Int {
//        return taiKhoanDao.insertTaiKhoan(taiKhoan).toInt()
//    }
//
//    fun getAllTaiKhoanVoiVaiTro(): Flow<List<TaiKhoanWithRole>> = taiKhoanDao.getAllWithRole()
//
//    fun getAllTrangThai(): Flow<List<String>> = taiKhoanDao.getAllTrangThai()
//
//
//
//
//
//    fun getAllTaiKhoan(): Flow<List<TaiKhoan>> = taiKhoanDao.getAll()
//    suspend fun insert(taiKhoan: TaiKhoan) = taiKhoanDao.insert(taiKhoan)
//    suspend fun update(taiKhoan: TaiKhoan) = taiKhoanDao.update(taiKhoan)
//    suspend fun delete(taiKhoan: TaiKhoan) = taiKhoanDao.delete(taiKhoan)
//
//    suspend fun getTaiKhoan(username: String, password: String): TaiKhoan? {
//        return taiKhoanDao.getTaiKhoan(username, password)
//    }
//
//    @OptIn(UnstableApi::class)
//    suspend fun validateLogin(username: String, password: String): TaiKhoanWithRole? {
//
//        return taiKhoanDao.getTaiKhoanWithRole(username, password)
//
//    }
//
//    @OptIn(UnstableApi::class)
//    suspend fun validateLogin2(username: String, password: String): TaiKhoan? {
//
//        return taiKhoanDao.getTaiKhoan(username, password)
//
//    }
//}
@Singleton
class TaiKhoanRepository @Inject constructor(
    private val taiKhoanDao: TaiKhoanDao,
    private val syncService: BaseFirestoreSyncService<TaiKhoan> // 🔄 Bổ sung sync
) {

    suspend fun getTkById(id: Int): TaiKhoan? {
        return taiKhoanDao.getById(id).firstOrNull()
    }

    fun getTaiKhoanById(id: Int): Flow<TaiKhoan?> = taiKhoanDao.getById(id)

    suspend fun getTaiKhoanChiTiet(userId: Int): TaiKhoanChiTiet? {
        return taiKhoanDao.getTaiKhoanChiTiet(userId)
    }

    suspend fun updateTrangThai(userId: Int, trangThai: String) {
        taiKhoanDao.updateTrangThai(userId, trangThai)
        taiKhoanDao.getById(userId).firstOrNull()?.let {
            SyncDispatcher.dispatch(syncService, it, SyncDispatcher.SyncType.UPDATE) // 🔄 Đồng bộ trạng thái
        }
    }

    fun getAllDonVi(): Flow<List<DonVi>> = taiKhoanDao.getAllDonVi()

    suspend fun registerTaiKhoan(taiKhoan: TaiKhoan): Int {
        val id = taiKhoanDao.insertTaiKhoan(taiKhoan).toInt()
        SyncDispatcher.dispatch(syncService, taiKhoan.copy(id = id), SyncDispatcher.SyncType.INSERT) // 🔄 Bổ sung
        return id
    }

    fun getAllTaiKhoanVoiVaiTro(): Flow<List<TaiKhoanWithRole>> = taiKhoanDao.getAllWithRole()

    fun getAllTrangThai(): Flow<List<String>> = taiKhoanDao.getAllTrangThai()

    fun getAllTaiKhoan(): Flow<List<TaiKhoan>> = taiKhoanDao.getAll()

    suspend fun insert(taiKhoan: TaiKhoan) {
        taiKhoanDao.insert(taiKhoan)
        SyncDispatcher.dispatch(syncService, taiKhoan, SyncDispatcher.SyncType.INSERT) // 🔄 Bổ sung
    }

    suspend fun update(taiKhoan: TaiKhoan) {
        taiKhoanDao.update(taiKhoan)
        SyncDispatcher.dispatch(syncService, taiKhoan, SyncDispatcher.SyncType.UPDATE) // 🔄 Bổ sung
    }

    suspend fun delete(taiKhoan: TaiKhoan) {
        taiKhoanDao.delete(taiKhoan)
        SyncDispatcher.dispatch(syncService, taiKhoan, SyncDispatcher.SyncType.DELETE) // 🔄 Bổ sung
    }

    suspend fun getTaiKhoan(username: String, password: String): TaiKhoan? {
        return taiKhoanDao.getTaiKhoan(username, password)
    }

    @OptIn(UnstableApi::class)
    suspend fun validateLogin(username: String, password: String): TaiKhoanWithRole? {
        return taiKhoanDao.getTaiKhoanWithRole(username, password)
    }

    @OptIn(UnstableApi::class)
    suspend fun validateLogin2(username: String, password: String): TaiKhoan? {
        return taiKhoanDao.getTaiKhoan(username, password)
    }
}
