package com.example.facilitiesmanagementpj.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.facilitiesmanagementpj.data.entity.ThietBi
import com.example.facilitiesmanagementpj.ui.viewmodel.*

@Composable
fun AddThietBiScreen(
    viewModel: ThietBiViewModel = hiltViewModel(),
    dayViewModel: DayViewModel = hiltViewModel(),
    tangViewModel: TangViewModel = hiltViewModel(),
    phongViewModel: PhongViewModel = hiltViewModel(),
    loaiThietBiViewModel: LoaiThietBiViewModel = hiltViewModel(),
    donViViewModel: DonViViewModel = hiltViewModel()
) {
    var tenThietBi by remember { mutableStateOf("") }
    var moTa by remember { mutableStateOf("") } // New state for description
    var selectedDayId by remember { mutableStateOf<Int?>(null) }
    var selectedTangId by remember { mutableStateOf<Int?>(null) }
    var selectedPhongId by remember { mutableStateOf<Int?>(null) }
    var selectedLoaiThietBiId by remember { mutableStateOf<Int?>(null) }
    var selectedDonViId by remember { mutableStateOf<Int?>(null) }
    var trangThai by remember { mutableStateOf("Bình thường") }

    val danhSachDay by dayViewModel.allDay.collectAsState(initial = emptyList())
    val danhSachTang by tangViewModel.allTang.collectAsState(initial = emptyList())
    val danhSachPhong by phongViewModel.allPhong.collectAsState(initial = emptyList())
    val danhSachLoaiThietBi by loaiThietBiViewModel.allLoaiThietBi.collectAsState(initial = emptyList())
    val danhSachDonVi by donViViewModel.allDonVi.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = tenThietBi,
            onValueChange = { tenThietBi = it },
            label = { Text("Tên thiết bị") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = moTa,
            onValueChange = { moTa = it },
            label = { Text("Mô tả") }, // New TextField for description
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenuSelector("Chọn dãy", danhSachDay.map { it.tenDay }, onSelect = {
            selectedDayId = danhSachDay[it].id
            selectedTangId = null
            selectedPhongId = null
        })

        DropdownMenuSelector("Chọn tầng", danhSachTang.filter { it.dayId == selectedDayId }.map { "Tầng ${it.tenTang}" }, onSelect = {
            selectedTangId = danhSachTang[it].id
            selectedPhongId = null
        })

        DropdownMenuSelector("Chọn phòng", danhSachPhong.filter { it.tangId == selectedTangId }.map { it.tenPhong }, onSelect = {
            selectedPhongId = danhSachPhong[it].id
        })

        DropdownMenuSelector("Chọn loại thiết bị", danhSachLoaiThietBi.map { it.tenLoai }, onSelect = {
            selectedLoaiThietBiId = danhSachLoaiThietBi[it].id
        })

        DropdownMenuSelector("Chọn đơn vị quản lý", danhSachDonVi.map { it.tenDonVi }, onSelect = {
            selectedDonViId = danhSachDonVi[it].id
        })

        Button(onClick = {
            viewModel.insert(
                ThietBi(
                    tenThietBi = tenThietBi,
                    loaiThietBiId = selectedLoaiThietBiId!!,
                    phongId = selectedPhongId,
                    tangId = selectedTangId,
                    trangThai = trangThai,
                    ngayBaoDuongGanNhat = null,
                    ngayBaoDuongTiepTheo = null,
                    baoDuongDinhKy = null,
                    loaiBaoDuong = null,
                    ghiChuBaoDuong = null,
                    moTa = moTa // Pass the description to the entity
                )
            )
            tenThietBi = ""
            moTa = "" // Reset description input
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Thêm Thiết Bị")
        }
    }
}

@Composable
fun DropdownMenuSelector(label: String, items: List<String>, onSelect: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(label) }

    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(selectedText)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        selectedText = item
                        onSelect(index)
                        expanded = false
                    }
                )
            }
        }
    }
}
