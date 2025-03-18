package com.example.facilitiesmanagementpj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.data.session.SessionManager.currentUser
import com.example.facilitiesmanagementpj.data.utils.TrangThaiTaiKhoan
import com.example.facilitiesmanagementpj.ui.navigation.AppNavigation
import com.example.facilitiesmanagementpj.ui.screen.AddThietBiScreen
import com.example.facilitiesmanagementpj.ui.screen.DebugScreen
import com.example.facilitiesmanagementpj.ui.screen.PhongScreen
import com.example.facilitiesmanagementpj.ui.screen.VaiTroScreen
import com.example.facilitiesmanagementpj.ui.theme.FacilitiesManagementPJTheme
import com.example.facilitiesmanagementpj.ui.viewmodel.AuthViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.PhongViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.VaiTroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: PhongViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FacilitiesManagementPJTheme {
                AppNavigation()
                //DebugScreen()
            }

//            NavHost(navController, startDestination = "splash") {
//                composable("splash") { SplashScreen(navController) }
//                composable("home") { HomeScreen(navController) }
//            }
            //TangScreen()
//            PhongScreen()
            //AddThietBiScreen()
            //PhongTheoDonViScreen()
            //MyApp()
        }

    }

    override fun onStop() {
        super.onStop()
        updateSessionStatus()
    }

    private fun updateSessionStatus() {
                authViewModel.logout()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val vaiTroViewModel: VaiTroViewModel = hiltViewModel() // ⚠️ Inject ViewModel bằng Hilt

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            VaiTroScreen(vaiTroViewModel)
        }
    }
}
