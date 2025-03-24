package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestListViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminRequestListScreen(navController: NavController) {
    val viewModel: AdminRequestListViewModel = hiltViewModel()
    val requestList by viewModel.yeuCauWithCount.collectAsState()
    val selectedTrangThai by viewModel.selectedTrangThai.collectAsState()
    val selectedDonVi by viewModel.selectedDonVi.collectAsState()
    val donViList by viewModel.donViList.collectAsState()

    ScaffoldLayout(
        title = "Danh sách yêu cầu",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
        isHomeScreen = false
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .then(innerPadding)) {

            DropdownMenuFilter(
                label = "Trạng thái",
                items = TrangThaiYeuCau.ALL,
                selected = selectedTrangThai,
                onSelectedChange = { viewModel.setTrangThaiFilter(it) }
            )

            DropdownMenuFilter(
                label = "Đơn vị",
                items = donViList.map { it.tenDonVi },
                selected = selectedDonVi?.let { id ->
                    donViList.find { it.id == id }?.tenDonVi
                },
                onSelectedChange = { selectedName ->
                    val selectedDonVi = donViList.find { it.tenDonVi == selectedName }
                    viewModel.setDonViFilter(selectedDonVi?.id)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(requestList) { request ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AdminRequestDetail.createRoute(request.yeuCau.id)
                                )
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Mô tả: ${request.yeuCau.moTa}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text("Đơn vị: ${request.tenDonVi}")
                            Text(
                                text = "Ngày yêu cầu: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(request.yeuCau.ngayYeuCau))}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "Trạng thái: ${request.yeuCau.trangThai}",
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Đã phân công: ${request.daPhanCong}/${request.tongChiTiet}",
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }
    }
}

