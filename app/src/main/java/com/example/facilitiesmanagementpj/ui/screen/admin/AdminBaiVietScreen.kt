package com.example.facilitiesmanagementpj.ui.screen.admin

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.net.toUri
import com.example.facilitiesmanagementpj.ui.navigation.Screen

import com.example.facilitiesmanagementpj.ui.screen.common.ArticleCardDemo
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestDetailViewModel.ChiTietYeuCauWithDisplayData
import com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel.BaiVietViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBaiVietScreen(
    navController: NavController,
    viewModel: BaiVietViewModel = hiltViewModel()
) {
    val list by viewModel.dsBaiViet.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }

    val filteredList = list.filter {
        it.tieuDe.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Quản lý bài viết") },
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
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Tìm bài viết theo tiêu đề...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.setEditing(null)
                navController.navigate(Screen.BaiVietForm.createRoute(null))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Thêm bài viết")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 80.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredList) { item ->
                ArticleCardAdminItem(
                    imagePainter = rememberAsyncImagePainter(item.anhDaiDien),
                    title = item.tieuDe,
                    description = item.moTa,
                    onEdit = {
                        viewModel.setEditing(item)
                        navController.navigate(Screen.BaiVietForm.createRoute(item.id))
                    },
                    onDelete = {
                        scope.launch { viewModel.delete(item) }
                    },
                    onView = {
                        val intent = Intent(Intent.ACTION_VIEW, item.link?.toUri())
                        context.startActivity(intent)
                    }
                )



            }
        }
    }
}


@Composable
fun ArticleCardAdminItem(
    imagePainter: Painter,
    title: String,
    description: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onView: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(8.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier

                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 100.dp, height = 100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .height(100.dp) // Cùng chiều cao với ảnh
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 8.dp, top = 2.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f)) // đẩy hàng nút xuống dưới cùng

                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(onClick = onView, modifier = Modifier.size(24.dp), contentPadding = PaddingValues(0.dp)) {
                            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        TextButton(onClick = onEdit, modifier = Modifier.size(24.dp), contentPadding = PaddingValues(0.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = MaterialTheme.colorScheme.primary)
                        }
                        TextButton(onClick = onDelete, modifier = Modifier.size(24.dp), contentPadding = PaddingValues(0.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Xoá", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

