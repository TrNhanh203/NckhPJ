package com.example.facilitiesmanagementpj.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.facilitiesmanagementpj.ui.screen.DebugLoginScreen
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminAccountScreen
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminDashboardScreen
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminDeviceDetailScreen
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminDeviceListScreen
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminRequestDetailScreen
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminRequestListScreen
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminViewDetailProfileScreen
import com.example.facilitiesmanagementpj.ui.screen.auth.LoginScreen
import com.example.facilitiesmanagementpj.ui.screen.common.HomeScreen
import com.example.facilitiesmanagementpj.ui.screen.common.ProfileScreen
import com.example.facilitiesmanagementpj.ui.screen.common.RegisterScreen
import com.example.facilitiesmanagementpj.ui.screen.common.SplashScreen
import com.example.facilitiesmanagementpj.ui.screen.kythuatvien.KtvDashboardScreen
import com.example.facilitiesmanagementpj.ui.screen.quanlydonvi.DanhSachYeuCauScreen
import com.example.facilitiesmanagementpj.ui.screen.quanlydonvi.DonViDashboardScreen
import com.example.facilitiesmanagementpj.ui.screen.quanlydonvi.QLDVPhongScreen
import com.example.facilitiesmanagementpj.ui.screen.quanlydonvi.QLDVThietBiTheoPhongScreen
import com.example.facilitiesmanagementpj.ui.screen.quanlydonvi.QLDVThietBiTheoDVScreen
import com.example.facilitiesmanagementpj.ui.screen.quanlydonvi.ThemYeuCauMoiScreen
import com.example.facilitiesmanagementpj.ui.screen.quanlydonvi.ThietBiDetailScreen


@Composable
fun NavGraph(startDestination: String = Screen.SplashScreen.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        // Màn hình chung
        composable(Screen.SplashScreen.route) { SplashScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }

        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.DebugLogin.route) { DebugLoginScreen(navController) }

        composable(Screen.Profile.route) { ProfileScreen(navController) }
//        composable(Screen.ForgotPassword.route) { ForgotPasswordScreen(navController) }
//        composable(Screen.ChangePassword.route) { ChangePasswordScreen(navController) }

        // Màn hình Admin
        composable(Screen.AdminDashboard.route) { AdminDashboardScreen(navController) }
        composable(Screen.AdminAccount.route) { AdminAccountScreen(navController) }
        composable(
            route = Screen.AdminViewDetailProfile.route,
            arguments = listOf(navArgument("taiKhoanId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taiKhoanId = backStackEntry.arguments?.getInt("taiKhoanId") ?: 0
            AdminViewDetailProfileScreen(navController, taiKhoanId)
        }
        composable(Screen.AdminRequestList.route) { AdminRequestListScreen(navController) }

        composable(
            route = Screen.AdminRequestDetail.route,
            arguments = listOf(navArgument("yeuCauId") { type = NavType.IntType })
        ) { backStackEntry ->
            val yeuCauId = backStackEntry.arguments?.getInt("yeuCauId") ?: 0
            AdminRequestDetailScreen(navController, yeuCauId)
        }
        composable(
            route = Screen.AdminDeviceDetail.route,
            arguments = listOf(
                navArgument("thietBiId") { type = NavType.IntType },
                navArgument("yeuCauId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val thietBiId = backStackEntry.arguments?.getInt("thietBiId") ?: 0
            val yeuCauId = backStackEntry.arguments?.getInt("yeuCauId") ?: 0
            AdminDeviceDetailScreen(navController, thietBiId, yeuCauId)
        }
        composable(
            route = Screen.AdminDeviceList.route
        ) {
            AdminDeviceListScreen(navController)
        }


        // Màn hình Quản lý đơn vị
        composable(Screen.DonViDashboard.route) { DonViDashboardScreen(navController) }
        composable(Screen.QLDVPhong.route) { QLDVPhongScreen(navController) }
        composable("quanlydonvi_thietbi/{phongId}") { backStackEntry ->
            val phongId = backStackEntry.arguments?.getString("phongId")?.toInt() ?: 0
            QLDVThietBiTheoPhongScreen(navController, phongId)
        }


        composable("danh_sach_yeu_cau") { DanhSachYeuCauScreen(navController) }
        composable("them_yeu_cau_moi/{yeuCauId}?", arguments = listOf(navArgument("yeuCauId") { nullable = true; defaultValue = null })) { backStackEntry ->
            val yeuCauId = backStackEntry.arguments?.getString("yeuCauId")?.toIntOrNull()
            ThemYeuCauMoiScreen(navController, yeuCauId)
        }

        composable(Screen.QLDVThietBiTheoDV.route) {
            QLDVThietBiTheoDVScreen(navController, isSelectMode = false, yeuCauId = null)
        }
        composable("chon_thiet_bi/{yeuCauId}") { backStackEntry ->  //vao chung mh ds thiet bị theo dv nhung de chọn thiet bi cho yêu cầu
            val yeuCauId = backStackEntry.arguments?.getString("yeuCauId")?.toInt() ?: 0
            QLDVThietBiTheoDVScreen(navController, isSelectMode = true, yeuCauId = yeuCauId)
        }


        //QLDV chi tiet thiet bi - chi tiet yeu cau
        composable(Screen.ThietBiDetail.route) { backStackEntry ->
            val thietBiId = backStackEntry.arguments?.getString("thietBiId")?.toInt() ?: 0
            val isEditMode = backStackEntry.arguments?.getString("isEditMode").toBoolean()
            val yeuCauId = backStackEntry.arguments?.getString("yeuCauId")?.toIntOrNull()
            ThietBiDetailScreen(navController, thietBiId, isEditMode, yeuCauId)
        }






        // Màn hình Kỹ thuật viên
        composable(Screen.KtvDashboard.route) { KtvDashboardScreen(navController) }


        // Màn hình Người dùng (Sinh viên, Giảng viên)
//        composable(Screen.UserSearch.route) { UserSearchScreen(navController) }
//        composable(Screen.RoomInfo.route) { RoomInfoScreen(navController) }
    }
}
