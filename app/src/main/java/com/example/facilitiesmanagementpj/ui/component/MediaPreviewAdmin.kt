package com.example.facilitiesmanagementpj.ui.component

import android.net.Uri

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

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
fun MediaPreviewAdmin(uri: Uri) {
    Box(modifier = Modifier
        .size(120.dp)
        .padding(4.dp)) {
        Image(
            painter = rememberAsyncImagePainter(uri),
            contentDescription = "Preview",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun VideoPreviewAdmin(uri: Uri) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
        }
    }

    Box(modifier = Modifier.size(200.dp).padding(4.dp)) {
        AndroidView(factory = { PlayerView(it).apply { player = exoPlayer } })
    }
}
