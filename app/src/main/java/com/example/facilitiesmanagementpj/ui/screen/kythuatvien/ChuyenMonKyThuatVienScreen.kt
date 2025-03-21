package com.example.facilitiesmanagementpj.ui.screen.kythuatvien

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.entity.ChuyenMon
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.ChuyenMonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChuyenMonKyThuatVienScreen(
    taiKhoanId: Int,
    navController: NavController,
    viewModel: ChuyenMonViewModel = hiltViewModel(),
    onSaveDone: () -> Unit = {}
) {
    val uiState = viewModel.uiState
    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(taiKhoanId) {
        viewModel.loadTaiKhoanVaKyThuatVien(taiKhoanId)
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    ScaffoldLayout(
        title = "Chuyên môn KTV",
        navController = navController,
        showBottomBar = true
    ) { modifier ->
        Column(modifier = modifier.padding(16.dp)) {
            Text("Họ tên: ${uiState.taiKhoan?.hoTen}")
            Text("Kinh nghiệm: ${uiState.kyThuatVien?.kinhNghiem ?: "Chưa có"} năm")
            Text("Trạng thái: ${uiState.kyThuatVien?.trangThaiHienTai}")
            Spacer(modifier = Modifier.height(16.dp))

            Text("Danh sách chuyên môn hiện có", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            val selectedChuyenMon = uiState.chuyenMonItems.filter { it.isChecked }

            if (selectedChuyenMon.isEmpty()) {
                Text("Chưa có chuyên môn nào")
            } else {
                LazyColumn {
                    items(selectedChuyenMon) {
                        Text("- ${it.chuyenMon.tenChuyenMon}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { showSheet = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Chỉnh sửa chuyên môn")
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                dragHandle = null
            ) {
                Column(Modifier.fillMaxHeight(0.85f)) {
                    TopAppBar(
                        title = { Text("Chỉnh sửa chuyên môn") },
                        actions = {
                            TextButton(onClick = {
                                viewModel.save {
                                    showSheet = false
                                    onSaveDone()
                                }
                            }) {
                                Text("Lưu")
                            }
                        }
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(uiState.chuyenMonItems) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = item.isChecked,
                                    onCheckedChange = {
                                        viewModel.toggleChecked(item.chuyenMon.id, it)
                                    }
                                )
                                Text(item.chuyenMon.tenChuyenMon)
                            }
                        }
                    }
                }
            }
        }
    }
}
