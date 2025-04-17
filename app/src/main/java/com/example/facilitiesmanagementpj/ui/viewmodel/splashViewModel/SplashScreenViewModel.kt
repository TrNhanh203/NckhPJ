package com.example.facilitiesmanagementpj.ui.viewmodel.splashViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.sync.FirestoreSyncCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val syncCoordinator: FirestoreSyncCoordinator
) : ViewModel() {

    init {
        Log.d("SplashViewModel", "SplashViewModel INITED")
    }

    var isSyncing by mutableStateOf(true)
        private set

//    fun startSync() {
//        viewModelScope.launch {
//            syncCoordinator.syncAll()
//            isSyncing = false
//        }
//    }
    fun startSync() {
        viewModelScope.launch {
            try {
                syncCoordinator.syncSmartAll()
            } catch (e: Exception) {
                Log.e("SYNCFIRESTORE", "Lỗi khi sync trên máy ảo: ${e.localizedMessage}")
            } finally {
                isSyncing = false
            }
        }
    }

}
