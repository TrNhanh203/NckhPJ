// Extracted UI logic for bottom sheet to prepare a new PhanCong
package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facilitiesmanagementpj.data.utils.LoaiYeuCau

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaoPhanCongBottomSheet(
    loaiPhanCong: String,
    onLoaiPhanCongChange: (String) -> Unit,
    ghiChu: String,
    onGhiChuChange: (String) -> Unit,
    mucDoUuTien: Float,
    onMucDoUuTienChange: (Float) -> Unit,
    onCreatePhanCong: (String, String, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        Text("Tạo phân công", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown chọn loại phân công
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = loaiPhanCong,
                onValueChange = {},
                readOnly = true,
                label = { Text("Loại phân công") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                LoaiYeuCau.ALL.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onLoaiPhanCongChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Khu vực ghi chú cố định chiều cao và có thể scroll
        Text("Ghi chú")
        Box(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            BasicTextField(
                value = ghiChu,
                onValueChange = onGhiChuChange,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                textStyle = TextStyle(fontSize = 14.sp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mức độ ưu tiên (1–5)
        Text("Mức độ ưu tiên: ${mucDoUuTien.toInt()}")
        Slider(
            value = mucDoUuTien,
            onValueChange = onMucDoUuTienChange,
            steps = 3,
            valueRange = 1f..5f
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onCreatePhanCong(loaiPhanCong, ghiChu, mucDoUuTien.toInt())
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tạo phân công")
        }
    }
}
