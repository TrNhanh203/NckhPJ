package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.repository.ThietBiRepository
import com.example.facilitiesmanagementpj.data.dao.ThietBiWithLoai
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class QLDVThietBiViewModel @Inject constructor(
    private val thietBiRepository: ThietBiRepository
) : ViewModel() {

    private val _thietBiList = MutableStateFlow<List<ThietBiWithLoai>>(emptyList())
    val thietBiList: StateFlow<List<ThietBiWithLoai>> = _thietBiList

    fun loadThietBiList(phongId: Int) {
        viewModelScope.launch {
            thietBiRepository.getThietBiByPhong(phongId).collect {
                _thietBiList.value = it
            }
        }
    }
}
