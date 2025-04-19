package com.example.facilitiesmanagementpj.data.sync

import android.content.Context
import android.util.Log
import com.example.facilitiesmanagementpj.data.dao.SyncMetadataDao
import com.example.facilitiesmanagementpj.data.dao.YeuCauDao
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirestoreRealtimeSyncManager(
    private val firestore: FirebaseFirestore,
    private val yeuCauDao: YeuCauDao,
    private val syncMetadataDao: SyncMetadataDao

    // Bạn có thể thêm các Dao khác ở đây nếu cần mở rộng
) {
    private val listeners = mutableListOf<ListenerRegistration>()

    fun startAllListeners() {
        startListeningYeuCau()
        // Có thể gọi thêm startListeningXyz() sau này
    }


    fun stopAllListeners() {
        listeners.forEach { it.remove() }
        listeners.clear()
    }

    private fun startListeningYeuCau() {
        CoroutineScope(Dispatchers.IO).launch {
            val lastSyncTime = syncMetadataDao.getLastSyncTime("yeu_cau") ?: 0L
            withContext(Dispatchers.Main) {
                startListening(lastSyncTime)
            }
        }
    }

//    private fun startListening(lastSyncTime: Long) {
//        val listener = firestore.collection("yeu_cau")
//            .whereGreaterThan("thoiGianCapNhat", lastSyncTime)
//            .addSnapshotListener { snapshots, e ->
//                if (e != null) {
//                    Log.e("RealtimeSync", "Listen failed: ${e.localizedMessage}", e)
//                    return@addSnapshotListener
//                }
//
////                if (snapshots != null) {
////                    Log.d("RealtimeSync", "Received snapshot with ${snapshots.size()} documents")
////                    for (change in snapshots.documentChanges) {
////                        val thoiGianCapNhat = change.document.getLong("thoiGianCapNhat") ?: 0L
////                        if (thoiGianCapNhat > lastSyncTime) {
////                            val yeuCau = change.document.toObject(YeuCau::class.java)
////                            Log.d("RealtimeSync", "Change detected: ${change.type} - $yeuCau")
////                            when (change.type) {
////                                DocumentChange.Type.ADDED,
////                                DocumentChange.Type.MODIFIED -> {
////                                    CoroutineScope(Dispatchers.IO).launch {
////                                        yeuCauDao.insert(yeuCau)
////                                    }
////                                }
////                                DocumentChange.Type.REMOVED -> {
////                                    CoroutineScope(Dispatchers.IO).launch {
////                                        yeuCauDao.delete(yeuCau)
////                                    }
////                                }
////                            }
////                        } else {
////                            Log.d("RealtimeSync", "Ignored document id=${change.document.id} vì thoiGianCapNhat <= lastSyncTime")
////                        }
////                    }
////                }
//                if (snapshots != null) {
//                    Log.d("RealtimeSync", "Received snapshot with ${snapshots.size()} documents")
//                    for (change in snapshots.documentChanges) {
//                        when (change.type) {
//                            DocumentChange.Type.REMOVED -> {
//                                val yeuCau = change.document.toObject(YeuCau::class.java)
//                                Log.d("RealtimeSync", "Change detected: REMOVED - $yeuCau")
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    yeuCauDao.delete(yeuCau)
//                                }
//                            }
//
//                            DocumentChange.Type.ADDED,
//                            DocumentChange.Type.MODIFIED -> {
//                                val thoiGianCapNhat = change.document.getLong("thoiGianCapNhat") ?: 0L
//                                if (thoiGianCapNhat > lastSyncTime) {
//                                    val yeuCau = change.document.toObject(YeuCau::class.java)
//                                    Log.d("RealtimeSync", "Change detected: ${change.type} - $yeuCau")
//                                    CoroutineScope(Dispatchers.IO).launch {
//                                        yeuCauDao.insert(yeuCau)
//                                    }
//                                } else {
//                                    Log.d(
//                                        "RealtimeSync",
//                                        "Ignored document id=${change.document.id} (${change.type}) vì thoiGianCapNhat <= lastSyncTime"
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//        listeners.add(listener)
//    }
private fun startListening(lastSyncTime: Long) {
    val listener = firestore.collection("yeu_cau")
        .whereGreaterThan("thoiGianCapNhat", lastSyncTime)
        .addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.e("RealtimeSync", "Listen failed: ${e.localizedMessage}", e)
                return@addSnapshotListener
            }

            if (snapshots != null) {
                Log.d("RealtimeSync", "Received snapshot with ${snapshots.size()} documents")

                for (change in snapshots.documentChanges) {
                    when (change.type) {
                        DocumentChange.Type.REMOVED -> {
                            // 🚩 Với REMOVE thì cứ xử lý luôn
                            val id = change.document.getLong("id")?.toInt()
                            if (id != null) {
                                Log.d("RealtimeSync", "Change detected: REMOVED - id=$id")
                                CoroutineScope(Dispatchers.IO).launch {
                                    yeuCauDao.deleteById(id)
                                }
                            } else {
                                Log.w("RealtimeSync", "REMOVED change but no valid id found")
                            }
                        }

                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                            val thoiGianCapNhat = change.document.getLong("thoiGianCapNhat") ?: 0L
                            if (thoiGianCapNhat > lastSyncTime) {
                                val yeuCau = change.document.toObject(YeuCau::class.java)
                                Log.d("RealtimeSync", "Change detected: ${change.type} - $yeuCau")
                                CoroutineScope(Dispatchers.IO).launch {
                                    yeuCauDao.insert(yeuCau)
                                }
                            } else {
                                Log.d("RealtimeSync", "Ignored ${change.type} id=${change.document.id} vì thoiGianCapNhat <= lastSyncTime")
                            }
                        }
                    }
                }
            }
        }
    listeners.add(listener)
}


}
