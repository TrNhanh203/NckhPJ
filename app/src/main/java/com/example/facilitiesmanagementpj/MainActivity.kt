package com.example.facilitiesmanagementpj

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.facilitiesmanagementpj.data.session.SessionManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FacilitiesManagementPJTheme {
                val color = MaterialTheme.colorScheme.primary
                Log.d("ThemeColor", "Current Primary Color = $color")

                val statusBarColor = MaterialTheme.colorScheme.primary
                val view = LocalView.current

                // ✅ Dùng màu đã lưu để set status bar
                SideEffect {
                    val window = (view.context as Activity).window
                    window.statusBarColor = statusBarColor.toArgb()
                }
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
