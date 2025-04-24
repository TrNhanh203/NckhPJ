package com.example.facilitiesmanagementpj.ui.screen.common


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.splashViewModel.SplashViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = hiltViewModel()) {
    var isLoading by remember { mutableStateOf(true) }

    // Giả lập xử lý dữ liệu trong 3 giây
//    LaunchedEffect(Unit) {
//        delay(2000) // Đợi 2 giây
//        isLoading = false
//        navController.navigate(Screen.Home.route)
//        {
//            popUpTo("splash") { inclusive = true }
//        }// Điều hướng đến màn hình chính
//    }


    //****** SYNC ALL KHI MO APP ********
    val isSyncing by remember { derivedStateOf { viewModel.isSyncing } }
    LaunchedEffect(Unit) {
        viewModel.startSync()
    }

    LaunchedEffect(isSyncing) {
        if (!isSyncing) {
            navController.navigate(Screen.Home.route) {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // UI Splash Screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(300.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}
