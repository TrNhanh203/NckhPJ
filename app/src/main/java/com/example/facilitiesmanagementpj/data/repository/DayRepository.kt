package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 3. DayRepository
//@Singleton
//class DayRepository @Inject constructor(private val dayDao: DayDao) {
//    fun getAllDay(): Flow<List<Day>> = dayDao.getAll()
//    suspend fun insert(day: Day) = dayDao.insert(day)
//    suspend fun update(day: Day) = dayDao.update(day)
//    suspend fun delete(day: Day) = dayDao.delete(day)
//
//    suspend fun getById(id: Int): Day? {
//        return dayDao.getById(id)
//    }
//
//}
@Singleton
class DayRepository @Inject constructor(
    private val dayDao: DayDao,
    private val syncService: BaseFirestoreSyncService<Day>
) {
    fun getAllDay(): Flow<List<Day>> = dayDao.getAll()

//    suspend fun insert(day: Day) {
//        dayDao.insert(day)
//        SyncDispatcher.dispatch(syncService, day, SyncDispatcher.SyncType.INSERT)
//    }

    suspend fun insert(entity: Day) {
        val id = dayDao.insertAndReturnId(entity).toInt()
        val entityWithId = entity.copy(id = id)
        SyncDispatcher.dispatch(syncService, entityWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(day: Day) {
        dayDao.update(day)
        SyncDispatcher.dispatch(syncService, day, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(day: Day) {
        dayDao.delete(day)
        SyncDispatcher.dispatch(syncService, day, SyncDispatcher.SyncType.DELETE)
    }

    suspend fun getById(id: Int): Day? {
        return dayDao.getById(id)
    }
}
