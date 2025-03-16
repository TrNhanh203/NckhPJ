package com.example.facilitiesmanagementpj.ui.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object Home : Screen("home")
    object Register : Screen("register")

    // Auth Screens
    object Login : Screen("login")
    object ForgotPassword : Screen("forgot_password")
    object ChangePassword : Screen("change_password")
    object DebugLogin : Screen("debug_login")

    // Admin Screens
    object AdminDashboard : Screen("admin_dashboard")
    object AdminAccount : Screen("admin_account")
    object AdminViewDetailProfile : Screen("admin_view_detail_profile/{taiKhoanId}") {
        fun createRoute(taiKhoanId: Int) = "admin_view_detail_profile/$taiKhoanId"
    }

    // Quản lý đơn vị Screens
    object DonViDashboard : Screen("donvi_dashboard")
    object QLDVPhong : Screen("quanlydonvi_phong")
    object QLDVThietBi : Screen("quanlydonvi_thietbi/{phongId}") {
        fun createRoute(phongId: Int) = "quanlydonvi_thietbi/$phongId"
    }
    object QLDVThietBiTheoDV : Screen("quanlydonvi_thietbi_theodv")

    object ThemYeuCauMoi : Screen("them_yeu_cau_moi/{yeuCauId}?") {
        fun createRoute(yeuCauId: Int?) = yeuCauId?.let { "them_yeu_cau_moi/$it" } ?: "them_yeu_cau_moi/"
    }

    object QLDVDanhSachYeuCau : Screen("danh_sach_yeu_cau")

    // Màn hình chi tiết thiết bị với chế độ xem và chỉnh sửa
    object ThietBiDetail : Screen("thiet_bi_detail/{thietBiId}/{isEditMode}/{yeuCauId}?") {
        fun createRoute(thietBiId: Int, isEditMode: Boolean = false, yeuCauId: Int? = null): String {
            return if (isEditMode) {
                "thiet_bi_detail/$thietBiId/true/${yeuCauId ?: ""}"
            } else {
                "thiet_bi_detail/$thietBiId/false"
            }
        }
    }

    // Kỹ thuật viên Screens
    object KtvDashboard : Screen("ktv_dashboard")

    // User Screens (Sinh viên, Giảng viên)
    object Profile : Screen("profile")
}




//sealed class Screen(val route: String) {
//    object SplashScreen : Screen("splash_screen")
//    object Home : Screen("home")
//    object Register : Screen("register")
//
//    // Auth Screens
//    object Login : Screen("login")
//    object ForgotPassword : Screen("forgot_password")
//    object ChangePassword : Screen("change_password")
//    object DebugLogin : Screen("debug_login")
//
//    // Admin Screens
//    object AdminDashboard : Screen("admin_dashboard")
//    object AdminAccount : Screen("admin_account")
//    object AdminViewDetailProfile : Screen("admin_view_detail_profile/{taiKhoanId}"){
//        fun createRoute(taiKhoanId: Int) = "admin_view_detail_profile/$taiKhoanId"
//    }
////    object PhongManagement : Screen("phong_management")
////    object DeviceManagement : Screen("device_management")
////    object ReportApproval : Screen("report_approval")
////    object TaskAssignment : Screen("task_assignment")
////    object Statistics : Screen("statistics")
//
//    // Quản lý đơn vị Screens
//    object DonViDashboard : Screen("donvi_dashboard")
//    object QLDVPhong : Screen("quanlydonvi_phong")
//    object QLDVThietBi : Screen("quanlydonvi_thietbi/{phongId}"){
//        fun createRoute(phongId: Int) = "quanlydonvi_thietbi/$phongId"
//    }
//    object QLDVThietBiTheoDV : Screen("quanlydonvi_thietbi_theodv")
//    object ThemYeuCauMoi : Screen("them_yeu_cau_moi/{yeuCauId}") {
//        fun createRoute(yeuCauId: Int?) = if (yeuCauId != null) "them_yeu_cau_moi/$yeuCauId" else "them_yeu_cau_moi/"
//    }
//
//
//    object QLDVDanhSachYeuCau : Screen("danh_sach_yeu_cau")
////    object PhongList : Screen("phong_list")
////    object DeviceList : Screen("device_list")
////    object Request : Screen("request")
////    object RequestStatus : Screen("request_status")
//
//    // Kỹ thuật viên Screens
//    object KtvDashboard : Screen("ktv_dashboard")
////    object TaskList : Screen("task_list")
////    object TaskDetail : Screen("task_detail")
////    object WorkHistory : Screen("work_history")
//
//    // User Screens (Sinh viên, Giảng viên)
//    object Profile : Screen("profile")
////    object UserSearch : Screen("user_search")
////    object RoomInfo : Screen("room_info")
//}
