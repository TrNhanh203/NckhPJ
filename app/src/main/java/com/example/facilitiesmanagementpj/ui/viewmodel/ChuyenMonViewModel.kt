package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.compose.runtime.setValue
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.facilitiesmanagementpj.data.entity.ChuyenMon
import com.example.facilitiesmanagementpj.data.repository.ChuyenMonRepository
import kotlinx.coroutines.flow.first


// 9. ChuyenMonViewModel
@HiltViewModel
class ChuyenMonViewModel @Inject constructor(
    private val taiKhoanRepo: TaiKhoanRepository,
    private val kyThuatVienRepo: KyThuatVienRepository,
    private val chuyenMonRepo: ChuyenMonRepository
) : ViewModel() {

    var uiState by mutableStateOf(KyThuatVienChuyenMonUiState())
        private set

    private var kyThuatVienId: Int = -1

    fun loadTaiKhoanVaKyThuatVien(taiKhoanId: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val taiKhoan = taiKhoanRepo.getTaiKhoanById(taiKhoanId).first()
            val kyThuatVien = kyThuatVienRepo.getByTaiKhoanId(taiKhoanId).first()


            if (kyThuatVien != null) {
                kyThuatVienId = kyThuatVien.id
            }

            val all = chuyenMonRepo.getAllChuyenMon()
            val selected = if (kyThuatVienId != -1) {
                chuyenMonRepo.getChuyenMonIdsByKtv(kyThuatVienId)
            } else emptyList()

            uiState = KyThuatVienChuyenMonUiState(
                taiKhoan = taiKhoan,
                kyThuatVien = kyThuatVien,
                chuyenMonItems = all.map { ChuyenMonCheckItem(it, it.id in selected) },
                isLoading = false
            )
        }
    }

    fun toggleChecked(chuyenMonId: Int, checked: Boolean) {
        uiState = uiState.copy(
            chuyenMonItems = uiState.chuyenMonItems.map {
                if (it.chuyenMon.id == chuyenMonId) it.copy(isChecked = checked) else it
            }
        )
    }

    fun save(onDone: () -> Unit) {
        viewModelScope.launch {
            val selectedIds = uiState.chuyenMonItems.filter { it.isChecked }.map { it.chuyenMon.id }
            if (kyThuatVienId != -1) {
                chuyenMonRepo.updateChuyenMonForKtv(kyThuatVienId, selectedIds)
            }
            onDone()
        }
    }
}
