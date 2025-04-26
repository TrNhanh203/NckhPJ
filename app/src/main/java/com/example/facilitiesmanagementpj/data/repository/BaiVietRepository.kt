package com.example.facilitiesmanagementpj.data.repository

import android.util.Log
import com.example.facilitiesmanagementpj.data.dao.BaiVietDao
import com.example.facilitiesmanagementpj.data.entity.BaiViet
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class BaiVietRepository @Inject constructor(
//    private val baiVietDao: BaiVietDao
//) {
//    fun getAll(): Flow<List<BaiViet>> = baiVietDao.getAll()
//
//    suspend fun insert(baiViet: BaiViet) {
//        baiVietDao.insert(baiViet)
//    }
//
//    suspend fun delete(baiViet: BaiViet) {
//        baiVietDao.delete(baiViet)
//    }
//
//    suspend fun deleteAll() {
//        baiVietDao.deleteAll()
//    }
//}

@Singleton
class BaiVietRepository @Inject constructor(
    private val baiVietDao: BaiVietDao,
    private val syncService: BaseFirestoreSyncService<BaiViet>
) {
    fun getAll(): Flow<List<BaiViet>> = baiVietDao.getAll()

//    suspend fun insert(baiViet: BaiViet) {
//        baiVietDao.insert(baiViet)
//        SyncDispatcher.dispatch(syncService, baiViet, SyncDispatcher.SyncType.INSERT)
//    }

    suspend fun insert(baiViet: BaiViet) {
        val id = baiVietDao.insertAndReturnId(baiViet).toInt()
        val baiVietWithId = baiViet.copy(id = id)
        SyncDispatcher.dispatch(syncService, baiVietWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun delete(baiViet: BaiViet) {
        baiVietDao.delete(baiViet)
        SyncDispatcher.dispatch(syncService, baiViet, SyncDispatcher.SyncType.DELETE)
    }

//    suspend fun deleteAll() {
//        val all = baiVietDao.getAllOnce()
//        baiVietDao.deleteAll()
//        all.forEach {
//            SyncDispatcher.dispatch(syncService, it, SyncDispatcher.SyncType.DELETE)
//        }
//    }
}

