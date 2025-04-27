package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.ThongBaoDao
import com.example.facilitiesmanagementpj.data.entity.BaiViet
import com.example.facilitiesmanagementpj.data.entity.ThongBao
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThongBaoRepository @Inject constructor(
    private val thongBaoDao: ThongBaoDao,
    private val syncService: BaseFirestoreSyncService<ThongBao>
) {

    suspend fun insertThongBao(thongBao: ThongBao) {
        thongBaoDao.insert(thongBao)
        SyncDispatcher.dispatch(syncService, thongBao, SyncDispatcher.SyncType.INSERT)
    }


    fun getAllThongBao(): Flow<List<ThongBao>> {
        return thongBaoDao.getAll()
    }

    fun getThongBaoByNguoiNhan(nguoiNhanId: Int): Flow<List<ThongBao>> {
        return thongBaoDao.getAllByNguoiNhan(nguoiNhanId)
    }

    suspend fun markThongBaoAsRead(id: String) {
        thongBaoDao.markAsRead(id)
    }

    suspend fun deleteThongBao(id: String) {
        thongBaoDao.delete(id)
    }
}
