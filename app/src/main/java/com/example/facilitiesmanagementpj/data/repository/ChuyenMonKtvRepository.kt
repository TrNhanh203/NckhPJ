package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 18. ChuyenMonKtvRepository
//@Singleton
//class ChuyenMonKtvRepository @Inject constructor(private val chuyenMonKtvDao: ChuyenMonKtvDao) {
//    fun getAllChuyenMonKtv(): Flow<List<ChuyenMonKtv>> = chuyenMonKtvDao.getAll()
//    suspend fun insert(chuyenMonKtv: ChuyenMonKtv) = chuyenMonKtvDao.insert(chuyenMonKtv)
//    suspend fun update(chuyenMonKtv: ChuyenMonKtv) = chuyenMonKtvDao.update(chuyenMonKtv)
//    suspend fun delete(chuyenMonKtv: ChuyenMonKtv) = chuyenMonKtvDao.delete(chuyenMonKtv)
//}

@Singleton
class ChuyenMonKtvRepository @Inject constructor(
    private val chuyenMonKtvDao: ChuyenMonKtvDao,
    private val syncService: BaseFirestoreSyncService<ChuyenMonKtv>
) {
    fun getAllChuyenMonKtv(): Flow<List<ChuyenMonKtv>> = chuyenMonKtvDao.getAll()

    suspend fun insert(chuyenMonKtv: ChuyenMonKtv) {
        chuyenMonKtvDao.insert(chuyenMonKtv)
        SyncDispatcher.dispatch(syncService, chuyenMonKtv, SyncDispatcher.SyncType.INSERT)
    }

    suspend fun update(chuyenMonKtv: ChuyenMonKtv) {
        chuyenMonKtvDao.update(chuyenMonKtv)
        SyncDispatcher.dispatch(syncService, chuyenMonKtv, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(chuyenMonKtv: ChuyenMonKtv) {
        chuyenMonKtvDao.delete(chuyenMonKtv)
        SyncDispatcher.dispatch(syncService, chuyenMonKtv, SyncDispatcher.SyncType.DELETE)
    }
}
