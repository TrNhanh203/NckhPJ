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
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.ui.navigation.Screen

@Composable
fun SplashScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }

    // Giả lập xử lý dữ liệu trong 3 giây
    LaunchedEffect(Unit) {
        delay(2000) // Đợi 2 giây
        isLoading = false
        navController.navigate(Screen.Home.route)
        {
            popUpTo("splash") { inclusive = true }
        }// Điều hướng đến màn hình chính
    }

    // UI Splash Screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo), // Đặt logo của bạn trong res/drawable
                    contentDescription = "App Logo",
                    modifier = Modifier.size(300.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}
