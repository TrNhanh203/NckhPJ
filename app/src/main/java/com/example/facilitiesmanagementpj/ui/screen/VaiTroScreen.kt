package com.example.facilitiesmanagementpj.ui.screen


import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.room.*
import com.example.facilitiesmanagementpj.data.entity.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.VaiTroViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp


@Composable
fun VaiTroScreen(viewModel: VaiTroViewModel = hiltViewModel()) {
    var tenVaiTro by remember { mutableStateOf("") }
    val danhSachVaiTro by viewModel.allVaiTro.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = tenVaiTro,
            onValueChange = { tenVaiTro = it },
            label = { Text("Nhập tên vai trò") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (tenVaiTro.isNotBlank()) {
                    viewModel.insert(VaiTro(tenVaiTro = tenVaiTro))
                    tenVaiTro = "" // Reset input
                }
            },
//            modifier = Modifier.align(Alignment.Horizontal(End))
        ) {
            Text("Thêm Vai Trò")
        }

        LazyColumn {
            items(danhSachVaiTro) { vaiTro ->
                Text(text = "- ${vaiTro.tenVaiTro}", fontSize = 18.sp)
            }
        }
    }
}



