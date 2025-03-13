package com.example.facilitiesmanagementpj.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminDashboardScreen
import com.example.facilitiesmanagementpj.ui.screen.common.HomeScreen
import com.example.facilitiesmanagementpj.ui.screen.common.SplashScreen
import com.example.facilitiesmanagementpj.ui.screen.common.LoginScreen
import com.example.facilitiesmanagementpj.ui.screen.kythuatvien.KtvDashboardScreen
import com.example.facilitiesmanagementpj.ui.screen.quanlydonvi.DonViDashboardScreen


@Composable
fun NavGraph(startDestination: String = Screen.SplashScreen.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        // Màn hình chung
        composable(Screen.SplashScreen.route) { SplashScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }

        composable(Screen.Login.route) { LoginScreen(navController) }
//        composable(Screen.ForgotPassword.route) { ForgotPasswordScreen(navController) }
//        composable(Screen.ChangePassword.route) { ChangePasswordScreen(navController) }

        // Màn hình Admin
        composable(Screen.AdminDashboard.route) { AdminDashboardScreen(navController) }


        // Màn hình Quản lý đơn vị
        composable(Screen.DonViDashboard.route) { DonViDashboardScreen(navController) }


        // Màn hình Kỹ thuật viên
        composable(Screen.KtvDashboard.route) { KtvDashboardScreen(navController) }


        // Màn hình Người dùng (Sinh viên, Giảng viên)
//        composable(Screen.UserSearch.route) { UserSearchScreen(navController) }
//        composable(Screen.RoomInfo.route) { RoomInfoScreen(navController) }
    }
}
