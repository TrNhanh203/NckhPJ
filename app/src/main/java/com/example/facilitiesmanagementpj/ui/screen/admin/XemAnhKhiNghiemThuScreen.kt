package com.example.facilitiesmanagementpj.ui.screen.admin
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.ui.component.VideoPreviewAdmin
import com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel.BienBanViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.sp


@Composable
fun ChiTietAnhMinhChungScreen(
    viewModel: BienBanViewModel = hiltViewModel(),
    chiTietId: Int,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val (anhBaoCao, anhLamViec) = viewModel.anhTheoChiTietMap[chiTietId] ?: Pair(emptyList(), emptyList())
    val imageUris = remember(anhBaoCao) {
        anhBaoCao.filter { it.type == "image" }.map { it.urlAnh.toUri() }
    }
    val videoUri = remember(anhBaoCao) {
        anhBaoCao.firstOrNull { it.type == "video" }?.urlAnh?.toUri()
    }

    LaunchedEffect(chiTietId) {
        viewModel.loadAnhTheoChiTiet(chiTietId)
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    var showImagePreview by remember { mutableStateOf<Uri?>(null) }
    var showVideoPreview by remember { mutableStateOf<Uri?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Minh chứng yêu cầu") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Minh chứng làm việc") })
        }

        when (selectedTab) {
            0 -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Minh chứng đính kèm", style = MaterialTheme.typography.titleMedium)

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(imageUris) { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { showImagePreview = uri },
                                contentScale = ContentScale.Crop
                            )
                        }

                        item {
                            videoUri?.let { uri ->
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black)
                                        .clickable { showVideoPreview = uri },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("▶", color = Color.White, fontSize = 32.sp)
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                val groupedByKtv = remember(anhLamViec) {
                    anhLamViec.groupBy { it.phanCongKTVId }
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    groupedByKtv.forEach { (ktvId, mediaList) ->
                        item {
                            var expanded by remember { mutableStateOf(false) }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable { expanded = !expanded },
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Kỹ thuật viên ID: $ktvId", fontWeight = FontWeight.SemiBold)
                                        Icon(
                                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = null
                                        )
                                    }

                                    if (expanded) {
                                        mediaList.forEach { media ->
                                            Text("• ${media.loaiAnh} - ${media.thoiGianTaiLen}", style = MaterialTheme.typography.labelMedium)
                                            Spacer(Modifier.height(4.dp))
                                            Image(
                                                painter = rememberAsyncImagePainter(media.urlAnh),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(180.dp)
                                                    .clip(RoundedCornerShape(8.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(Modifier.height(12.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    // Xem ảnh fullscreen
    showImagePreview?.let { uri ->
        Dialog(onDismissRequest = { showImagePreview = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }

    // Xem video fullscreen
    showVideoPreview?.let { uri ->
        Dialog(onDismissRequest = { showVideoPreview = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                VideoPreviewAdmin(uri = uri)
            }
        }
    }
}
