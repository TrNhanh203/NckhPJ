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
    private val collectionName: String,
    private val clazz: Class<T>,
    private val dao: SyncableDao<T>
) {
    private val firestore = Firebase.firestore

    //    fun syncFromCloudToRoom() {
//        firestore.collection(collectionName).get()
//            .addOnSuccessListener { snapshot ->
//                val items = snapshot.toObjects(clazz)
//                Log.d("SYNCFIRESTORE", "Đã sync từ Firestore về Room: ${items.size} items")
//                CoroutineScope(Dispatchers.IO).launch {
//                    dao.insertAll(items)
//                }
//            }
//    }
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


    fun pushToCloud(item: T, id: String) {
        val data = item as Any
        val map = Gson().fromJson(Gson().toJson(data), Map::class.java).toMutableMap()
        map["thoiGianCapNhat"] = System.currentTimeMillis()

        firestore.collection(collectionName)
            .document(id)
            .set(map)
    }


    fun deleteFromCloud(id: String) {
        firestore.collection(collectionName)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d("Sync", "Đã xoá $id khỏi Firestore") }
            .addOnFailureListener { Log.e("Sync", "Lỗi xoá: ${it.message}") }
    }


}
