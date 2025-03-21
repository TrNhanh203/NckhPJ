package com.example.facilitiesmanagementpj.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.entity.ThietBi
import com.example.facilitiesmanagementpj.ui.viewmodel.*
import java.util.*

@Composable
fun AddThietBiScreen(
    thietBiViewModel: ThietBiViewModel = hiltViewModel(),
    dayViewModel: DayViewModel = hiltViewModel(),
    tangViewModel: TangViewModel = hiltViewModel(),
    phongViewModel: PhongViewModel = hiltViewModel(),
    loaiThietBiViewModel: LoaiThietBiViewModel = hiltViewModel()
) {
    var tenThietBi by remember { mutableStateOf("") }
    var moTa by remember { mutableStateOf("") }
    var selectedDayId by remember { mutableStateOf<Int?>(null) }
    var selectedTangId by remember { mutableStateOf<Int?>(null) }
    var selectedPhongId by remember { mutableStateOf<Int?>(null) }
    var selectedLoaiThietBiId by remember { mutableStateOf<Int?>(null) }

    val danhSachDay by dayViewModel.allDay.collectAsState(initial = emptyList())
    val danhSachTang by tangViewModel.getTangByDayId(selectedDayId).collectAsState(initial = emptyList())
    val danhSachPhong by phongViewModel.getPhongByTangId(selectedTangId).collectAsState(initial = emptyList())
    val danhSachLoaiThietBi by loaiThietBiViewModel.allLoaiThietBi.collectAsState(initial = emptyList())
    val danhSachThietBi by thietBiViewModel.allThietBi.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Thêm Thiết Bị Mới", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = tenThietBi,
            onValueChange = { tenThietBi = it },
            label = { Text("Tên thiết bị") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenuField(
            label = "Chọn Loại Thiết Bị",
            options = danhSachLoaiThietBi.map { it.id to it.tenLoai },
            selectedOption = selectedLoaiThietBiId,
            onOptionSelected = { selectedLoaiThietBiId = it }
        )

        DropdownMenuField(
            label = "Chọn Dãy",
            options = danhSachDay.map { it.id to it.tenDay },
            selectedOption = selectedDayId,
            onOptionSelected = {
                selectedDayId = it
                selectedTangId = null
                selectedPhongId = null
            }
        )

        DropdownMenuField(
            label = "Chọn Tầng",
            options = danhSachTang.map { it.id to it.tenTang },
            selectedOption = selectedTangId,
            onOptionSelected = { selectedTangId = it }
        )

        DropdownMenuField(
            label = "Chọn Phòng (Tùy chọn)",
            options = danhSachPhong.map { it.id to it.tenPhong },
            selectedOption = selectedPhongId,
            onOptionSelected = { selectedPhongId = it }
        )

        OutlinedTextField(
            value = moTa,
            onValueChange = { moTa = it },
            label = { Text("Mô tả") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp, max = 120.dp)
        )

        Button(
            onClick = {
                if (tenThietBi.isNotBlank() && selectedTangId != null) {
                    val ngayCaiDat = System.currentTimeMillis()
                    val newThietBi = ThietBi(
                        tenThietBi = tenThietBi,
                        loaiThietBiId = selectedLoaiThietBiId ?: 1,
                        phongId = selectedPhongId,
                        tangId = selectedTangId,
                        trangThai = "binh_thuong",
                        ngayDaCat = ngayCaiDat,
                        ngayDungSuDung = null,
                        ghiChu = null,
                        ngayBaoDuongGanNhat = null,
                        ngayBaoDuongTiepTheo = System.currentTimeMillis() + 180 * 86400000L,
                        baoDuongDinhKy = 180,
                        loaiBaoDuong = null,
                        ghiChuBaoDuong = null,
                        moTa = moTa
                    )
                    thietBiViewModel.insert(newThietBi)
                    tenThietBi = ""
                    moTa = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Thêm Thiết Bị")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Danh sách thiết bị:", style = MaterialTheme.typography.titleLarge)
        LazyColumn {
            items(danhSachThietBi) { thietBi ->
                Text("${thietBi.tenThietBi} - ${thietBi.tangId ?: "Hành lang"} - ${thietBi.phongId ?: "Không có phòng"}")
            }
        }
    }
}

@Composable
fun DropdownMenuField(
    label: String,
    options: List<Pair<Int, String>>,
    selectedOption: Int?,
    onOptionSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("") }

    OutlinedTextField(
        value = selectedLabel,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        options.forEach { (id, name) ->
            DropdownMenuItem(
                text = { Text(name) },
                onClick = {
                    selectedLabel = name
                    onOptionSelected(id)
                    expanded = false
                }
            )
        }
    }
}
