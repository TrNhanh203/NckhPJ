package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 16. AnhMinhChungLamViecRepository
//@Singleton
//class AnhMinhChungLamViecRepository @Inject constructor(private val anhMinhChungLamViecDao: AnhMinhChungLamViecDao) {
//
//    suspend fun getByPhanCongKtvId(phanCongKtvId: Int): List<AnhMinhChungLamViec> {
//        return anhMinhChungLamViecDao.getMediaByPhanCongKtvId(phanCongKtvId)
//    }
//
//    fun getAllAnhMinhChungLamViec(): Flow<List<AnhMinhChungLamViec>> = anhMinhChungLamViecDao.getAll()
//    suspend fun insert(anhMinhChungLamViec: AnhMinhChungLamViec) = anhMinhChungLamViecDao.insert(anhMinhChungLamViec)
//    suspend fun update(anhMinhChungLamViec: AnhMinhChungLamViec) = anhMinhChungLamViecDao.update(anhMinhChungLamViec)
//    suspend fun delete(anhMinhChungLamViec: AnhMinhChungLamViec) = anhMinhChungLamViecDao.delete(anhMinhChungLamViec)
//}
@Singleton
class AnhMinhChungLamViecRepository @Inject constructor(
    private val anhMinhChungLamViecDao: AnhMinhChungLamViecDao,
    private val syncService: BaseFirestoreSyncService<AnhMinhChungLamViec>
) {

    suspend fun getByPhanCongKtvId(phanCongKtvId: Int): List<AnhMinhChungLamViec> {
        return anhMinhChungLamViecDao.getMediaByPhanCongKtvId(phanCongKtvId)
    }

    fun getAllAnhMinhChungLamViec(): Flow<List<AnhMinhChungLamViec>> =
        anhMinhChungLamViecDao.getAll()

//    suspend fun insert(anh: AnhMinhChungLamViec) {
//        anhMinhChungLamViecDao.insert(anh)
//        SyncDispatcher.dispatch(syncService, anh, SyncDispatcher.SyncType.INSERT)
//    }

    suspend fun insert(anh: AnhMinhChungLamViec) {
        val id = anhMinhChungLamViecDao.insertAndReturnId(anh).toInt()
        val anhWithId = anh.copy(id = id)
        SyncDispatcher.dispatch(syncService, anhWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(anh: AnhMinhChungLamViec) {
        anhMinhChungLamViecDao.update(anh)
        SyncDispatcher.dispatch(syncService, anh, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(anh: AnhMinhChungLamViec) {
        anhMinhChungLamViecDao.delete(anh)
        SyncDispatcher.dispatch(syncService, anh, SyncDispatcher.SyncType.DELETE)
    }
}
