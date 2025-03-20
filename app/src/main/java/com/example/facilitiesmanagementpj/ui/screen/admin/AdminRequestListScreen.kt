//package com.example.facilitiesmanagementpj.ui.screen.admin
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.example.facilitiesmanagementpj.data.entity.YeuCau
//import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
//import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
//import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
//import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestListViewModel
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.*
//
//@Composable
//fun AdminRequestListScreen(navController: NavController) {
//    val viewModel: AdminRequestListViewModel = hiltViewModel()
//    val requestList by viewModel.filteredYeuCauList.collectAsState()
//    val coroutineScope = rememberCoroutineScope()
//    var showDialog by remember { mutableStateOf(false) }
//    var selectedYeuCau by remember { mutableStateOf<YeuCau?>(null) }
//
//    if (showDialog && selectedYeuCau != null) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text("Xác nhận xóa") },
//            text = { Text("Bạn có chắc chắn muốn xóa yêu cầu này không?") },
//            confirmButton = {
//                TextButton(onClick = {
//
//                }) {
//                    Text("Xóa")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDialog = false }) {
//                    Text("Hủy")
//                }
//            }
//        )
//    }
//
//    ScaffoldLayout(
//        title = "Admin Request List",
//        navController = navController,
//        showTopBar = true,
//        showBottomBar = false,
//        showDrawer = false,
//        isHomeScreen = false
//    ) { innerPadding ->
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp).then(innerPadding)) {
//            Text("Danh sách yêu cầu", style = MaterialTheme.typography.headlineMedium)
//
//            DropdownMenuFilter(
//                label = "Trạng thái",
//                items = TrangThaiYeuCau.ALL,
//                selected = viewModel.selectedTrangThai.collectAsState().value,
//                onSelectedChange = { viewModel.setTrangThaiFilter(it) }
//            )
//
//            DropdownMenuFilter(
//                label = "Đơn vị",
//                items = viewModel.donViList.collectAsState().value.map { it.tenDonVi },
//                selected = viewModel.selectedDonVi.collectAsState().value?.let { viewModel.donViList.value.find { donVi -> donVi.id == it }?.tenDonVi },
//                onSelectedChange = { selectedName ->
//                    val selectedDonVi = viewModel.donViList.value.find { it.tenDonVi == selectedName }
//                    viewModel.setDonViFilter(selectedDonVi?.id)
//                }
//            )
//
//            LazyColumn {
//                items(requestList) { requestWithDonVi ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                            .pointerInput(Unit) {
//                                detectTapGestures(
//                                    onTap = {
//                                        // Navigate to request details or edit screen
//                                    }
//                                )
//                            }
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text("Mô tả: ${requestWithDonVi.yeuCau.moTa}")
//                            Text("Đơn vị: ${requestWithDonVi.tenDonVi}")
//                            Text("Trạng thái: ${requestWithDonVi.yeuCau.trangThai}")
//                            Text("Ngày yêu cầu: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(requestWithDonVi.yeuCau.ngayYeuCau))}")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestListViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminRequestListScreen(navController: NavController) {
    val viewModel: AdminRequestListViewModel = hiltViewModel()
    val requestList by viewModel.filteredYeuCauList.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var selectedYeuCau by remember { mutableStateOf<YeuCau?>(null) }



    ScaffoldLayout(
        title = "Admin Request List",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
        isHomeScreen = false
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).then(innerPadding)) {
            Text("Danh sách yêu cầu", style = MaterialTheme.typography.headlineSmall)

            // Filters Section
            DropdownMenuFilter(
                label = "Trạng thái",
                items = TrangThaiYeuCau.ALL,
                selected = viewModel.selectedTrangThai.collectAsState().value,
                onSelectedChange = { viewModel.setTrangThaiFilter(it) }
            )

            DropdownMenuFilter(
                label = "Đơn vị",
                items = viewModel.donViList.collectAsState().value.map { it.tenDonVi },
                selected = viewModel.selectedDonVi.collectAsState().value?.let {
                    viewModel.donViList.value.find { donVi -> donVi.id == it }?.tenDonVi
                },
                onSelectedChange = { selectedName ->
                    val selectedDonVi = viewModel.donViList.value.find { it.tenDonVi == selectedName }
                    viewModel.setDonViFilter(selectedDonVi?.id)
                }
            )

            // Sort Order Dropdown
            var expanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Button(onClick = { expanded = true }) {
                    Text("Sắp xếp theo ngày gửi")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Mới nhất") },
                        onClick = {
                            viewModel.setSortOrder(AdminRequestListViewModel.SortOrder.NEWEST)
                            expanded = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Cũ nhất") },
                        onClick = {
                            viewModel.setSortOrder(AdminRequestListViewModel.SortOrder.OLDEST)
                            expanded = false
                        }
                    )

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Requests List
            LazyColumn {
                items(requestList) { requestWithDonVi ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                // Navigate to request details
                                navController.navigate(Screen.AdminRequestDetail.createRoute(requestWithDonVi.yeuCau.id))
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Mô tả: ${requestWithDonVi.yeuCau.moTa}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text("Đơn vị: ${requestWithDonVi.tenDonVi}", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = "Ngày yêu cầu: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(requestWithDonVi.yeuCau.ngayYeuCau))}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "Trạng thái: ${requestWithDonVi.yeuCau.trangThai}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                        }
                    }
                }
            }
        }
    }
}
