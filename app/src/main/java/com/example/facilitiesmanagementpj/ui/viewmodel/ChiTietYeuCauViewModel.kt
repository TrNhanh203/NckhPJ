
package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCau
import com.example.facilitiesmanagementpj.data.repository.ChiTietYeuCauRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChiTietYeuCauViewModel @Inject constructor(
    private val repository: ChiTietYeuCauRepository
) : ViewModel() {
    val allChiTietYeuCau: Flow<List<ChiTietYeuCau>> = repository.getAllChiTietYeuCau()

    fun insert(chiTietYeuCau: ChiTietYeuCau) = viewModelScope.launch {
        repository.insert(chiTietYeuCau)
    }
    fun update(chiTietYeuCau: ChiTietYeuCau) = viewModelScope.launch {
        repository.update(chiTietYeuCau)
    }
    fun delete(chiTietYeuCau: ChiTietYeuCau) = viewModelScope.launch {
        repository.delete(chiTietYeuCau)
    }
}