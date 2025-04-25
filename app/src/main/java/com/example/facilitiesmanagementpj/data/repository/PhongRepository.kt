package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 5. PhongRepository
//@Singleton
//class PhongRepository @Inject constructor(private val phongDao: PhongDao) {
//
//    fun getPhongByDonVi(donViId: Int): Flow<List<PhongWithDetails>> {
//        return phongDao.getPhongByDonVi(donViId)
//    }
//
//    suspend fun getById(id: Int): Phong? {
//        return phongDao.getById(id)
//    }
//
//
//
//    fun getAllPhong(): Flow<List<Phong>> = phongDao.getAll()
//
//    //fun getPhongTheoDay(): Flow<List<Phong>> = phongDao.getPhongTheoDay()
//    suspend fun insert(phong: Phong) = phongDao.insert(phong)
//    suspend fun update(phong: Phong) = phongDao.update(phong)
//    suspend fun delete(phong: Phong) = phongDao.delete(phong)
//
//    suspend fun capNhatDonViId(phongId: Int, donViId: Int) = phongDao.capNhatDonViId(phongId, donViId)
//
//    suspend fun getAllPhongList(): List<Phong> {
//        return phongDao.getAllList() // ✅ Trả về List<Phong>
//    }
//
//    fun getPhongTheoDonVi(donViId: Int): Flow<List<Phong>> {
//        return phongDao.getPhongTheoDonVi(donViId)
//    }
//
//    fun getAllPhongWithLoaiPhong(): Flow<List<PhongWithLoaiPhong>> = phongDao.getPhongWithLoaiPhong()
//
//
//
//    fun getPhongByTangId(tangId: Int): Flow<List<Phong>> = phongDao.getPhongByTangId(tangId)
//
//}

@Singleton
class PhongRepository @Inject constructor(
    private val phongDao: PhongDao,
    private val syncService: BaseFirestoreSyncService<Phong> // 🔄 Bổ sung sync service
) {

    fun getPhongByDonVi(donViId: Int): Flow<List<PhongWithDetails>> {
        return phongDao.getPhongByDonVi(donViId)
    }

    suspend fun getById(id: Int): Phong? {
        return phongDao.getById(id)
    }

    fun getAllPhong(): Flow<List<Phong>> = phongDao.getAll()

    suspend fun insert(phong: Phong) {
        phongDao.insert(phong)
        SyncDispatcher.dispatch(syncService, phong, SyncDispatcher.SyncType.INSERT) // 🔄 Bổ sung
    }

    suspend fun update(phong: Phong) {
        phongDao.update(phong)
        SyncDispatcher.dispatch(syncService, phong, SyncDispatcher.SyncType.UPDATE) // 🔄 Bổ sung
    }

    suspend fun delete(phong: Phong) {
        phongDao.delete(phong)
        SyncDispatcher.dispatch(syncService, phong, SyncDispatcher.SyncType.DELETE) // 🔄 Bổ sung
    }

    suspend fun capNhatDonViId(phongId: Int, donViId: Int) {
        phongDao.capNhatDonViId(phongId, donViId)
        val updated = phongDao.getById(phongId)
        if (updated != null) {
            SyncDispatcher.dispatch(syncService, updated, SyncDispatcher.SyncType.UPDATE) // 🔄 Bổ sung
        }
    }

    suspend fun getAllPhongList(): List<Phong> {
        return phongDao.getAllList()
    }

    fun getPhongTheoDonVi(donViId: Int): Flow<List<Phong>> {
        return phongDao.getPhongTheoDonVi(donViId)
    }

    fun getAllPhongWithLoaiPhong(): Flow<List<PhongWithLoaiPhong>> =
        phongDao.getPhongWithLoaiPhong()

    fun getPhongByTangId(tangId: Int): Flow<List<Phong>> =
        phongDao.getPhongByTangId(tangId)
}
