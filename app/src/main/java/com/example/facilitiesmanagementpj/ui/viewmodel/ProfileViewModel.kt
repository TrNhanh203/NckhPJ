package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.TaiKhoan
import com.example.facilitiesmanagementpj.data.repository.TaiKhoanRepository
import com.example.facilitiesmanagementpj.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: TaiKhoanRepository) : ViewModel() {

    private val _taiKhoan = MutableStateFlow<TaiKhoan?>(null)
    val taiKhoan: StateFlow<TaiKhoan?> = _taiKhoan

    init {
        val currentUser = SessionManager.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                val user = repository.getTaiKhoan(currentUser.tenTaiKhoan, currentUser.matKhau)
                _taiKhoan.value = user
            }
        }
    }
}
