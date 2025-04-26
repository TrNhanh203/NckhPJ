package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 1. LoaiThietBiRepository
//@Singleton
//class LoaiThietBiRepository @Inject constructor(private val loaiThietBiDao: LoaiThietBiDao) {
//    fun getLoaiThietBiByThietBiId(thietBiId: Int): Flow<LoaiThietBi> = loaiThietBiDao.getLoaiThietBiByThietBiId(thietBiId)
//
//    fun getAllLoaiThietBi(): Flow<List<LoaiThietBi>> = loaiThietBiDao.getAll()
//    suspend fun insert(loaiThietBi: LoaiThietBi) = loaiThietBiDao.insert(loaiThietBi)
//    suspend fun update(loaiThietBi: LoaiThietBi) = loaiThietBiDao.update(loaiThietBi)
//    suspend fun delete(loaiThietBi: LoaiThietBi) = loaiThietBiDao.delete(loaiThietBi)
//}
@Singleton
class LoaiThietBiRepository @Inject constructor(
    private val loaiThietBiDao: LoaiThietBiDao,
    private val syncService: BaseFirestoreSyncService<LoaiThietBi>
) {
    fun getLoaiThietBiByThietBiId(thietBiId: Int): Flow<LoaiThietBi> =
        loaiThietBiDao.getLoaiThietBiByThietBiId(thietBiId)

    fun getAllLoaiThietBi(): Flow<List<LoaiThietBi>> = loaiThietBiDao.getAll()

//    suspend fun insert(loaiThietBi: LoaiThietBi) {
//        loaiThietBiDao.insert(loaiThietBi)
//        SyncDispatcher.dispatch(syncService, loaiThietBi, SyncDispatcher.SyncType.INSERT)
//    }

    suspend fun insert(loaiThietBi: LoaiThietBi) {
        val id = loaiThietBiDao.insertAndReturnId(loaiThietBi).toInt()
        val updated = loaiThietBi.copy(id = id)
        SyncDispatcher.dispatch(syncService, updated, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(loaiThietBi: LoaiThietBi) {
        loaiThietBiDao.update(loaiThietBi)
        SyncDispatcher.dispatch(syncService, loaiThietBi, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(loaiThietBi: LoaiThietBi) {
        loaiThietBiDao.delete(loaiThietBi)
        SyncDispatcher.dispatch(syncService, loaiThietBi, SyncDispatcher.SyncType.DELETE)
    }
}
