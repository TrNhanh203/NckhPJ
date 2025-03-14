package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.repository.TaiKhoanRepository
import com.example.facilitiesmanagementpj.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


data class LoginResult(val success: Boolean, val errorMessage: String? = null, val role: String? = null)

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: TaiKhoanRepository) : ViewModel() {

    private val _loginResult = mutableStateOf(LoginResult(success = false, errorMessage = null, role = null))
    val loginResult: State<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = LoginResult(false, errorMessage = null, role = null)

            val trimmedUsername = username.trim()
            val trimmedPassword = password.trim()

            if (trimmedUsername.isBlank() || trimmedPassword.isBlank()) {
                _loginResult.value = LoginResult(false, "Tài khoản và mật khẩu không được để trống")
                return@launch
            }

            val user = repository.validateLogin(trimmedUsername, trimmedPassword)

            if (user != null) {
                SessionManager.login(user) // ✅ Lưu tài khoản vào session
                _loginResult.value = LoginResult(true, role = user.tenVaiTro)
            } else {
                _loginResult.value = LoginResult(false, "Tài khoản hoặc mật khẩu không đúng")
            }
        }
    }
}

