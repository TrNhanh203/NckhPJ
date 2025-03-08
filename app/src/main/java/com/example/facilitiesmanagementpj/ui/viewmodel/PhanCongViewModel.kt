package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 11. PhanCongViewModel
@HiltViewModel
class PhanCongViewModel @Inject constructor(private val repository: PhanCongRepository) : ViewModel() {
    val allPhanCong: Flow<List<PhanCong>> = repository.getAllPhanCong()

    fun insert(phanCong: PhanCong) = viewModelScope.launch {
        repository.insert(phanCong)
    }
    fun update(phanCong: PhanCong) = viewModelScope.launch {
        repository.update(phanCong)
    }
    fun delete(phanCong: PhanCong) = viewModelScope.launch {
        repository.delete(phanCong)
    }
}