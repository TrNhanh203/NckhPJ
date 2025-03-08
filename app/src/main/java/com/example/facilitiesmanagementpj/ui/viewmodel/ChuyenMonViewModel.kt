package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 9. ChuyenMonViewModel
@HiltViewModel
class ChuyenMonViewModel @Inject constructor(private val repository: ChuyenMonRepository) : ViewModel() {
    val allChuyenMon: Flow<List<ChuyenMon>> = repository.getAllChuyenMon()

    fun insert(chuyenMon: ChuyenMon) = viewModelScope.launch {
        repository.insert(chuyenMon)
    }
    fun update(chuyenMon: ChuyenMon) = viewModelScope.launch {
        repository.update(chuyenMon)
    }
    fun delete(chuyenMon: ChuyenMon) = viewModelScope.launch {
        repository.delete(chuyenMon)
    }
}