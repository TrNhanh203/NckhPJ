
package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 10. KyThuatVienViewModel
@HiltViewModel
class KyThuatVienViewModel @Inject constructor(private val repository: KyThuatVienRepository) : ViewModel() {
    val allKyThuatVien: Flow<List<KyThuatVien>> = repository.getAllKyThuatVien()

    fun insert(kyThuatVien: KyThuatVien) = viewModelScope.launch {
        repository.insert(kyThuatVien)
    }
    fun update(kyThuatVien: KyThuatVien) = viewModelScope.launch {
        repository.update(kyThuatVien)
    }
    fun delete(kyThuatVien: KyThuatVien) = viewModelScope.launch {
        repository.delete(kyThuatVien)
    }
}