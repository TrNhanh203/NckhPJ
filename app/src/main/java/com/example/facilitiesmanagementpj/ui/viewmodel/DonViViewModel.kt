package com.example.facilitiesmanagementpj.ui.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 19. DonViViewModel
@HiltViewModel
class DonViViewModel @Inject constructor(private val repository: DonViRepository) : ViewModel() {
    val allDonVi: Flow<List<DonVi>> = repository.getAllDonVi()

    fun insert(donVi: DonVi) = viewModelScope.launch {
        repository.insert(donVi)
    }
    fun update(donVi: DonVi) = viewModelScope.launch {
        repository.update(donVi)
    }
    fun delete(donVi: DonVi) = viewModelScope.launch {
        repository.delete(donVi)
    }
}