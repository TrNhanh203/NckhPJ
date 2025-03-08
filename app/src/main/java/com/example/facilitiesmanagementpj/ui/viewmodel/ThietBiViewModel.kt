package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 8. ThietBiViewModel
@HiltViewModel
class ThietBiViewModel @Inject constructor(private val repository: ThietBiRepository) : ViewModel() {
    val allThietBi: Flow<List<ThietBi>> = repository.getAllThietBi()

    fun getThietBiCanBaoDuong(ngayHienTai: Long): Flow<List<ThietBi>> {
        return repository.getThietBiCanBaoDuong(ngayHienTai)
    }

    fun insert(thietBi: ThietBi) = viewModelScope.launch {
        repository.insert(thietBi)
    }
    fun update(thietBi: ThietBi) = viewModelScope.launch {
        repository.update(thietBi)
    }
    fun delete(thietBi: ThietBi) = viewModelScope.launch {
        repository.delete(thietBi)
    }
}