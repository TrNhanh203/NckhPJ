package com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.facilitiesmanagementpj.data.repository.*
import com.example.facilitiesmanagementpj.data.utils.TrangThaiChungCuaPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import com.example.facilitiesmanagementpj.data.dao.PhanCongKtvDao
import com.example.facilitiesmanagementpj.data.relation.PhanCongKtvWithFullInfo

@HiltViewModel
class KtvDanhSachCongViecViewModel @Inject constructor(
    private val phanCongKtvDao: PhanCongKtvDao
) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<PhanCongKtvWithFullInfo>>(emptyList())
    val allTasks: StateFlow<List<PhanCongKtvWithFullInfo>> = _allTasks

    fun loadTasks(ktvId: Int) {
        viewModelScope.launch {
            _allTasks.value = phanCongKtvDao.getWithFullInfo(ktvId)
        }
    }


}
