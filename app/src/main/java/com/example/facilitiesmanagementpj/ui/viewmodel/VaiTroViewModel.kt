package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 6. VaiTroViewModel
@HiltViewModel
class VaiTroViewModel @Inject constructor(private val repository: VaiTroRepository) : ViewModel() {
    val allVaiTro: Flow<List<VaiTro>> = repository.getAllVaiTro()

    fun insert(vaiTro: VaiTro) = viewModelScope.launch {
        repository.insert(vaiTro)
    }
    fun update(vaiTro: VaiTro) = viewModelScope.launch {
        repository.update(vaiTro)
    }
    fun delete(vaiTro: VaiTro) = viewModelScope.launch {
        repository.delete(vaiTro)
    }
}