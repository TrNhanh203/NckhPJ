package com.example.facilitiesmanagementpj.data.sync

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SyncDispatcher {

    enum class SyncType { INSERT, UPDATE, DELETE }

    fun <T : Any> dispatch(
        syncService: BaseFirestoreSyncService<T>,
        item: T,
        type: SyncType
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("SyncDebug", "Dispatching $type for ${item.toString()}")
            try {
                when (type) {
                    SyncType.INSERT, SyncType.UPDATE -> {
                        syncService.pushToCloud(item)
                    }
                    SyncType.DELETE -> {
                        val idField = item::class.members.find { it.name == "id" }
                        val idValue = idField?.call(item)?.toString()
                            ?: throw IllegalStateException("Item must have an 'id' field")
                        syncService.deleteFromCloud(idValue)
                    }
                }
                Log.d("SyncDebug", "Finished dispatching $type for ${syncService.collectionName}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


