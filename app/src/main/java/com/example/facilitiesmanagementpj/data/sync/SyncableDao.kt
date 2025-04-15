package com.example.facilitiesmanagementpj.data.sync

interface SyncableDao<T> {
    suspend fun insertAll(items: List<T>)
}
