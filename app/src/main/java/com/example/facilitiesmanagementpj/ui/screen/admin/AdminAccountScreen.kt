package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.dao.TaiKhoanWithRole
import com.example.facilitiesmanagementpj.data.utils.TrangThaiTaiKhoan
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminAccountViewModel
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.VaiTroViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAccountScreen(
    navController: NavController,
    viewModel: AdminAccountViewModel = hiltViewModel()
) {
    val filteredTaiKhoanList by viewModel.filteredTaiKhoanList.collectAsState()
    val vaiTroList by viewModel.vaiTroList.collectAsState()
    val trangThaiList by viewModel.trangThaiList.collectAsState()

    var currentFilterSheet by remember { mutableStateOf<AdminAccountFilterType?>(null) }

    ScaffoldLayout(
        title = "Quản lý tài khoản",
        navController = navController,
        showBottomBar = false
    ) { innerPadding ->

        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .then(innerPadding)) {

            // ✅ Bộ lọc UI
            AdminAccountFilterBar(
                selectedVaiTro = viewModel.selectedVaiTro.collectAsState().value,
                selectedTrangThai = viewModel.selectedTrangThai.collectAsState().value,
                onChipClicked = { currentFilterSheet = it },
                onReset = { viewModel.resetFilters() }
            )
            Divider(
                thickness = 1.2.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            // ✅ Danh sách tài khoản
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredTaiKhoanList) { taiKhoan ->
                    AccountItem(taiKhoan = taiKhoan, navController = navController)
                }
            }
        }

        // ✅ BottomSheet chọn filter
        currentFilterSheet?.let { type ->
            ModalBottomSheet(
                onDismissRequest = { currentFilterSheet = null },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    val items = when (type) {
                        AdminAccountFilterType.VAI_TRO -> vaiTroList.map { it.tenVaiTro }
                        AdminAccountFilterType.TRANG_THAI -> trangThaiList
                    }

                    Text(
                        text = "Chọn ${if (type == AdminAccountFilterType.VAI_TRO) "vai trò" else "trạng thái"}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    items.forEach { item ->
                        ListItem(
                            headlineContent = { Text(item) },
                            modifier = Modifier.clickable {
                                when (type) {
                                    AdminAccountFilterType.VAI_TRO -> viewModel.setVaiTroFilter(item)
                                    AdminAccountFilterType.TRANG_THAI -> viewModel.setTrangThaiFilter(item)
                                }
                                currentFilterSheet = null
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AccountItem(taiKhoan: TaiKhoanWithRole, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.AdminViewDetailProfile.createRoute(taiKhoan.id))
            }
            .border(
                width = 1.2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // 🔹 Thanh màu bên trái
            Box(
                modifier = Modifier
                    .width(12.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
            )

            // 🔸 Nội dung chính
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = taiKhoan.hoTen ?: "Chưa cập nhật",
                        style = MaterialTheme.typography.titleMedium
                    )

                    AssistChip(
                        onClick = {},
                        label = {
                            Text(taiKhoan.trangThai, style = MaterialTheme.typography.labelSmall)
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = TrangThaiTaiKhoan.getColor(taiKhoan.trangThai),
                            labelColor = Color.White
                        ),
                        modifier = Modifier.height(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 🔸 Vai trò highlight
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Vai trò: ${taiKhoan.tenVaiTro}",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}



enum class AdminAccountFilterType {
    VAI_TRO,
    TRANG_THAI
}

@Composable
fun AdminAccountFilterBar(
    selectedVaiTro: String?,
    selectedTrangThai: String?,
    onChipClicked: (AdminAccountFilterType) -> Unit,
    onReset: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 🔹 Filter Vai trò
            FilterChip(
                selected = selectedVaiTro != null,
                onClick = { onChipClicked(AdminAccountFilterType.VAI_TRO) },
                label = {
                    Text(
                        selectedVaiTro ?: "Vai trò",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            // 🔹 Filter Trạng thái
            FilterChip(
                selected = selectedTrangThai != null,
                onClick = { onChipClicked(AdminAccountFilterType.TRANG_THAI) },
                label = {
                    Text(
                        selectedTrangThai ?: "Trạng thái",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            // 🔹 Nút Xoá lọc
            FilterChip(
                selected = false,
                onClick = onReset,
                label = {
                    Text(
                        "Xoá lọc",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = false,
                    borderColor = MaterialTheme.colorScheme.primary
                ),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                    labelColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}



