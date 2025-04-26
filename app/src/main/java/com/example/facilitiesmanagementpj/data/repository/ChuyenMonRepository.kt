package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 9. ChuyenMonRepository
//@Singleton
//class ChuyenMonRepository @Inject constructor(
//    private val chuyenMonDao: ChuyenMonDao,
//    private val chuyenMonKtvDao: ChuyenMonKtvDao
//) {
//    suspend fun getChuyenMonIdsCuaKTV(kyThuatVienId: Int): List<Int> {
//        return chuyenMonKtvDao.getChuyenMonIdsByKyThuatVienId(kyThuatVienId)
//    }
//
//
//    suspend fun insert(chuyenMon: ChuyenMon) = chuyenMonDao.insert(chuyenMon)
//    suspend fun update(chuyenMon: ChuyenMon) = chuyenMonDao.update(chuyenMon)
//    suspend fun delete(chuyenMon: ChuyenMon) = chuyenMonDao.delete(chuyenMon)
//
//    fun getAllChuyenMon(): Flow<List<ChuyenMon>> {
//        return chuyenMonDao.getAll()
//    }
//
//    suspend fun getChuyenMonIdsByKtv(kyThuatVienId: Int): List<Int> {
//        return chuyenMonKtvDao.getChuyenMonIdsByKtvId(kyThuatVienId)
//    }
//
//    suspend fun updateChuyenMonForKtv(kyThuatVienId: Int, selectedIds: List<Int>) {
//        chuyenMonKtvDao.deleteAllByKtvId(kyThuatVienId)
//        selectedIds.forEach { chuyenMonId ->
//            chuyenMonKtvDao.insert(
//                ChuyenMonKtv(chuyenMonId = chuyenMonId, kyThuatVienId = kyThuatVienId)
//            )
//        }
//    }
//}
@Singleton
class ChuyenMonRepository @Inject constructor(
    private val chuyenMonDao: ChuyenMonDao,
    private val chuyenMonKtvDao: ChuyenMonKtvDao,
    private val chuyenMonSync: BaseFirestoreSyncService<ChuyenMon>,
    private val chuyenMonKtvSync: BaseFirestoreSyncService<ChuyenMonKtv>
) {
    suspend fun getChuyenMonIdsCuaKTV(kyThuatVienId: Int): List<Int> {
        return chuyenMonKtvDao.getChuyenMonIdsByKyThuatVienId(kyThuatVienId)
    }

//    suspend fun insert(chuyenMon: ChuyenMon) {
//        chuyenMonDao.insert(chuyenMon)
//        SyncDispatcher.dispatch(chuyenMonSync, chuyenMon, SyncDispatcher.SyncType.INSERT)
//    }

    suspend fun insert(entity: ChuyenMon) {
        val id = chuyenMonDao.insertAndReturnId(entity).toInt()
        val entityWithId = entity.copy(id = id)
        SyncDispatcher.dispatch(chuyenMonSync, entityWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(chuyenMon: ChuyenMon) {
        chuyenMonDao.update(chuyenMon)
        SyncDispatcher.dispatch(chuyenMonSync, chuyenMon, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(chuyenMon: ChuyenMon) {
        chuyenMonDao.delete(chuyenMon)
        SyncDispatcher.dispatch(chuyenMonSync, chuyenMon, SyncDispatcher.SyncType.DELETE)
    }

    fun getAllChuyenMon(): Flow<List<ChuyenMon>> {
        return chuyenMonDao.getAll()
    }

    suspend fun getChuyenMonIdsByKtv(kyThuatVienId: Int): List<Int> {
        return chuyenMonKtvDao.getChuyenMonIdsByKtvId(kyThuatVienId)
    }

    suspend fun updateChuyenMonForKtv(kyThuatVienId: Int, selectedIds: List<Int>) {
        val oldList = chuyenMonKtvDao.getByKtvId(kyThuatVienId)
        chuyenMonKtvDao.deleteAllByKtvId(kyThuatVienId)
        oldList.forEach {
            SyncDispatcher.dispatch(chuyenMonKtvSync, it, SyncDispatcher.SyncType.DELETE)
        }

        selectedIds.forEach { chuyenMonId ->
            val item = ChuyenMonKtv(chuyenMonId = chuyenMonId, kyThuatVienId = kyThuatVienId)
            val id = chuyenMonKtvDao.insertAndReturnId(item).toInt()
            val itemWithId = item.copy(id = id)
            SyncDispatcher.dispatch(chuyenMonKtvSync, itemWithId, SyncDispatcher.SyncType.INSERT)
        }
    }
}
