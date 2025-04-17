// App.kt



package com.example.facilitiesmanagementpj

import android.app.Application
import android.util.Log
import com.example.facilitiesmanagementpj.data.sync.FirestoreRealtimeSyncManager
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("GLOBAL_CRASH", "Crash tại thread ${thread.name}", throwable)
        }

//        val realtimeSyncManager = FirestoreRealtimeSyncManager(
//            firestore = FirebaseFirestore.getInstance(),
//            yeuCauDao = .yeuCauDao()
//        )
//
//        realtimeSyncManager.startAllListeners()
//
//        realtimeSyncManager.stopAllListeners()

    }
}
