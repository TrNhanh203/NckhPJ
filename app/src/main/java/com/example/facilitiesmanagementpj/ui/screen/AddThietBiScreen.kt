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
    // Trạng thái chọn lựa
    var tenThietBi by remember { mutableStateOf("") }
    var moTa by remember { mutableStateOf("") }
    var expandedDay by remember { mutableStateOf(false) }
    var expandedTang by remember { mutableStateOf(false) }
    var expandedPhong by remember { mutableStateOf(false) }

    var selectedDayId by remember { mutableStateOf<Int?>(null) }
    var selectedTangId by remember { mutableStateOf<Int?>(null) }
    var selectedPhongId by remember { mutableStateOf<Int?>(null) }
    var selectiedLoaiThietBiId by remember { mutableStateOf<Int?>(null) }

    val danhSachDay by dayViewModel.allDay.collectAsState(initial = emptyList())
    val danhSachTang by tangViewModel.getTangByDayId(selectedDayId).collectAsState(initial = emptyList())
    val danhSachPhong by phongViewModel.getPhongByTangId(selectedTangId).collectAsState(initial = emptyList())
    val danhSachLoaiThietBi by loaiThietBiViewModel.allLoaiThietBi.collectAsState(initial = emptyList())

    val danhSachThietBi by thietBiViewModel.allThietBi.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Thêm Thiết Bị Mới", style = MaterialTheme.typography.headlineMedium)

        // Ô nhập tên thiết bị
        OutlinedTextField(
            value = tenThietBi,
            onValueChange = { tenThietBi = it },
            label = { Text("Tên thiết bị") },
            modifier = Modifier.fillMaxWidth()
        )

        // Chọn Loại Thiết Bị
        DropdownMenuField(
            label = "Chọn Loại Thiết Bị",
            options = danhSachLoaiThietBi.map { it.id to it.tenLoai },
            selectedOption = selectiedLoaiThietBiId,
            onOptionSelected = { selectiedLoaiThietBiId = it;}
        )

        // Chọn Dãy
        DropdownMenuField(
            label = "Chọn Dãy",
            options = danhSachDay.map { it.id to it.tenDay },
            selectedOption = selectedDayId,
            onOptionSelected = { selectedDayId = it; selectedTangId = null; selectedPhongId = null }
        )

        // Chọn Tầng (lọc theo Dãy)
        DropdownMenuField(
            label = "Chọn Tầng",
            options = danhSachTang.map { it.id to it.tenTang },
            selectedOption = selectedTangId,
            onOptionSelected = { selectedTangId = it; selectedPhongId = null }
        )

        // Chọn Phòng (có thể để trống nếu thiết bị đặt ngoài hành lang)
        DropdownMenuField(
            label = "Chọn Phòng (Tùy chọn)",
            options = danhSachPhong.map { it.id to it.tenPhong },
            selectedOption = selectedPhongId,
            onOptionSelected = { selectedPhongId = it }
        )

        // Ô nhập mô tả có giới hạn chiều cao
        OutlinedTextField(
            value = moTa,
            onValueChange = { moTa = it },
            label = { Text("Mô tả") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp, max = 120.dp)
        )

        // Nút thêm thiết bị
        Button(
            onClick = {
                if (tenThietBi.isNotBlank() && selectedTangId != null) {
                    val ngayCaiDat = System.currentTimeMillis()
                    val newThietBi = ThietBi(
                        tenThietBi = tenThietBi,
                        loaiThietBiId = selectiedLoaiThietBiId ?: 1, // Mặc định là điều hòa
                        phongId = selectedPhongId, // Nếu null, thiết bị sẽ được gắn ở tầng (hành lang)
                        tangId = selectedTangId,
                        trangThai = "binh_thuong",
                        ngayDaCat = ngayCaiDat,
                        ngayDungSuDung = null,
                        ghiChu = null,
                        ngayBaoDuongGanNhat = null,
                        ngayBaoDuongTiepTheo = null,
                        baoDuongDinhKy = null,
                        loaiBaoDuong = null,
                        ghiChuBaoDuong = null,
                        moTa = moTa
                    )
                    thietBiViewModel.insert(newThietBi)
                    tenThietBi = ""
                    moTa = ""
                    selectedDayId = null
                    selectedTangId = null
                    selectedPhongId = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Thêm Thiết Bị")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị danh sách thiết bị
        Text("Danh sách thiết bị:", style = MaterialTheme.typography.titleLarge)
        LazyColumn {
            items(danhSachThietBi) { thietBi ->
                Text("${thietBi.tenThietBi} - ${thietBi.tangId ?: "Hành lang"} - ${thietBi.phongId ?: "Không có phòng"}")
            }
        }
    }
}

// Component Dropdown chọn Dãy, Tầng, Phòng
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
