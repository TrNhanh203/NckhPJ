package com.example.facilitiesmanagementpj.data.syncRepository

import com.example.facilitiesmanagementpj.data.dao.YeuCauDao
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YeuCauSyncRepository(
    private val yeuCauDao: YeuCauDao,
    private val yeuCauSyncService: BaseFirestoreSyncService<YeuCau>
) {
    suspend fun insert(yeuCau: YeuCau) {
        withContext(Dispatchers.IO) {
            yeuCauDao.insert(yeuCau)
            yeuCauSyncService.pushToCloud(yeuCau)
        }
    }

    suspend fun update(yeuCau: YeuCau) {
        withContext(Dispatchers.IO) {
            yeuCauDao.update(yeuCau)
            yeuCauSyncService.pushToCloud(yeuCau)
        }
    }

    suspend fun delete(yeuCau: YeuCau) {
        withContext(Dispatchers.IO) {
            yeuCauDao.delete(yeuCau)
            yeuCauSyncService.deleteFromCloud(yeuCau.id.toString())
        }
    }
}
