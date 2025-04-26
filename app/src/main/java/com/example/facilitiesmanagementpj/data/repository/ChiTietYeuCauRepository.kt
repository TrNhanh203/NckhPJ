package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 14. ChiTietBaoCaoRepository
//@Singleton
//class ChiTietYeuCauRepository @Inject constructor(private val chiTietYeuCauDao: ChiTietYeuCauDao) {
//
//    suspend fun getAllByYeuCauId(yeuCauId: Int): List<ChiTietYeuCau> = chiTietYeuCauDao.getAllByYeuCauId(yeuCauId)
//
//    suspend fun getById(chiTietId: Int): ChiTietYeuCau? = chiTietYeuCauDao.getChiTietYeuCauByChiTietId(chiTietId)
//    fun getAllChiTietYeuCau(): Flow<List<ChiTietYeuCau>> = chiTietYeuCauDao.getAll()
//    suspend fun insert(chiTietYeuCau: ChiTietYeuCau) = chiTietYeuCauDao.insert(chiTietYeuCau)
//    suspend fun update(chiTietYeuCau: ChiTietYeuCau) = chiTietYeuCauDao.update(chiTietYeuCau)
//    suspend fun delete(chiTietYeuCau: ChiTietYeuCau) = chiTietYeuCauDao.delete(chiTietYeuCau)
//}

@Singleton
class ChiTietYeuCauRepository @Inject constructor(
    private val chiTietYeuCauDao: ChiTietYeuCauDao,
    private val syncService: BaseFirestoreSyncService<ChiTietYeuCau>
) {

    suspend fun getAllByYeuCauId(yeuCauId: Int): List<ChiTietYeuCau> =
        chiTietYeuCauDao.getAllByYeuCauId(yeuCauId)

    suspend fun getById(chiTietId: Int): ChiTietYeuCau? =
        chiTietYeuCauDao.getChiTietYeuCauByChiTietId(chiTietId)

    fun getAllChiTietYeuCau(): Flow<List<ChiTietYeuCau>> =
        chiTietYeuCauDao.getAll()

//    suspend fun insert(chiTiet: ChiTietYeuCau) {
//        chiTietYeuCauDao.insert(chiTiet)
//        SyncDispatcher.dispatch(syncService, chiTiet, SyncDispatcher.SyncType.INSERT)
//    }

    suspend fun insert(entity: ChiTietYeuCau) {
        val id = chiTietYeuCauDao.insertAndReturnId(entity).toInt()
        val entityWithId = entity.copy(id = id)
        SyncDispatcher.dispatch(syncService, entityWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(chiTiet: ChiTietYeuCau) {
        chiTietYeuCauDao.update(chiTiet)
        SyncDispatcher.dispatch(syncService, chiTiet, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(chiTiet: ChiTietYeuCau) {
        chiTietYeuCauDao.delete(chiTiet)
        SyncDispatcher.dispatch(syncService, chiTiet, SyncDispatcher.SyncType.DELETE)
    }
}
