package com.example.facilitiesmanagementpj.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun ScaffoldLayout(
    title: String,
    navController: NavController,
    showBottomBar: Boolean = false,
    content: @Composable (Modifier) -> Unit // ✅ Nhận Modifier thay vì PaddingValues
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(navController) { scope.launch { drawerState.close() } }
        },
        drawerState = drawerState,
        gesturesEnabled = true,
        modifier = Modifier.fillMaxSize() // Chiếm 2/3 màn hình
    ) {
        Scaffold(
            topBar = { CustomTopAppBar(title = title) { scope.launch { drawerState.open() } } },
            bottomBar = { if (showBottomBar) BottomNavigationBar(navController) },
            containerColor = Color.White
        ) { innerPadding ->
            content(Modifier.padding(innerPadding)) // ✅ Truyền Modifier.padding(PaddingValues)
        }
    }
}

