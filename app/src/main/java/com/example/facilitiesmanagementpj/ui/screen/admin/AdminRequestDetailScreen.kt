package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestDetailViewModel

@Composable
fun AdminRequestDetailScreen(navController: NavController, yeuCauId: Int) {
    val viewModel: AdminRequestDetailViewModel = hiltViewModel()
    val chiTietList by viewModel.filteredChiTietYeuCauList.collectAsState()
    val deviceTypes by viewModel.deviceTypes.collectAsState()

    LaunchedEffect(yeuCauId) {
        viewModel.loadChiTietYeuCau(yeuCauId)
        viewModel.loadDeviceTypes()
    }

    ScaffoldLayout(
        title = "Chi tiết yêu cầu",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
        isHomeScreen = false
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).then(innerPadding)) {
            Text("Chi tiết yêu cầu", style = MaterialTheme.typography.headlineSmall)

            // Filters Section
            DropdownMenuFilter(
                label = "Loại thiết bị",
                items = deviceTypes.map { it.tenLoai },
                selected = viewModel.selectedDeviceType.collectAsState().value,
                onSelectedChange = { viewModel.setDeviceTypeFilter(it) }
            )

            DropdownMenuFilter(
                label = "Loại yêu cầu",
                items = viewModel.requestTypes,
                selected = viewModel.selectedRequestType.collectAsState().value,
                onSelectedChange = { viewModel.setRequestTypeFilter(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Request Details List
            LazyColumn {
                items(chiTietList) { chiTiet ->
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${chiTiet.id}")
                            Text("Yêu cầu ID: ${chiTiet.yeuCauId}")
                            Text("Thiết bị ID: ${chiTiet.thietBiId}")
                            Text("Loại yêu cầu: ${chiTiet.loaiYeuCau}")
                            Text("Mô tả: ${chiTiet.moTa}")
                            Text("Loại thiết bị ID: ${chiTiet.loaiThietBiId}")
                            Text("Tên loại thiết bị: ${chiTiet.tenLoaiThietBi}")
                            Text("Tên thiết bị: ${chiTiet.tenThietBi}")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Approval Button
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Duyệt yêu cầu")
            }
        }
    }
}