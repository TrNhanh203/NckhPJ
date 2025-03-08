
package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.ChiTietBaoCao
import com.example.facilitiesmanagementpj.data.repository.ChiTietBaoCaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChiTietBaoCaoViewModel @Inject constructor(
    private val repository: ChiTietBaoCaoRepository
) : ViewModel() {
    val allChiTietBaoCao: Flow<List<ChiTietBaoCao>> = repository.getAllChiTietBaoCao()

    fun insert(chiTietBaoCao: ChiTietBaoCao) = viewModelScope.launch {
        repository.insert(chiTietBaoCao)
    }
    fun update(chiTietBaoCao: ChiTietBaoCao) = viewModelScope.launch {
        repository.update(chiTietBaoCao)
    }
    fun delete(chiTietBaoCao: ChiTietBaoCao) = viewModelScope.launch {
        repository.delete(chiTietBaoCao)
    }
}