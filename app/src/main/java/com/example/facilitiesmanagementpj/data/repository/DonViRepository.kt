package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class DonViRepository @Inject constructor(private val donViDao: DonViDao) {
//    fun getAllDonVi(): Flow<List<DonVi>> = donViDao.getAll()
//
//    fun getAllDonViSorted(): Flow<List<DonVi>> = donViDao.getAllDonVi()
//
//    suspend fun getById(id: Int): DonVi? {
//        return donViDao.getById(id)
//    }
//
//
//    suspend fun insert(donVi: DonVi) {
//        donViDao.insert(donVi)
//    }
//
//    suspend fun update(donVi: DonVi) {
//        donViDao.update(donVi)
//    }
//
//    suspend fun delete(donVi: DonVi) {
//        donViDao.delete(donVi)
//    }
//}

@Singleton
class DonViRepository @Inject constructor(
    private val donViDao: DonViDao,
    private val syncService: BaseFirestoreSyncService<DonVi>
) {
    fun getAllDonVi(): Flow<List<DonVi>> = donViDao.getAll()

    fun getAllDonViSorted(): Flow<List<DonVi>> = donViDao.getAllDonVi()

    suspend fun getById(id: Int): DonVi? {
        return donViDao.getById(id)
    }

//    suspend fun insert(donVi: DonVi) {
//        donViDao.insert(donVi)
//        SyncDispatcher.dispatch(syncService, donVi, SyncDispatcher.SyncType.INSERT)
//    }

    suspend fun insert(entity: DonVi) {
        val id = donViDao.insertAndReturnId(entity).toInt()
        val entityWithId = entity.copy(id = id)
        SyncDispatcher.dispatch(syncService, entityWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(donVi: DonVi) {
        donViDao.update(donVi)
        SyncDispatcher.dispatch(syncService, donVi, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(donVi: DonVi) {
        donViDao.delete(donVi)
        SyncDispatcher.dispatch(syncService, donVi, SyncDispatcher.SyncType.DELETE)
    }
}

