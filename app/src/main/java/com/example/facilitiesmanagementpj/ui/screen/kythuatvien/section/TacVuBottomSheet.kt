package com.example.facilitiesmanagementpj.ui.screen.kythuatvien.section

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import coil.compose.rememberAsyncImagePainter

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.LoaiTacVu


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TacVuBottomSheet(
    tacVu: LoaiTacVu,
    imageUris: List<Uri>,
    videoUri: Uri?,
    onDismiss: () -> Unit,
    onChupAnh: () -> Unit,
    onXoaAnh: (Uri) -> Unit,
    onXoaVideo: () -> Unit,
    getNoteForImage: (Uri) -> String?,
    updateNoteForImage: (Uri, String) -> Unit,
    onSubmit: (soPhut: Int?) -> Unit
) {
    var soPhut by remember { mutableStateOf("10") }
    val canSubmit = imageUris.isNotEmpty()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 500.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    tacVu.tieuDe,
                    style = MaterialTheme.typography.titleMedium
                )

                if (tacVu.canNhapSoPhut) {
                    OutlinedTextField(
                        value = soPhut,
                        onValueChange = { soPhut = it.filter(Char::isDigit) },
                        label = { Text("Số phút cần gia hạn thêm:") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ảnh: ${imageUris.size}/5")
                    IconButton(onClick = onChupAnh) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Chụp ảnh")
                    }
                }

                imageUris.forEach { uri ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        OutlinedTextField(
                            value = getNoteForImage(uri) ?: "",
                            onValueChange = { updateNoteForImage(uri, it) },
                            placeholder = { Text("Ghi chú ảnh") },
                            maxLines = 2,
                            singleLine = false,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onXoaAnh(uri) }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                }

                videoUri?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color.Black.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎥", style = MaterialTheme.typography.headlineLarge)
                        IconButton(
                            onClick = onXoaVideo,
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                }
            }

            Column {
                Button(
                    onClick = {
                        onSubmit(soPhut.toIntOrNull())
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = canSubmit
                ) {
                    Text(tacVu.buttonLabel)
                }

                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Hủy")
                }
            }
        }
    }
}
