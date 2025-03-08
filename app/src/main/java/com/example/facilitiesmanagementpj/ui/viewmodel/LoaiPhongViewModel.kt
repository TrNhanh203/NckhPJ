package com.example.facilitiesmanagementpj.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 4. LoaiPhongViewModel
@HiltViewModel
class LoaiPhongViewModel @Inject constructor(private val repository: LoaiPhongRepository) : ViewModel() {
    val allLoaiPhong: Flow<List<LoaiPhong>> = repository.getAllLoaiPhong()

    fun insert(loaiPhong: LoaiPhong) = viewModelScope.launch {
        repository.insert(loaiPhong)
    }
    fun update(loaiPhong: LoaiPhong) = viewModelScope.launch {
        repository.update(loaiPhong)
    }
    fun delete(loaiPhong: LoaiPhong) = viewModelScope.launch {
        repository.delete(loaiPhong)
    }
}