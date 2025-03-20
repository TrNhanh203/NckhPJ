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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.example.facilitiesmanagementpj.ui.component.AddNewMediaButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerSection(
    selectedImages: List<Uri>,
    onImagesSelected: (List<Uri>) -> Unit
) {
    var showImagePicker by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris.isNotEmpty()) {
            onImagesSelected((selectedImages + uris.take(5 - selectedImages.size)).distinct().take(5))
        }
    }

    Column {
        Text("Ảnh đã chọn:")
        LazyRow {
            item { AddNewMediaButton { showImagePicker = true } }
            items(selectedImages) { uri ->
                MediaPreviewItem(uri, onRemove = { onImagesSelected(selectedImages - uri) })
            }
        }
    }

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
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ImagePickerSection() {
//    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
//    var showImagePicker by remember { mutableStateOf(false) }
//
//    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
//        if (uris.isNotEmpty()) {
//            selectedImages = (selectedImages + uris.take(5 - selectedImages.size)).distinct().take(5)
//        }
//    }
//
//    Column {
//        Text("Ảnh đã chọn:")
//        LazyRow {
//            item { AddNewMediaButton { showImagePicker = true } }
//            items(selectedImages) { uri ->
//                MediaPreviewItem(uri, onRemove = { selectedImages = selectedImages - uri })
//            }
//        }
//    }
//
//    if (showImagePicker) {
//        ModalBottomSheet(
//            onDismissRequest = { showImagePicker = false },
//            //containerColor = Color.Black.copy(alpha = 0.6f),
//            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
//        ) {
//            Column(
//                modifier = Modifier.fillMaxWidth().padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text("Chọn ảnh", style = MaterialTheme.typography.titleLarge)
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
//                    Text("Chọn ảnh từ thư viện")
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(onClick = { showImagePicker = false },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
//                    Text("Hủy")
//                }
//            }
//        }
//    }
//
//
//}
