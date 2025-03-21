package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.DanhSachKyThuatVienViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DanhSachKyThuatVienScreen(
    navController: NavController,
    viewModel: DanhSachKyThuatVienViewModel = hiltViewModel()
) {
    val ktvList = viewModel.danhSachKTV
    val chuyenMonList = viewModel.allChuyenMon
    var showSheet by remember { mutableStateOf(false) }

    ScaffoldLayout(title = "Danh sách KTV", navController = navController, showDrawer = false) { modifier ->
        Column(modifier = modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                listOf("Tất cả", "Đang làm", "Đang nghỉ").forEach { tt ->
                    FilterChip(
                        selected = viewModel.selectedTrangThai == tt || (tt == "Tất cả" && viewModel.selectedTrangThai == null),
                        onClick = {
                            viewModel.filterByTrangThai(if (tt == "Tất cả") null else tt)
                        },
                        label = { Text(tt) }
                    )
                    Spacer(Modifier.width(8.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = { showSheet = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.searchText,
                onValueChange = {
                    viewModel.searchText = it
                    viewModel.applyFilters()
                },
                label = { Text("Tìm theo tên") },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ktvList) { ktv ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("admin_view_detail_profile/${ktv.taiKhoan.id}")
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("👷 ${ktv.taiKhoan.hoTen}", style = MaterialTheme.typography.titleMedium)
                            Text("Trạng thái: ${ktv.kyThuatVien.trangThaiHienTai}")
                        }
                    }
                }
            }
        }

        if (showSheet) {
            ModalBottomSheet(onDismissRequest = { showSheet = false }) {
                Column(Modifier.padding(16.dp)) {
                    Text("Lọc theo chuyên môn", style = MaterialTheme.typography.titleMedium)

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row {
                            TextButton(onClick = {
                                viewModel.resetFilters()
                                showSheet = false
                            }) {
                                Text("Xóa lọc")
                            }
                            Button(onClick = {
                                viewModel.applyFilters()
                                showSheet = false
                            }) {
                                Text("Áp dụng")
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))




                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(chuyenMonList) { cm ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = viewModel.selectedChuyenMonIds.contains(cm.id),
                                    onCheckedChange = { viewModel.toggleChuyenMon(cm.id, it) }
                                )
                                Text(cm.tenChuyenMon)
                            }
                        }
                    }
                }
            }
        }
    }
}
