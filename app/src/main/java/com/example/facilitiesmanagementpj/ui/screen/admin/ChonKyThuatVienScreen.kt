package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.ChonKyThuatVienViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChonKyThuatVienScreen(
    navController: NavController,
    thietBiId: Int,
    yeuCauId: Int,
    viewModel: ChonKyThuatVienViewModel = hiltViewModel()
) {
    val ktvList by viewModel.danhSachKTV.collectAsState()
    val selectedIds by viewModel.selectedKtvIds.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDanhSachKTV()
    }

    ScaffoldLayout(title = "Chọn kỹ thuật viên", navController = navController, showDrawer = false) { modifier ->
        Column(modifier = modifier.padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                items(ktvList) { ktv ->
                    val isSelected = selectedIds.contains(ktv.kyThuatVien.id)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleSelection(ktv.kyThuatVien.id) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { viewModel.toggleSelection(ktv.kyThuatVien.id) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("👷 ${ktv.taiKhoan.hoTen}", style = MaterialTheme.typography.titleMedium)
                                Text("Trạng thái: ${ktv.kyThuatVien.trangThaiHienTai}")
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
//                    viewModel.taoPhanCong(thietBiId, yeuCauId)
//                    navController.popBackStack() // hoặc điều hướng tới màn xác nhận
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = selectedIds.isNotEmpty()
            ) {
                Text("Tạo phân công")
            }
        }
    }
}
