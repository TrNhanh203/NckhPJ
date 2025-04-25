package com.example.facilitiesmanagementpj.ui.screen.admin

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.facilitiesmanagementpj.R
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestDetailViewModel
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel.BaiVietViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaiVietFormScreen(
    navController: NavController,
    idBaiViet: Int?,
    viewModel: BaiVietViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uriSaver: Saver<Uri?, String> = Saver(
        save = { it?.toString() ?: "" },
        restore = { if (it.isNotBlank()) it.toUri() else null }
    )

    // Load bài viết nếu có ID
    LaunchedEffect(idBaiViet) {
        idBaiViet?.let { viewModel.loadBaiVietById(it) }
    }

    val editingItem by viewModel.editingItem.collectAsState()
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()

    var tieuDe by rememberSaveable { mutableStateOf("") }
    var moTa by rememberSaveable { mutableStateOf("") }
    var link by rememberSaveable { mutableStateOf("") }
    var imageUrl by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable(stateSaver = uriSaver) { mutableStateOf(null as Uri?) }

    var tieuDeError by remember { mutableStateOf<String?>(null) }
    var moTaError by remember { mutableStateOf<String?>(null) }

    var isUploading by remember { mutableStateOf(false) }

    LaunchedEffect(editingItem) {
        editingItem?.let {
            tieuDe = it.tieuDe
            moTa = it.moTa
            link = it.link ?: ""
            imageUrl = it.anhDaiDien
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (idBaiViet == null) "Thêm bài viết" else "Chỉnh sửa bài viết") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                var hasError = false
                if (tieuDe.isBlank()) {
                    tieuDeError = "Không được bỏ trống tiêu đề"
                    hasError = true
                } else tieuDeError = null

                if (moTa.isBlank()) {
                    moTaError = "Không được bỏ trống mô tả"
                    hasError = true
                } else moTaError = null

                if (!hasError) {
                    isUploading = true
                    viewModel.saveBaiViet(
                        tieuDe = tieuDe,
                        moTa = moTa,
                        link = link,
                        imageUri = imageUri,
                        context = context,
                        onDone = {
                            isUploading = false
                            Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = {
                            isUploading = false
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Check, contentDescription = "Lưu")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = tieuDe,
                onValueChange = {
                    tieuDe = it
                    if (it.isNotBlank()) tieuDeError = null
                },
                label = { Text("Tiêu đề") },
                isError = tieuDeError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (tieuDeError != null) {
                Text(tieuDeError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = moTa,
                onValueChange = {
                    moTa = it
                    if (it.isNotBlank()) moTaError = null
                },
                label = { Text("Mô tả") },
                isError = moTaError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (moTaError != null) {
                Text(moTaError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = link,
                onValueChange = { link = it },
                label = { Text("Link bài viết") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Ảnh đại diện", style = MaterialTheme.typography.labelMedium)
            Box(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                when {
                    imageUri != null -> Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    imageUrl.isNotBlank() -> Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    else -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = Color.Gray)
                        Text("Chọn ảnh đại diện", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }
    }
}

