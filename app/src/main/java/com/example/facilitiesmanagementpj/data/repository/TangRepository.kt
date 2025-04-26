package com.example.facilitiesmanagementpj.data.repository


import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 4. TangRepository
//@Singleton
//class TangRepository @Inject constructor(private val tangDao: TangDao) {
//    fun getAllTang(): Flow<List<Tang>> = tangDao.getAll()
//    suspend fun insert(tang: Tang) = tangDao.insert(tang)
//    suspend fun update(tang: Tang) = tangDao.update(tang)
//    suspend fun delete(tang: Tang) = tangDao.delete(tang)
//
//    suspend fun getById(id: Int): Tang? {
//        return tangDao.getById(id)
//    }
//
//
//    val getTangWithDay: Flow<List<TangWithDay>> = tangDao.getTangWithDay()
//
//    fun getTangByDayId(dayId: Int): Flow<List<Tang>> = tangDao.getTangByDayId(dayId)
//}
@Singleton
class TangRepository @Inject constructor(
    private val tangDao: TangDao,
    private val syncService: BaseFirestoreSyncService<Tang> // 🔄 Bổ sung sync
) {
    fun getAllTang(): Flow<List<Tang>> = tangDao.getAll()

//    suspend fun insert(tang: Tang) {
//        tangDao.insert(tang)
//        SyncDispatcher.dispatch(syncService, tang, SyncDispatcher.SyncType.INSERT) // 🔄 Bổ sung
//    }

    suspend fun insert(entity: Tang) {
        val id = tangDao.insertAndReturnId(entity).toInt()
        val entityWithId = entity.copy(id = id)
        SyncDispatcher.dispatch(syncService, entityWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(tang: Tang) {
        tangDao.update(tang)
        SyncDispatcher.dispatch(syncService, tang, SyncDispatcher.SyncType.UPDATE) // 🔄 Bổ sung
    }

    suspend fun delete(tang: Tang) {
        tangDao.delete(tang)
        SyncDispatcher.dispatch(syncService, tang, SyncDispatcher.SyncType.DELETE) // 🔄 Bổ sung
    }

    suspend fun getById(id: Int): Tang? {
        return tangDao.getById(id)
    }

    val getTangWithDay: Flow<List<TangWithDay>> = tangDao.getTangWithDay()

    fun getTangByDayId(dayId: Int): Flow<List<Tang>> = tangDao.getTangByDayId(dayId)
}
