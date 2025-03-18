package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVPhongViewModel


@Composable
fun QLDVPhongScreen(navController: NavController, viewModel: QLDVPhongViewModel = hiltViewModel()) {
    val phongList by viewModel.phongList.collectAsState()
    val donViId = SessionManager.currentUser?.donViId ?: 0

    LaunchedEffect(Unit) {
        viewModel.loadPhongList(donViId)
    }
    com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout("Danh Sách Yêu Cầu", navController, showTopBar = true,showBottomBar = false,showDrawer = false)
    { modifier ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).then(modifier)) {
            Text("Danh sách phòng", style = MaterialTheme.typography.headlineMedium)

            LazyColumn {
                items(phongList) { phong ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate(Screen.QLDVThietBiTheoPhong.createRoute(phongId = phong.id)) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Tên phòng: ${phong.tenPhong}")
                            Text("Dãy: ${phong.tenDay} - Tầng: ${phong.tenTang}")
                            Text("Số lượng thiết bị: ${phong.soLuongThietBi}")
                        }
                    }
                }
            }
        }
    }



}
