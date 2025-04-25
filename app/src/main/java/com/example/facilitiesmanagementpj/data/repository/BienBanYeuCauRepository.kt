package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class BienBanYeuCauRepository @Inject constructor(private val bienBanYeuCauDao: BienBanYeuCauDao) {
//    fun getBienBanByYeuCau(yeuCauId: Int): Flow<List<BienBanYeuCau>> = bienBanYeuCauDao.getBienBanByYeuCau(yeuCauId)
//
//    fun getAll(): Flow<List<BienBanYeuCau>> = bienBanYeuCauDao.getAll()
//    suspend fun insert(bienBanYeuCau: BienBanYeuCau) {
//        bienBanYeuCauDao.insert(bienBanYeuCau)
//    }
//
//    suspend fun update(bienBanYeuCau: BienBanYeuCau) {
//        bienBanYeuCauDao.update(bienBanYeuCau)
//    }
//
//    suspend fun delete(bienBanYeuCau: BienBanYeuCau) {
//        bienBanYeuCauDao.delete(bienBanYeuCau)
//    }
//}
@Singleton
class BienBanYeuCauRepository @Inject constructor(
    private val bienBanYeuCauDao: BienBanYeuCauDao,
    private val syncService: BaseFirestoreSyncService<BienBanYeuCau>
) {
    fun getBienBanByYeuCau(yeuCauId: Int): Flow<List<BienBanYeuCau>> =
        bienBanYeuCauDao.getBienBanByYeuCau(yeuCauId)

    fun getAll(): Flow<List<BienBanYeuCau>> = bienBanYeuCauDao.getAll()

    suspend fun insert(bienBan: BienBanYeuCau) {
        bienBanYeuCauDao.insert(bienBan)
        SyncDispatcher.dispatch(syncService, bienBan, SyncDispatcher.SyncType.INSERT)
    }

    suspend fun update(bienBan: BienBanYeuCau) {
        bienBanYeuCauDao.update(bienBan)
        SyncDispatcher.dispatch(syncService, bienBan, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(bienBan: BienBanYeuCau) {
        bienBanYeuCauDao.delete(bienBan)
        SyncDispatcher.dispatch(syncService, bienBan, SyncDispatcher.SyncType.DELETE)
    }
}

