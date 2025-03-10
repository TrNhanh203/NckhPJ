package com.example.facilitiesmanagementpj.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.facilitiesmanagementpj.data.entity.Phong
import com.example.facilitiesmanagementpj.ui.viewmodel.DayViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.LoaiPhongViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.PhongViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.TangViewModel

@Composable
fun PhongScreen(
    viewModel: PhongViewModel = hiltViewModel(),
    dayViewModel: DayViewModel = hiltViewModel(),
    tangViewModel: TangViewModel = hiltViewModel(),
    loaiPhongViewModel: LoaiPhongViewModel = hiltViewModel()
) {
    var tenPhong by remember { mutableStateOf("") }
    var expandedDay by remember { mutableStateOf(false) }
    var expandedTang by remember { mutableStateOf(false) }
    var expandedLoaiPhong by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    var selectedDayName by remember { mutableStateOf("Chọn dãy") }
    var selectedTangName by remember { mutableStateOf("Chọn tầng") }
    var selectedLoaiPhongName by remember { mutableStateOf("Chọn loại phòng") }

    var selectedDayId by remember { mutableStateOf<Int?>(null) }
    var selectedTangId by remember { mutableStateOf<Int?>(null) }
    var selectedLoaiPhongId by remember { mutableStateOf<Int?>(null) }

    val danhSachDay by dayViewModel.allDay.collectAsState(initial = emptyList())
    val danhSachTang by tangViewModel.allTang.collectAsState(initial = emptyList())
    val danhSachLoaiPhong by loaiPhongViewModel.allLoaiPhong.collectAsState(initial = emptyList())
    val danhSachPhong by viewModel.allPhong.collectAsState(initial = emptyList())
    //val danhSachPhongTheoDay by viewModel.allPhongTheoDay.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Dropdown chọn dãy
        Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopStart)) {
            Button(onClick = { expandedDay = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selectedDayName)
            }
            DropdownMenu(expanded = expandedDay, onDismissRequest = { expandedDay = false }) {
                danhSachDay.forEach { day ->
                    DropdownMenuItem(text = { Text(day.tenDay) }, onClick = {
                        if (selectedDayId != day.id) {
                            selectedDayId = day.id
                            selectedDayName = day.tenDay
                            selectedTangId = null
                            selectedTangName = "Chọn tầng"
                        }
                        expandedDay = false
                    })
                }
            }
        }

        // Dropdown chọn tầng (lọc theo dãy đã chọn)
        Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopStart)) {
            Button(onClick = { expandedTang = true }, modifier = Modifier.fillMaxWidth(), enabled = selectedDayId != null) {
                Text(selectedTangName)
            }
            DropdownMenu(expanded = expandedTang, onDismissRequest = { expandedTang = false }) {
                danhSachTang.filter { it.dayId == selectedDayId }.forEach { tang ->
                    DropdownMenuItem(text = { Text("Tầng ${tang.tenTang}") }, onClick = {
                        selectedTangId = tang.id
                        selectedTangName = "Tầng ${tang.tenTang}"
                        expandedTang = false
                    })
                }
            }
        }

        // Ô nhập tên phòng
        TextField(value = tenPhong, onValueChange = { tenPhong = it }, label = { Text("Nhập tên phòng") }, modifier = Modifier.fillMaxWidth())

        // Dropdown chọn loại phòng
        Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopStart)) {
            Button(onClick = { expandedLoaiPhong = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selectedLoaiPhongName)
            }
            DropdownMenu(expanded = expandedLoaiPhong, onDismissRequest = { expandedLoaiPhong = false }) {
                danhSachLoaiPhong.forEach { loaiPhong ->
                    DropdownMenuItem(text = { Text(loaiPhong.tenLoaiPhong) }, onClick = {
                        selectedLoaiPhongId = loaiPhong.id
                        selectedLoaiPhongName = loaiPhong.tenLoaiPhong
                        expandedLoaiPhong = false
                    })
                }
            }
        }

        // Nút xác nhận trước khi thêm phòng
        Button(onClick = { showDialog = true }, modifier = Modifier.align(Alignment.CenterHorizontally), enabled = tenPhong.isNotBlank() && selectedTangId != null) {
            Text("Thêm Phòng")
        }

        if (selectedDayId != null) {
            LazyColumn {
                danhSachPhong.filter { it.dayId == selectedDayId }.groupBy { it.tangId }.forEach { (tangId, phongList) ->
                    val tenTang = danhSachTang.find { it.id == tangId }?.tenTang ?: "?"
                    item {
                        Text(text = "🏢 Tầng: $tenTang", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    items(phongList) { phong ->
                        val loaiPhongName = danhSachLoaiPhong.find { it.id == phong.loaiPhongId }?.tenLoaiPhong ?: "Không xác định"
                        Text(text = "    - ${phong.tenPhong} ($loaiPhongName)", fontSize = 18.sp)
                    }
                }
            }
        }
    }

    // Dialog xác nhận
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận thêm phòng") },
            text = {
                Text("Bạn có chắc muốn thêm phòng '$tenPhong' tại $selectedTangName, thuộc dãy $selectedDayName và loại phòng $selectedLoaiPhongName?")
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.insert(
                        Phong(
                            tenPhong = tenPhong,
                            tangId = selectedTangId!!,
                            dayId = selectedDayId!!,
                            donViId = null,
                            loaiPhongId = selectedLoaiPhongId
                        )
                    )
                    tenPhong = ""
                    showDialog = false
                }) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }


        )
    }


}
