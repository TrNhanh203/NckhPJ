package com.example.facilitiesmanagementpj.data.sync

import android.util.Log
import com.example.facilitiesmanagementpj.data.dao.YeuCauDao
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirestoreRealtimeSyncManager(
    private val firestore: FirebaseFirestore,
    private val yeuCauDao: YeuCauDao,
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
        val listener = firestore.collection("yeu_cau")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("RealtimeSync", "Listen failed: ${e.localizedMessage}", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    for (change in snapshots.documentChanges) {
                        val yeuCau = change.document.toObject(YeuCau::class.java)
                        when (change.type) {
                            DocumentChange.Type.ADDED,
                            DocumentChange.Type.MODIFIED -> {
                                CoroutineScope(Dispatchers.IO).launch {
                                    yeuCauDao.insert(yeuCau)
                                }
                            }
                            DocumentChange.Type.REMOVED -> {
                                CoroutineScope(Dispatchers.IO).launch {
                                    yeuCauDao.delete(yeuCau)
                                }
                            }
                        }
                    }
                }
            }
        listeners.add(listener)
    }
}
