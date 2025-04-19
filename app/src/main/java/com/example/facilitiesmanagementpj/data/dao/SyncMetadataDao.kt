package com.example.facilitiesmanagementpj.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.facilitiesmanagementpj.data.entity.SyncMetadata

@Dao
interface SyncMetadataDao {

    @Query("SELECT lastSyncTime FROM sync_metadata WHERE collectionName = :collectionName")
    suspend fun getLastSyncTime(collectionName: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(syncMetadata: SyncMetadata)

    @Query("UPDATE sync_metadata SET lastSyncTime = :lastSyncTime WHERE collectionName = :collectionName")
    suspend fun updateSyncTime(collectionName: String, lastSyncTime: Long)
}

