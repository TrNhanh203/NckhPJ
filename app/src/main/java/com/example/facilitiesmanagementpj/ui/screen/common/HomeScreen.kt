package com.example.facilitiesmanagementpj.ui.screen.common

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout

@Composable
fun HomeScreen(navController: NavController) {
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Xác nhận thoát") },
            text = { Text("Bạn có chắc chắn muốn thoát không?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    // Close the app
                    navController.popBackStack(navController.graph.startDestinationId, true)
                }) {
                    Text("Thoát")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    ScaffoldLayout(title = "Trang chủ", navController = navController, isHomeScreen = true) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            Text("Nội dung màn hình chính", style = MaterialTheme.typography.headlineMedium)
        }
    }
}