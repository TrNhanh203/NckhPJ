package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 3. ChuyenMonKtvViewModel
@HiltViewModel
class ChuyenMonKtvViewModel @Inject constructor(private val repository: ChuyenMonKtvRepository) : ViewModel() {
    val allChuyenMonKtv: Flow<List<ChuyenMonKtv>> = repository.getAllChuyenMonKtv()

    fun insert(chuyenMonKtv: ChuyenMonKtv) = viewModelScope.launch {
        repository.insert(chuyenMonKtv)
    }
    fun update(chuyenMonKtv: ChuyenMonKtv) = viewModelScope.launch {
        repository.update(chuyenMonKtv)
    }
    fun delete(chuyenMonKtv: ChuyenMonKtv) = viewModelScope.launch {
        repository.delete(chuyenMonKtv)
    }
}
