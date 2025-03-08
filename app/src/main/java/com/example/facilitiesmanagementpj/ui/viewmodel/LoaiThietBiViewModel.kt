package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. LoaiThietBiViewModel
@HiltViewModel
class LoaiThietBiViewModel @Inject constructor(private val repository: LoaiThietBiRepository) : ViewModel() {
    val allLoaiThietBi: Flow<List<LoaiThietBi>> = repository.getAllLoaiThietBi()

    fun insert(loaiThietBi: LoaiThietBi) = viewModelScope.launch {
        repository.insert(loaiThietBi)
    }
    fun update(loaiThietBi: LoaiThietBi) = viewModelScope.launch {
        repository.update(loaiThietBi)
    }
    fun delete(loaiThietBi: LoaiThietBi) = viewModelScope.launch {
        repository.delete(loaiThietBi)
    }
}