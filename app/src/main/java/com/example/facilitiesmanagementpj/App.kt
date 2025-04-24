// App.kt



package com.example.facilitiesmanagementpj

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.facilitiesmanagementpj.data.database.AppDatabase
import com.example.facilitiesmanagementpj.data.sync.FirestorePushManager
import com.example.facilitiesmanagementpj.data.sync.FirestoreRealtimeSyncManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    private lateinit var realtimeSyncManager: FirestoreRealtimeSyncManager

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseAuth", "✅ Đăng nhập ẩn danh thành công!")
                } else {
                    Log.e("FirebaseAuth", "❌ Đăng nhập ẩn danh thất bại", task.exception)
                }
            }


        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("GLOBAL_CRASH", "Crash tại thread ${thread.name}", throwable)
        }

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()


        realtimeSyncManager = FirestoreRealtimeSyncManager(
            firestore = FirebaseFirestore.getInstance(),
            yeuCauDao = database.yeuCauDao(),
            syncMetadataDao = database.syncMetadataDao()
        )

        realtimeSyncManager.startAllListeners()
    }

    override fun onTerminate() {
        super.onTerminate()
        realtimeSyncManager.stopAllListeners()
    }
}

