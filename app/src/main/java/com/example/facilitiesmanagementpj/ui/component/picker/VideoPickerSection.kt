package com.example.facilitiesmanagementpj.ui.component.picker
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
import androidx.media3.common.MediaItem
import com.example.facilitiesmanagementpj.ui.component.AddNewMediaButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPickerSection(
    selectedVideo: Uri?,
    onVideoSelected: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var showVideoPicker by remember { mutableStateOf(false) }

    val videoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            if (isVideoValid(context, it)) onVideoSelected(it)
            else Toast.makeText(context, "Chỉ chọn video dưới 10 giây!", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (selectedVideo == null) {
                Button(onClick = { showVideoPicker = true }) {
                    Text("Thêm Video")
                }
            } else {
                VideoPreviewItem(
                    uri = selectedVideo,
                    onRemove = { onVideoSelected(null) },
                    onClick = { /* Tùy chọn thêm nếu muốn mở video lớn */ }
                )
            }
        }

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

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun VideoPickerSection() {
//    val context = LocalContext.current
//    var selectedVideo by remember { mutableStateOf<Uri?>(null) }
//    var showVideoPicker by remember { mutableStateOf(false) }
//
//    // ActivityResult để chọn video
//    val videoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        uri?.let {
//            if (isVideoValid(context, it)) selectedVideo = it
//            else Toast.makeText(context, "Chỉ chọn video dưới 10 giây!", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Vùng cố định để chọn và xem video
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)  // Chiều cao cố định cho video
//                .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
//            contentAlignment = Alignment.Center
//        ) {
//            if (selectedVideo == null) {
//                // Nếu chưa chọn video, hiển thị nút thêm video
//                Button(onClick = { showVideoPicker = true }) {
//                    Text("Thêm Video")
//                }
//            } else {
//                // Nếu đã chọn video, hiển thị video preview
//                VideoPreviewItem(
//                    uri = selectedVideo!!,
//                    onRemove = { selectedVideo = null },
//                    onClick = { /* Tùy chọn thêm nếu muốn mở video lớn */ }
//                )
//            }
//        }
//
//        // Bottom Sheet để chọn video
//        if (showVideoPicker) {
//            ModalBottomSheet(
//                onDismissRequest = { showVideoPicker = false },
//                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxWidth().padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text("Chọn video", style = MaterialTheme.typography.titleLarge)
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Button(onClick = { videoPickerLauncher.launch("video/*") }) {
//                        Text("Chọn video từ thư viện")
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Button(onClick = { showVideoPicker = false },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
//                        Text("Hủy")
//                    }
//                }
//            }
//        }
//    }
//}




fun isVideoValid(context: Context, uri: Uri): Boolean {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, uri)
        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()?.let { it <= 10000 } ?: false
    } catch (e: Exception) { false }
    finally { retriever.release() }
}
