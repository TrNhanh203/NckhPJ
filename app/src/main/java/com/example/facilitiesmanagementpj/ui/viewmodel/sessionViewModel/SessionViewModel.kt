package com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.TaiKhoanWithRole
import com.example.facilitiesmanagementpj.data.repository.TaiKhoanRepository
import com.example.facilitiesmanagementpj.data.session.UserSessionManager
import com.example.facilitiesmanagementpj.data.utils.TrangThaiTaiKhoan
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: TaiKhoanRepository,
) : ViewModel() {

    val currentUser: StateFlow<TaiKhoanWithRole?> =
        UserSessionManager.getUserFlow(context).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    fun logout() {
        viewModelScope.launch {
            val user = currentUser.value
            if (user != null) {
                // Cập nhật trạng thái trước khi xóa session
                repository.updateTrangThai(user.id, TrangThaiTaiKhoan.NGOAI_TUYEN)

                // Xoá phiên đăng nhập
                UserSessionManager.clearUser(context)
            }

        }
    }
}
