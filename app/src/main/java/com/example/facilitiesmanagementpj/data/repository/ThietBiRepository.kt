package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 8. ThietBiRepository
//@Singleton
//class ThietBiRepository @Inject constructor(private val thietBiDao: ThietBiDao) {
//
//    suspend fun updateThietBiStatus(thietBiId: Int, trangThai: String) {
//        val thietBi = thietBiDao.getThietBiById(thietBiId)
//        thietBi?.let {
//            // Cập nhật trạng thái thiết bị
//            val updatedThietBi = it.copy(trangThai = trangThai)
//            thietBiDao.updateThietBiStatus(updatedThietBi)
//        }
//    }
//
//    suspend fun getThietBiById(id: Int): ThietBi? = thietBiDao.getThietBiById(id)
//
//    fun getThietBiByDonVi(donViId: Int): Flow<List<ThietBiWithDetails>> {
//        return thietBiDao.getThietBiByDonVi(donViId)
//    }
//
//    fun getThietBiByPhong(phongId: Int): Flow<List<ThietBiWithDetails>> {
//        return thietBiDao.getThietBiByPhong(phongId)
//    }
//
//    fun getAllThietBiWithDetails(): Flow<List<ThietBiWithDetails>> {
//        return thietBiDao.getAllThietBiWithDetails()
//    }
//
//    suspend fun getById(id: Int?): ThietBi? = thietBiDao.getThietBiById(id)
//
//    fun getAllThietBi(): Flow<List<ThietBi>> = thietBiDao.getAll()
//    fun getThietBiCanBaoDuong(ngayHienTai: Long): Flow<List<ThietBi>> =
//        thietBiDao.getThietBiCanBaoDuong(ngayHienTai)
//    suspend fun insert(thietBi: ThietBi) = thietBiDao.insert(thietBi)
//    suspend fun update(thietBi: ThietBi) = thietBiDao.update(thietBi)
//    suspend fun delete(thietBi: ThietBi) = thietBiDao.delete(thietBi)
//}
@Singleton
class ThietBiRepository @Inject constructor(
    private val thietBiDao: ThietBiDao,
    private val syncService: BaseFirestoreSyncService<ThietBi> // 🔄 Bổ sung sync
) {

    suspend fun updateThietBiStatus(thietBiId: Int, trangThai: String) {
        val thietBi = thietBiDao.getThietBiById(thietBiId)
        thietBi?.let {
            val updatedThietBi = it.copy(trangThai = trangThai)
            thietBiDao.updateThietBiStatus(updatedThietBi)
            SyncDispatcher.dispatch(syncService, updatedThietBi, SyncDispatcher.SyncType.UPDATE) // 🔄 Bổ sung
        }
    }

    suspend fun getThietBiById(id: Int): ThietBi? = thietBiDao.getThietBiById(id)

    fun getThietBiByDonVi(donViId: Int): Flow<List<ThietBiWithDetails>> =
        thietBiDao.getThietBiByDonVi(donViId)

    fun getThietBiByPhong(phongId: Int): Flow<List<ThietBiWithDetails>> =
        thietBiDao.getThietBiByPhong(phongId)

    fun getAllThietBiWithDetails(): Flow<List<ThietBiWithDetails>> =
        thietBiDao.getAllThietBiWithDetails()

    suspend fun getById(id: Int?): ThietBi? = thietBiDao.getThietBiById(id)

    fun getAllThietBi(): Flow<List<ThietBi>> = thietBiDao.getAll()

    fun getThietBiCanBaoDuong(ngayHienTai: Long): Flow<List<ThietBi>> =
        thietBiDao.getThietBiCanBaoDuong(ngayHienTai)

//    suspend fun insert(thietBi: ThietBi) {
//        thietBiDao.insert(thietBi)
//        SyncDispatcher.dispatch(syncService, thietBi, SyncDispatcher.SyncType.INSERT) // 🔄 Bổ sung
//    }

    suspend fun insert(entity: ThietBi) {
        val id = thietBiDao.insertAndReturnId(entity).toInt()
        val entityWithId = entity.copy(id = id)
        SyncDispatcher.dispatch(syncService, entityWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(thietBi: ThietBi) {
        thietBiDao.update(thietBi)
        SyncDispatcher.dispatch(syncService, thietBi, SyncDispatcher.SyncType.UPDATE) // 🔄 Bổ sung
    }

    suspend fun delete(thietBi: ThietBi) {
        thietBiDao.delete(thietBi)
        SyncDispatcher.dispatch(syncService, thietBi, SyncDispatcher.SyncType.DELETE) // 🔄 Bổ sung
    }
}
