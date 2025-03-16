package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.repository.PhongRepository
import com.example.facilitiesmanagementpj.data.dao.PhongWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QLDVPhongViewModel @Inject constructor(
    private val phongRepository: PhongRepository
) : ViewModel() {

    private val _phongList = MutableStateFlow<List<PhongWithDetails>>(emptyList())
    val phongList: StateFlow<List<PhongWithDetails>> = _phongList

    fun loadPhongList(donViId: Int) {
        viewModelScope.launch {
            phongRepository.getPhongByDonVi(donViId).collect {
                _phongList.value = it
            }
        }
    }
}
