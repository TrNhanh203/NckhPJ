package com.example.facilitiesmanagementpj.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun ScaffoldLayout(
    title: String,
    navController: NavController,
    showTopBar: Boolean = true,  // ✅ Kiểm soát hiển thị TopAppBar
    showBottomBar: Boolean = true,  // ✅ Kiểm soát hiển thị BottomAppBar
    showDrawer: Boolean = true,  // ✅ Kiểm soát hiển thị DrawerMenu
    isHomeScreen: Boolean = false,  // ✅ Kiểm soát hiển thị nút Menu hoặc Back
    onBackClick: () -> Unit = { navController.popBackStack() },
    snackbarHost: @Composable (() -> Unit)? = null,  // ✅ Thêm hỗ trợ snackbarHost
    content: @Composable (Modifier) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            if (showDrawer) DrawerMenu(navController) { scope.launch { drawerState.close() } }
        },
        drawerState = drawerState,
        gesturesEnabled = showDrawer,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                if (showTopBar) CustomTopAppBar(
                    title = title,
                    navController = navController,
                    isHomeScreen = isHomeScreen,
                    onBackClick = onBackClick
                )
            },
            bottomBar = { if (showBottomBar) BottomNavigationBar(navController) },
            containerColor = Color.White,
            snackbarHost = snackbarHost ?: {},  // ✅ Cấu hình snackbarHost nếu có
        ) { innerPadding ->
            content(Modifier.padding(innerPadding))
        }
    }
}



//package com.example.facilitiesmanagementpj.ui.component
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.navigation.NavController
//import kotlinx.coroutines.launch
//
//@Composable
//fun ScaffoldLayout(
//    title: String,
//    navController: NavController,
//    showTopBar: Boolean = true,  // ✅ Kiểm soát hiển thị TopAppBar
//    showBottomBar: Boolean = true,  // ✅ Kiểm soát hiển thị BottomAppBar
//    showDrawer: Boolean = true,  // ✅ Kiểm soát hiển thị DrawerMenu
//    isHomeScreen: Boolean = false,  // ✅ Kiểm soát hiển thị nút Menu hoặc Back
//    onBackClick: () -> Unit = { navController.popBackStack() },
//    content: @Composable (Modifier) -> Unit
//) {
//    val drawerState = rememberDrawerState(DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//
//    ModalNavigationDrawer(
//        drawerContent = {
//            if (showDrawer) DrawerMenu(navController) { scope.launch { drawerState.close() } }
//        },
//        drawerState = drawerState,
//        gesturesEnabled = showDrawer,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Scaffold(
//            topBar = {
//                if (showTopBar) CustomTopAppBar(
//                    title = title,
//                    navController = navController,
//                    isHomeScreen = isHomeScreen,
//                    onBackClick = onBackClick
//                )
//            },
//            bottomBar = { if (showBottomBar) BottomNavigationBar(navController) },
//            containerColor = Color.White
//        ) { innerPadding ->
//            content(Modifier.padding(innerPadding))
//        }
//    }
//}
