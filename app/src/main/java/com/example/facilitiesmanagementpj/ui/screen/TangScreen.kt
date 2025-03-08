package com.example.facilitiesmanagementpj.ui.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.input.KeyboardType
import com.example.facilitiesmanagementpj.data.entity.Tang
import com.example.facilitiesmanagementpj.ui.viewmodel.DayViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.TangViewModel

@Composable
fun TangScreen(
    viewModel: TangViewModel = hiltViewModel(),
    dayViewModel: DayViewModel = hiltViewModel()
) {
    var soTang by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedDayName by remember { mutableStateOf("Chọn dãy") }
    var selectedDayId by remember { mutableStateOf<Int?>(null) }

    val danhSachDay by dayViewModel.allDay.collectAsState(initial = emptyList())
    val danhSachTangWithDay by viewModel.allTangWithDay.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Dropdown chọn dãy
        Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopStart)) {
            Button(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedDayName)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                danhSachDay.forEach { day ->
                    DropdownMenuItem(
                        text = { Text(day.tenDay) },
                        onClick = {
                            selectedDayId = day.id
                            selectedDayName = day.tenDay
                            expanded = false
                        }
                    )
                }
            }
        }

        // Ô nhập số tầng
        TextField(
            value = soTang,
            onValueChange = { soTang = it },
            label = { Text("Nhập số tầng") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Nút thêm tầng vào database
        Button(
            onClick = {
                if (soTang.isNotBlank() && selectedDayId != null) {
                    val soTangInt = soTang.toIntOrNull() ?: 0
                    if (soTangInt > 0) {
                        for (i in 1..soTangInt) {
                            viewModel.insert(Tang(tenTang = i.toString(), dayId = selectedDayId!!))
                        }
                        soTang = "" // Reset input
                        selectedDayId = null
                        selectedDayName = "Chọn dãy"
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = soTang.isNotBlank() && selectedDayId != null
        ) {
            Text("Thêm Tầng")
        }

        // Hiển thị danh sách tầng theo từng dãy
        LazyColumn {
            danhSachTangWithDay.groupBy { it.tenDay }.forEach { (tenDay, tangList) ->
                item {
                    Text(text = "🏢 Dãy: $tenDay", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                items(tangList) { tang ->
                    Text(text = "    - Tầng: ${tang.tenTang}", fontSize = 18.sp)
                }
            }
        }
    }
}
