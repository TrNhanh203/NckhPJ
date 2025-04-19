package com.example.facilitiesmanagementpj.data.sync

import com.example.facilitiesmanagementpj.data.dao.SyncMetadataDao
import com.example.facilitiesmanagementpj.data.entity.SyncMetadata

object SyncHelper {

    suspend fun saveLastSyncTime(syncMetadataDao: SyncMetadataDao, collectionName: String, time: Long) {
        val metadata = SyncMetadata(collectionName = collectionName, lastSyncTime = time)
        syncMetadataDao.insertOrUpdate(metadata)
    }

    suspend fun getLastSyncTime(syncMetadataDao: SyncMetadataDao, collectionName: String): Long {
        return syncMetadataDao.getLastSyncTime(collectionName) ?: 0L
    }
}
