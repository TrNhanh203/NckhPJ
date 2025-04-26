package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 6. VaiTroRepository
//@Singleton
//class VaiTroRepository @Inject constructor(private val vaiTroDao: VaiTroDao) {
//    fun getAllVaiTro(): Flow<List<VaiTro>> = vaiTroDao.getAll()
//    suspend fun insert(vaiTro: VaiTro) = vaiTroDao.insert(vaiTro)
//    suspend fun update(vaiTro: VaiTro) = vaiTroDao.update(vaiTro)
//    suspend fun delete(vaiTro: VaiTro) = vaiTroDao.delete(vaiTro)
//}

@Singleton
class VaiTroRepository @Inject constructor(
    private val vaiTroDao: VaiTroDao,
    private val syncService: BaseFirestoreSyncService<VaiTro> // 🔄 Bổ sung sync
) {
    fun getAllVaiTro(): Flow<List<VaiTro>> = vaiTroDao.getAll()

//    suspend fun insert(vaiTro: VaiTro) {
//        vaiTroDao.insert(vaiTro)
//        SyncDispatcher.dispatch(syncService, vaiTro, SyncDispatcher.SyncType.INSERT) // 🔄 Bổ sung
//    }

    suspend fun insert(entity: VaiTro) {
        val id = vaiTroDao.insertAndReturnId(entity).toInt()
        val entityWithId = entity.copy(id = id)
        SyncDispatcher.dispatch(syncService, entityWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(vaiTro: VaiTro) {
        vaiTroDao.update(vaiTro)
        SyncDispatcher.dispatch(syncService, vaiTro, SyncDispatcher.SyncType.UPDATE) // 🔄 Bổ sung
    }

    suspend fun delete(vaiTro: VaiTro) {
        vaiTroDao.delete(vaiTro)
        SyncDispatcher.dispatch(syncService, vaiTro, SyncDispatcher.SyncType.DELETE) // 🔄 Bổ sung
    }
}
