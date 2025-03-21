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
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVThietBiViewModel

@Composable
fun QLDVThietBiTheoPhongScreen(navController: NavController, phongId: Int, viewModel: QLDVThietBiViewModel = hiltViewModel()) {
    val thietBiList by viewModel.thietBiList.collectAsState()

    LaunchedEffect(phongId) {
        viewModel.loadThietBiListByPhong(phongId)
    }
    com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout("Danh Sách Yêu Cầu", navController, showTopBar = true,showBottomBar = false,showDrawer = false)
    { modifier ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).then(modifier)) {
            Text("Danh sách thiết bị trong phòng", style = MaterialTheme.typography.headlineMedium)

            LazyColumn {
                items(thietBiList) { thietBi ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate(Screen.ThietBiDetail.createRoute(thietBi.id, isEditMode = false, yeuCauId = null)) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Tên thiết bị: ${thietBi.tenThietBi}")
                            Text("Loại: ${thietBi.tenLoai}")
                            Text("Trạng thái: ${thietBi.trangThai}")
                        }
                    }
                }
            }
        }
    }


}