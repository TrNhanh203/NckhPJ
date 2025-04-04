package com.example.facilitiesmanagementpj.ui.screen.kythuatvien

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    ScaffoldLayout(
        title = "Chuyên môn KTV",
        navController = navController,
        showBottomBar = true
    ) { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar tròn + trạng thái
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar to hơn + trạng thái
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                color = if (uiState.taiKhoan?.trangThai == "Trực Tuyến")
                                    Color(0xFF4CAF50) else Color(0xFFF44336),
                                shape = CircleShape
                            )
                            .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Họ tên
                Text(
                    text = uiState.taiKhoan?.hoTen ?: "Chưa rõ tên",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Email
                Text(
                    text = uiState.taiKhoan?.email ?: "Chưa có email",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Card thông tin
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Họ tên: ${uiState.taiKhoan?.hoTen}")
                    Text("Kinh nghiệm: ${uiState.kyThuatVien?.kinhNghiem ?: "Đang cập nhật"} ")
                    Text("Trạng thái: ${uiState.kyThuatVien?.trangThaiHienTai}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card chuyên môn hiện có
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Chuyên môn hiện có",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val selectedChuyenMon = uiState.chuyenMonItems.filter { it.isChecked }

                    if (selectedChuyenMon.isEmpty()) {
                        Text("Chưa có chuyên môn nào")
                    } else {
                        selectedChuyenMon.forEach {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(it.chuyenMon.tenChuyenMon)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nút mở bottom sheet
            Button(
                onClick = { showSheet = true },
                modifier = Modifier.align(Alignment.End),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Chỉnh sửa chuyên môn")
            }
        }

        // BottomSheet
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                dragHandle = null
            ) {
                Column(Modifier.fillMaxHeight(0.85f).background(MaterialTheme.colorScheme.surface)) {
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

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.outline,
                        thickness = 1.dp
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
                                    .background(Color.Transparent)
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,

                            ) {
                                Checkbox(
                                    checked = item.isChecked,
                                    onCheckedChange = {
                                        viewModel.toggleChecked(item.chuyenMon.id, it)
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(item.chuyenMon.tenChuyenMon)
                            }
                        }
                    }
                }
            }
        }
    }
}

