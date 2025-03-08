package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 12. PhanCongKtvViewModel
@HiltViewModel
class PhanCongKtvViewModel @Inject constructor(private val repository: PhanCongKtvRepository) : ViewModel() {
    val allPhanCongKtv: Flow<List<PhanCongKtv>> = repository.getAllPhanCongKtv()

    fun insert(phanCongKtv: PhanCongKtv) = viewModelScope.launch {
        repository.insert(phanCongKtv)
    }
    fun update(phanCongKtv: PhanCongKtv) = viewModelScope.launch {
        repository.update(phanCongKtv)
    }
    fun delete(phanCongKtv: PhanCongKtv) = viewModelScope.launch {
        repository.delete(phanCongKtv)
    }
}