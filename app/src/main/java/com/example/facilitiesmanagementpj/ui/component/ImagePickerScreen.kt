package com.example.facilitiesmanagementpj.ui.component
import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.browse.MediaBrowser
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.rememberAsyncImagePainter

import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerControlView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageVideoPickerScreen() {
    val context = LocalContext.current

    // Lưu danh sách ảnh và video đã chọn
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedVideo by remember { mutableStateOf<Uri?>(null) }

    // Điều khiển mở Bottom Sheet
    var showImagePicker by remember { mutableStateOf(false) }
    var showVideoPicker by remember { mutableStateOf(false) }

    // ActivityResult để chọn nhiều ảnh (tối đa 5)
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris.isNotEmpty()) {
            val newList = selectedImages + uris.take(5 - selectedImages.size)
            selectedImages = newList.distinct().take(5) // Đảm bảo tối đa 5 ảnh
        }
    }

    // ActivityResult để chọn 1 video (tối đa 10 giây)
    val videoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            if (isVideoValid(context, it)) {
                selectedVideo = uri
            } else {
                Toast.makeText(context, "Chỉ chọn video dưới 10 giây!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Ảnh đã chọn:", style = MaterialTheme.typography.titleMedium)

        // Hàng 1: Chọn ảnh (Tối đa 5 ảnh)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                AddNewMediaButton { showImagePicker = true }
            }
            items(selectedImages.size) { index ->
                MediaPreviewItem(
                    uri = selectedImages[index],
                    onRemove = {
                        selectedImages = selectedImages.toMutableList().also { it.removeAt(index) }
                    }
                )
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Video đã chọn:", style = MaterialTheme.typography.titleMedium)

        // Hàng 2: Chọn video (Chỉ 1 video)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                if (selectedVideo == null) {
                    AddNewMediaButton { showVideoPicker = true }
                }
            }
            selectedVideo?.let { videoUri ->
                item {
                    //MediaPreviewItem(uri = videoUri)
                }
            }
        }

        // Bottom Sheet để chọn ảnh
        if (showImagePicker) {
            ModalBottomSheet(
                onDismissRequest = { showImagePicker = false },
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Chọn ảnh", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Text("Chọn ảnh từ thư viện")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { showImagePicker = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                        Text("Hủy")
                    }
                }
            }
        }

        // Bottom Sheet để chọn video
        if (showVideoPicker) {
            ModalBottomSheet(
                onDismissRequest = { showVideoPicker = false },
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Chọn video", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { videoPickerLauncher.launch("video/*") }) {
                        Text("Chọn video từ thư viện")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { showVideoPicker = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                        Text("Hủy")
                    }
                }
            }
        }
    }
}

// Nút "Thêm ảnh/video"
@Composable
fun AddNewMediaButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text("+", style = MaterialTheme.typography.headlineLarge, color = Color.White)
    }
}

// Hiển thị ảnh/video đã chọn
@Composable
fun MediaPreviewItem(uri: Uri, onRemove: () -> Unit) {
    Box(modifier = Modifier.size(80.dp).padding(4.dp)) {
        Image(
            painter = rememberAsyncImagePainter(uri),
            contentDescription = "Ảnh đã chọn",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Nút "X" để xóa ảnh
        IconButton(
            onClick = { onRemove() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Xóa ảnh",
                tint = Color.White
            )
        }
    }
}

// Kiểm tra độ dài video (không quá 10 giây)
fun isVideoValid(context: Context, uri: Uri): Boolean {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, uri)
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
        duration <= 10000 // 10 giây
    } catch (e: Exception) {
        false
    } finally {
        retriever.release()
    }
}


