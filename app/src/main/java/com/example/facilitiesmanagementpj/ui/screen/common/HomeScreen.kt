package com.example.facilitiesmanagementpj.ui.screen.common



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout


@Composable
fun HomeScreen(navController: NavController) {
    ScaffoldLayout(title = "Trang chủ", navController = navController) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier) // ✅ Áp dụng Modifier đã có padding từ Scaffold
        ) {
            Text("Nội dung màn hình chính", style = MaterialTheme.typography.headlineMedium)
        }
    }
}





