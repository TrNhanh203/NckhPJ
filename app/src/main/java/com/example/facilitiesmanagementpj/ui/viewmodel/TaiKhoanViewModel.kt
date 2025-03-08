package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 7. TaiKhoanViewModel
@HiltViewModel
class TaiKhoanViewModel @Inject constructor(private val repository: TaiKhoanRepository) : ViewModel() {
    val allTaiKhoan: Flow<List<TaiKhoan>> = repository.getAllTaiKhoan()

    fun insert(taiKhoan: TaiKhoan) = viewModelScope.launch {
        repository.insert(taiKhoan)
    }
    fun update(taiKhoan: TaiKhoan) = viewModelScope.launch {
        repository.update(taiKhoan)
    }
    fun delete(taiKhoan: TaiKhoan) = viewModelScope.launch {
        repository.delete(taiKhoan)
    }
}