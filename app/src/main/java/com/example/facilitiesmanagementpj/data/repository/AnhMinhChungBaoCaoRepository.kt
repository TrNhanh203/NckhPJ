package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 15. AnhMinhChungBaoCaoRepository
//@Singleton
//class AnhMinhChungBaoCaoRepository @Inject constructor(private val anhMinhChungBaoCaoDao: AnhMinhChungBaoCaoDao) {
//    fun getAllAnhMinhChungBaoCao(): Flow<List<AnhMinhChungBaoCao>> = anhMinhChungBaoCaoDao.getAll()
//
//    suspend fun getByChiTietId(chiTietId: Int): List<AnhMinhChungBaoCao> {
//        return anhMinhChungBaoCaoDao.getByChiTietId(chiTietId)
//    }
//
//
//    suspend fun getImagesByChiTietBaoCaoId(chiTietBaoCaoId: Int): List<AnhMinhChungBaoCao> = anhMinhChungBaoCaoDao.getImagesByChiTietBaoCaoId(chiTietBaoCaoId)
//    suspend fun getVideosByChiTietBaoCaoId(chiTietBaoCaoId: Int): List<AnhMinhChungBaoCao> = anhMinhChungBaoCaoDao.getVideosByChiTietBaoCaoId(chiTietBaoCaoId)
//    suspend fun insert(anhMinhChungBaoCao: AnhMinhChungBaoCao) = anhMinhChungBaoCaoDao.insert(anhMinhChungBaoCao)
//    suspend fun update(anhMinhChungBaoCao: AnhMinhChungBaoCao) = anhMinhChungBaoCaoDao.update(anhMinhChungBaoCao)
//    suspend fun delete(anhMinhChungBaoCao: AnhMinhChungBaoCao) = anhMinhChungBaoCaoDao.delete(anhMinhChungBaoCao)
//}
@Singleton
class AnhMinhChungBaoCaoRepository @Inject constructor(
    private val anhMinhChungBaoCaoDao: AnhMinhChungBaoCaoDao,
    private val syncService: BaseFirestoreSyncService<AnhMinhChungBaoCao>
) {
    fun getAllAnhMinhChungBaoCao(): Flow<List<AnhMinhChungBaoCao>> = anhMinhChungBaoCaoDao.getAll()

    suspend fun getByChiTietId(chiTietId: Int): List<AnhMinhChungBaoCao> {
        return anhMinhChungBaoCaoDao.getByChiTietId(chiTietId)
    }

    suspend fun getImagesByChiTietBaoCaoId(chiTietBaoCaoId: Int): List<AnhMinhChungBaoCao> =
        anhMinhChungBaoCaoDao.getImagesByChiTietBaoCaoId(chiTietBaoCaoId)

    suspend fun getVideosByChiTietBaoCaoId(chiTietBaoCaoId: Int): List<AnhMinhChungBaoCao> =
        anhMinhChungBaoCaoDao.getVideosByChiTietBaoCaoId(chiTietBaoCaoId)

//    suspend fun insert(anh: AnhMinhChungBaoCao) {
//        anhMinhChungBaoCaoDao.insert(anh)
//        SyncDispatcher.dispatch(syncService, anh, SyncDispatcher.SyncType.INSERT)
//    }

    suspend fun insert(anh: AnhMinhChungBaoCao) {
        val id = anhMinhChungBaoCaoDao.insertAndReturnId(anh).toInt()
        val anhWithId = anh.copy(id = id)
        SyncDispatcher.dispatch(syncService, anhWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(anh: AnhMinhChungBaoCao) {
        anhMinhChungBaoCaoDao.update(anh)
        SyncDispatcher.dispatch(syncService, anh, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(anh: AnhMinhChungBaoCao) {
        anhMinhChungBaoCaoDao.delete(anh)
        SyncDispatcher.dispatch(syncService, anh, SyncDispatcher.SyncType.DELETE)
    }
}
