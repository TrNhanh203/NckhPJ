package com.example.facilitiesmanagementpj.data.sync

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BaseFirestoreSyncService<T>(
    val collectionName: String,
    private val clazz: Class<T>,
    private val dao: SyncableDao<T>
) {
    private val firestore = Firebase.firestore

    suspend fun syncFromCloudToRoom(): List<T> {
        val snapshot = firestore.collection(collectionName).get().await()
        val items = snapshot.toObjects(clazz)
        dao.insertAll(items)
        return items
    }

    suspend fun syncUpdatedOnly(lastSyncTime: Long): List<T> {
        val snapshot = firestore.collection(collectionName)
            .whereGreaterThan("thoiGianCapNhat", lastSyncTime)
            .get()
            .await()

        val items = snapshot.toObjects(clazz)
        dao.insertAll(items)
        return items
    }


    suspend fun pushToCloud(item: T, id: String) {
        val data = item as Any
        val map = Gson().fromJson(Gson().toJson(data), Map::class.java).toMutableMap()
        map["thoiGianCapNhat"] = System.currentTimeMillis()

        firestore.collection(collectionName)
            .document(id)
            .set(map)
            .await()
    }

    suspend fun pushToCloud(item: T) {
        val idField = item!!::class.members.find { it.name == "id" }
        val idValue = idField?.call(item)?.toString() ?: throw IllegalStateException("Item must have an 'id' field")

        val data = item as Any
        val map = Gson().fromJson(Gson().toJson(data), Map::class.java).toMutableMap()
        map["thoiGianCapNhat"] = System.currentTimeMillis()

        firestore.collection(collectionName)
            .document(idValue)
            .set(map)
            .await()

                Log.d("SyncDebug", "Pushed successfully: $collectionName/$idValue")


    }




    suspend fun deleteFromCloud(id: String) {
        firestore.collection(collectionName)
            .document(id)
            .delete()
            .await()

            Log.d("Sync", "Đã xoá $id khỏi Firestore")

    }


}
