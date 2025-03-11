package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

// 20. TangViewModel
@HiltViewModel
class TangViewModel @Inject constructor(private val repository: TangRepository) : ViewModel() {
    val allTang: Flow<List<Tang>> = repository.getAllTang()
    val allTangWithDay: Flow<List<TangWithDay>> = repository.getTangWithDay

    fun insert(tang: Tang) = viewModelScope.launch {
        repository.insert(tang)
    }
    fun update(tang: Tang) = viewModelScope.launch {
        repository.update(tang)
    }
    fun delete(tang: Tang) = viewModelScope.launch {
        repository.delete(tang)
    }

    fun getTangByDayId(dayId: Int?): Flow<List<Tang>> {
        return if (dayId != null) {
            repository.getTangByDayId(dayId)
        } else {
            flowOf(emptyList()) // Trả về danh sách rỗng nếu chưa chọn dãy
        }
    }


}
