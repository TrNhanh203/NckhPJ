package com.example.facilitiesmanagementpj.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminAccountScreen
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminDashboardScreen
import com.example.facilitiesmanagementpj.ui.screen.admin.AdminViewDetailProfileScreen
import com.example.facilitiesmanagementpj.ui.screen.auth.LoginScreen
import com.example.facilitiesmanagementpj.ui.screen.common.HomeScreen
import com.example.facilitiesmanagementpj.ui.screen.common.ProfileScreen
import com.example.facilitiesmanagementpj.ui.screen.common.RegisterScreen
import com.example.facilitiesmanagementpj.ui.screen.common.SplashScreen
import com.example.facilitiesmanagementpj.ui.screen.kythuatvien.KtvDashboardScreen
import com.example.facilitiesmanagementpj.ui.screen.quanlydonvi.DonViDashboardScreen


@Composable
fun NavGraph(startDestination: String = Screen.SplashScreen.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        // Màn hình chung
        composable(Screen.SplashScreen.route) { SplashScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }

        composable(Screen.Login.route) { LoginScreen(navController) }
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

        // Màn hình Quản lý đơn vị
        composable(Screen.DonViDashboard.route) { DonViDashboardScreen(navController) }


        // Màn hình Kỹ thuật viên
        composable(Screen.KtvDashboard.route) { KtvDashboardScreen(navController) }


        // Màn hình Người dùng (Sinh viên, Giảng viên)
//        composable(Screen.UserSearch.route) { UserSearchScreen(navController) }
//        composable(Screen.RoomInfo.route) { RoomInfoScreen(navController) }
    }
}
