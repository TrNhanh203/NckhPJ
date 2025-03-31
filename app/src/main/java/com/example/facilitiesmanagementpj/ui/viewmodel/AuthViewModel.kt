package com.example.facilitiesmanagementpj.ui.viewmodel

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.facilitiesmanagementpj.data.repository.TaiKhoanRepository
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.data.session.UserSessionManager
import com.example.facilitiesmanagementpj.data.utils.TrangThaiTaiKhoan
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginResult(
    val success: Boolean,
    val errorMessage: String? = null,
    val role: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: TaiKhoanRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _loginResult =
        mutableStateOf(LoginResult(success = false, errorMessage = null, role = null))
    val loginResult: State<LoginResult> = _loginResult

    @OptIn(UnstableApi::class)
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = LoginResult(false, errorMessage = null, role = null)

            val user = repository.validateLogin(username.trim(), password.trim())


            if (user != null) {
                when (user.trangThai) {
                    TrangThaiTaiKhoan.BI_KHOA -> {
                        _loginResult.value = LoginResult(false, "Tài khoản của bạn đã bị khóa.")
                    }

                    TrangThaiTaiKhoan.CHO_XAC_THUC -> {
                        _loginResult.value = LoginResult(
                            false,
                            "Tài khoản đang chờ xác thực. Vui lòng liên hệ quản trị viên."
                        )
                    }

                    else -> {
                        repository.updateTrangThai(
                            user.id,
                            TrangThaiTaiKhoan.TRUC_TUYEN
                        ) // ✅ Cập nhật trạng thái "TRỰC TUYẾN"
                        user.trangThai = TrangThaiTaiKhoan.TRUC_TUYEN
                        //SessionManager.login(user)
                        UserSessionManager.saveUser(context, user) // ✅ Lưu bằng DataStore

                        _loginResult.value = LoginResult(true, role = user.tenVaiTro)
                    }
                }
            } else {
                _loginResult.value = LoginResult(false, "Tài khoản hoặc mật khẩu không đúng.")
            }
        }
    }




}

