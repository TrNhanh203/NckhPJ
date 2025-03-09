package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 5. PhongViewModel
@HiltViewModel
class PhongViewModel @Inject constructor(private val repository: PhongRepository) : ViewModel() {
    val allPhong: Flow<List<Phong>> = repository.getAllPhong()

    //val allPhongTheoDay: Flow<List<Phong>> = repository.getPhongTheoDay()

    fun insert(phong: Phong) = viewModelScope.launch {
        repository.insert(phong)
    }
    fun update(phong: Phong) = viewModelScope.launch {
        repository.update(phong)
    }
    fun delete(phong: Phong) = viewModelScope.launch {
        repository.delete(phong)
    }
}