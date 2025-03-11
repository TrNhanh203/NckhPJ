package com.example.facilitiesmanagementpj.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.DonViViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.PhongViewModel

// xem ds theo don vi duoc chon
@Composable
fun PhongTheoDonViScreen(
    viewModel: PhongViewModel = hiltViewModel(),
    donViViewModel: DonViViewModel = hiltViewModel()
) {
    val danhSachDonVi by donViViewModel.allDonVi.collectAsState(initial = emptyList())

    var donViIdChon by remember { mutableStateOf<Int?>(null) }
    val danhSachPhong by viewModel.danhSachPhong.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Chọn đơn vị quản lý:", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown chọn đơn vị
        var expanded by remember { mutableStateOf(false) }
        Box {
            Button(onClick = { expanded = true }) {
                Text(danhSachDonVi.find { it.id == donViIdChon }?.tenDonVi ?: "Chọn đơn vị")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                danhSachDonVi.forEach { donVi ->
                    DropdownMenuItem(
                        text = { Text(donVi.tenDonVi) },
                        onClick = {
                            donViIdChon = donVi.id
                            viewModel.setDonViId(donVi.id)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Danh sách phòng
        if (donViIdChon == null) {
            Text("Vui lòng chọn đơn vị để xem danh sách phòng.")
        } else {
            LazyColumn {
                items(danhSachPhong) { phong ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Phòng: ${phong.tenPhong}", style = MaterialTheme.typography.bodyLarge)
                            Text("ID: ${phong.id}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}


//xem tat ca phong kem theo ten dvi
//@Composable
//fun PhongTheoDonViScreen(
//    viewModel: PhongViewModel = hiltViewModel(),
//    donViViewModel: DonViViewModel = hiltViewModel()
//) {
//    val danhSachPhong by viewModel.allPhongWithDetails.collectAsState(initial = emptyList())
//    val danhSachDonVi by donViViewModel.allDonVi.collectAsState(initial = emptyList())
//
//    var selectedPhongId by remember { mutableStateOf<Int?>(null) }
//    var expanded by remember { mutableStateOf(false) }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        Text("Danh sách Phòng", style = MaterialTheme.typography.headlineMedium)
//
//        LazyColumn {
//            items(danhSachPhong) { phong ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp, horizontal = 8.dp)
//                ) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp),
//                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text(
//                                text = "🏠 ${phong.tenPhong}",
//                                fontSize = 18.sp,
//                                modifier = Modifier.weight(1f)
//                            )
//                            Text(
//                                text = "📌 ${phong.tenLoaiPhong ?: "Không xác định"}",
//                                fontSize = 16.sp,
//                                modifier = Modifier.weight(1f)
//                            )
//                            TextButton(
//                                onClick = {
//                                    selectedPhongId = phong.phongId
//                                    expanded = true
//                                },
//                                modifier = Modifier.weight(1f)
//                            ) {
//                                Text(
//                                    text = "🏢 ${phong.tenDonVi ?: "Chọn đơn vị"}",
//                                    fontSize = 16.sp,
//                                    color = MaterialTheme.colorScheme.primary
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    // Dropdown chọn đơn vị quản lý mới
//    if (expanded && selectedPhongId != null) {
//        AlertDialog(
//            onDismissRequest = { expanded = false },
//            title = { Text("Chọn Đơn Vị Mới") },
//            text = {
//                LazyColumn {
//                    items(danhSachDonVi) { donVi ->
//                        TextButton(onClick = {
//                            viewModel.capNhatDonViId(selectedPhongId!!, donVi.id)
//                            expanded = false
//                        }) {
//                            Text(donVi.tenDonVi)
//                        }
//                    }
//                }
//            },
//            confirmButton = {
//                TextButton(onClick = { expanded = false }) {
//                    Text("Hủy")
//                }
//            }
//        )
//    }
//}




