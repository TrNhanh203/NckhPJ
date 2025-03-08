package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 2. PhanCongThietBiViewModel
@HiltViewModel
class PhanCongThietBiViewModel @Inject constructor(private val repository: PhanCongThietBiRepository) : ViewModel() {
    val allPhanCongThietBi: Flow<List<PhanCongThietBi>> = repository.getAllPhanCongThietBi()

    fun insert(phanCongThietBi: PhanCongThietBi) = viewModelScope.launch {
        repository.insert(phanCongThietBi)
    }
    fun update(phanCongThietBi: PhanCongThietBi) = viewModelScope.launch {
        repository.update(phanCongThietBi)
    }
    fun delete(phanCongThietBi: PhanCongThietBi) = viewModelScope.launch {
        repository.delete(phanCongThietBi)
    }
}