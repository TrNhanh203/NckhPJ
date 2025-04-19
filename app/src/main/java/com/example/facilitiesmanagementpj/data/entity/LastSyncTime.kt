package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_metadata")
data class SyncMetadata(
    @PrimaryKey val collectionName: String, // Ví dụ: "yeu_cau", "phan_cong"
    val lastSyncTime: Long
)
