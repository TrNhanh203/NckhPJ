package com.example.facilitiesmanagementpj.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.sync.FirestorePushManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State


@HiltViewModel
class MainViewModel @Inject constructor(
    private val firestorePushManager: FirestorePushManager
) : ViewModel() {

    private val _pushStatus = mutableStateOf<String?>(null)
    val pushStatus: State<String?> = _pushStatus

    fun pushAllToCloudManually(context: Context) {
        viewModelScope.launch {
            try {
                _pushStatus.value = "Đang đẩy dữ liệu lên cloud..."
                firestorePushManager.pushAllToCloud()
                _pushStatus.value = "Đã đẩy dữ liệu thành công ✅"
            } catch (e: Exception) {
                _pushStatus.value = "Lỗi khi đồng bộ: ${e.message}"
            }
        }
    }
}
