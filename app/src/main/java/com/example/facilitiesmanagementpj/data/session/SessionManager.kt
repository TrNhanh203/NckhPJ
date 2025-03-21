package com.example.facilitiesmanagementpj.data.session

import com.example.facilitiesmanagementpj.data.dao.TaiKhoanWithRole
import com.example.facilitiesmanagementpj.data.utils.TrangThaiTaiKhoan


object SessionManager {
    var currentUser: TaiKhoanWithRole? = null
        private set

    fun login(user: TaiKhoanWithRole) {
        currentUser = user
    }

    fun logout(onStatusUpdate: (Int, String) -> Unit) {
        currentUser?.let { user ->
            onStatusUpdate(user.id, TrangThaiTaiKhoan.NGOAI_TUYEN,) // ✅ Cập nhật trạng thái ngoại tuyến
        }

        currentUser = null
    }
}

