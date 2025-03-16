package com.example.facilitiesmanagementpj.ui.component


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun DropdownMenuFilter(
    label: String,
    items: List<String>,
    selected: String?,
    onSelectedChange: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = selected ?: "Tất cả"

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(selectedText)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Tất cả") },
                onClick = {
                    onSelectedChange(null)
                    expanded = false
                }
            )
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelectedChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


//@Composable
//fun DropdownMenuFilter(
//    label: String,
//    items: List<String>,
//    selected: String?,
//    onSelectedChange: (String?) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    val selectedText = selected ?: "Chọn..." // ✅ Nếu selected null, hiển thị "Chọn..."
//
//    Column(modifier = Modifier.padding(8.dp)) {
//        Text(text = label, style = MaterialTheme.typography.bodySmall)
//        Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
//            Text(selectedText)
//        }
//        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//            DropdownMenuItem(
//                text = { Text("Chọn...") },
//                onClick = {
//                    onSelectedChange(null) // ✅ Chọn giá trị null
//                    expanded = false
//                }
//            )
//            items.forEach { item ->
//                DropdownMenuItem(
//                    text = { Text(item) },
//                    onClick = {
//                        onSelectedChange(item)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}

