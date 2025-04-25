package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 2. LoaiPhongRepository
//@Singleton
//class LoaiPhongRepository @Inject constructor(private val loaiPhongDao: LoaiPhongDao) {
//    fun getAllLoaiPhong(): Flow<List<LoaiPhong>> = loaiPhongDao.getAll()
//    suspend fun insert(loaiPhong: LoaiPhong) = loaiPhongDao.insert(loaiPhong)
//    suspend fun update(loaiPhong: LoaiPhong) = loaiPhongDao.update(loaiPhong)
//    suspend fun delete(loaiPhong: LoaiPhong) = loaiPhongDao.delete(loaiPhong)
//}
@Singleton
class LoaiPhongRepository @Inject constructor(
    private val loaiPhongDao: LoaiPhongDao,
    private val syncService: BaseFirestoreSyncService<LoaiPhong>
) {
    fun getAllLoaiPhong(): Flow<List<LoaiPhong>> = loaiPhongDao.getAll()

    suspend fun insert(loaiPhong: LoaiPhong) {
        loaiPhongDao.insert(loaiPhong)
        SyncDispatcher.dispatch(syncService, loaiPhong, SyncDispatcher.SyncType.INSERT)
    }

    suspend fun update(loaiPhong: LoaiPhong) {
        loaiPhongDao.update(loaiPhong)
        SyncDispatcher.dispatch(syncService, loaiPhong, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(loaiPhong: LoaiPhong) {
        loaiPhongDao.delete(loaiPhong)
        SyncDispatcher.dispatch(syncService, loaiPhong, SyncDispatcher.SyncType.DELETE)
    }
}

