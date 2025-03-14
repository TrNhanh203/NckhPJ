package com.example.facilitiesmanagementpj.data.session


import com.example.facilitiesmanagementpj.data.dao.TaiKhoanWithRole

object SessionManager {
    var currentUser: TaiKhoanWithRole? = null
        private set

    fun login(user: TaiKhoanWithRole) {
        currentUser = user // ✅ Lưu thông tin khi đăng nhập
    }

    fun logout() {
        currentUser = null // ❌ Xóa dữ liệu khi đăng xuất hoặc thoát app
    }
}
