package com.example.facilitiesmanagementpj.ui.screen.common

import android.app.Activity
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
import androidx.navigation.compose.rememberNavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import com.example.facilitiesmanagementpj.ui.screen.admin.BienBanNghiemThuScreen


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
                    (navController.context as Activity).finish()
                    //navController.popBackStack(navController.graph.startDestinationId, true)
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
//    ScaffoldLayout(
//        title = "Xem thử phân công",
//        navController = rememberNavController(), // hoặc truyền navController nếu cần
//        showBottomBar = false
//    ) { modifier ->
//        DemoDangLamViecScreen()
//
//    }
}


