package com.example.facilitiesmanagementpj.ui.navigation


sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object Home : Screen("home")

    // Auth Screens
    object Login : Screen("login")
    object ForgotPassword : Screen("forgot_password")
    object ChangePassword : Screen("change_password")

    // Admin Screens
    object AdminDashboard : Screen("admin_dashboard")
//    object UserManagement : Screen("user_management")
//    object PhongManagement : Screen("phong_management")
//    object DeviceManagement : Screen("device_management")
//    object ReportApproval : Screen("report_approval")
//    object TaskAssignment : Screen("task_assignment")
//    object Statistics : Screen("statistics")

    // Quản lý đơn vị Screens
    object DonViDashboard : Screen("donvi_dashboard")
//    object PhongList : Screen("phong_list")
//    object DeviceList : Screen("device_list")
//    object Request : Screen("request")
//    object RequestStatus : Screen("request_status")

    // Kỹ thuật viên Screens
    object KtvDashboard : Screen("ktv_dashboard")
//    object TaskList : Screen("task_list")
//    object TaskDetail : Screen("task_detail")
//    object WorkHistory : Screen("work_history")

    // User Screens (Sinh viên, Giảng viên)
//    object UserSearch : Screen("user_search")
//    object RoomInfo : Screen("room_info")
}
