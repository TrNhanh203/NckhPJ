package com.example.facilitiesmanagementpj.data.sync

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BaseFirestoreSyncService<T>(
    private val collectionName: String,
    private val clazz: Class<T>,
    private val dao: SyncableDao<T>
) {
    private val firestore = Firebase.firestore

    fun syncFromCloudToRoom() {
        firestore.collection(collectionName).get()
            .addOnSuccessListener { snapshot ->
                val items = snapshot.toObjects(clazz)
                CoroutineScope(Dispatchers.IO).launch {
                    dao.insertAll(items)
                }
            }
    }

    fun pushToCloud(item: T, id: String) {
        firestore.collection(collectionName)
            .document(id)
            .set(item as Any)
    }

    fun deleteFromCloud(id: String) {
        firestore.collection(collectionName)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d("Sync", "Đã xoá $id khỏi Firestore") }
            .addOnFailureListener { Log.e("Sync", "Lỗi xoá: ${it.message}") }
    }


}
