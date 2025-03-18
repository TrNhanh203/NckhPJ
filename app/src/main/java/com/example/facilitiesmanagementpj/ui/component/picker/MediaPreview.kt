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

@Composable
fun MediaPreviewItem(uri: Uri, onRemove: () -> Unit) {
    Box(modifier = Modifier
        .size(80.dp)
        .padding(4.dp)) {
        Image(
            painter = rememberAsyncImagePainter(uri),
            contentDescription = "Preview",
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
//        IconButton(
//            onClick = { onRemove() },
//            modifier = Modifier.align(Alignment.TopEnd).size(24.dp).background(Color.Black, shape = CircleShape)
//        ) {
//            Icon(Icons.Default.Close, contentDescription = "Xóa", tint = Color.White)
//        }
    }
}

@Composable
fun VideoPreviewItem(uri: Uri, onRemove: () -> Unit, onClick: () -> Unit) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
        }
    }

    Box(modifier = Modifier.size(160.dp).padding(4.dp).clickable { onClick() }) {
        AndroidView(factory = { PlayerView(it).apply { player = exoPlayer } })

        // Nút "X" để xóa video
        IconButton(
            onClick = { onRemove() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                .clip(CircleShape)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Xóa video", tint = Color.White)
        }
    }
}





