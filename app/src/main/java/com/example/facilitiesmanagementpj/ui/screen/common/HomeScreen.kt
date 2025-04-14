package com.example.facilitiesmanagementpj.ui.screen.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.facilitiesmanagementpj.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel.BaiVietViewModel


@Composable
fun HomeScreen(navController: NavController) {
    val baiVietViewModel: BaiVietViewModel = hiltViewModel()
    var showExitDialog by remember { mutableStateOf(false) }
    val listBaiViet by baiVietViewModel.dsBaiViet.collectAsState()
    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Xác nhận thoát") },
            text = { Text("Bạn có chắc chắn muốn thoát không?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    // Close the app
                    (navController.context as Activity).finish()
                    //navController.popBackStack(navController.graph.startDestinationId, true)
                }) {
                    Text("Thoát")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    ScaffoldLayout(title = "Trang chủ", navController = navController, isHomeScreen = true) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .then(modifier),
        ) {
            //Text("Nội dung màn hình chính", style = MaterialTheme.typography.headlineMedium)
            HeaderDateTimeBar() // 👈 đặt trên cùng

            Spacer(Modifier.height(8.dp))
            Text(
                "Bài viết mới",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                listBaiViet.forEach { item ->
                    ArticleCardDemo(
                        imagePainter = rememberAsyncImagePainter(item.anhDaiDien),
                        title = item.tieuDe,
                        description = item.moTa,
                        url = item.link ?: "",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }


//            Spacer(modifier = Modifier.height(16.dp))
//
//            LocationCardDemo(
//                onNavigateClick = {
//                    // TODO: Mở Google Maps hoặc navigation
//                }
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
//            DemoCardTiemKiemPhong(
//                onFilterClick = {
//                    // Mở bottom sheet bộ lọc nâng cao
//                }
//            )


        }
    }
}


@Composable
fun HeaderDateTimeBar(modifier: Modifier = Modifier) {
    val currentTime by rememberUpdatedState(newValue = System.currentTimeMillis())

    val dateFormatter = remember {
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("vi", "VN"))
    }
    val timeFormatter = remember {
        SimpleDateFormat("HH:mm", Locale("vi", "VN"))
    }

    val currentDate = remember { dateFormatter.format(Date(currentTime)) }
    val currentHour = remember { timeFormatter.format(Date(currentTime)) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = currentDate.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Giờ hiện tại: $currentHour",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoCardTiemKiemPhong(
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 🔹 Đường dẫn
            Text(
                text = "Phòng học / Tìm kiếm",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))

            // 🔹 Tiêu đề
            Text(
                text = "Tìm kiếm phòng học",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(16.dp))

            // 🔹 Các chip bộ lọc
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(selected = true, onClick = {}, label = { Text("Tầng 1") })
                FilterChip(selected = true, onClick = {}, label = { Text("Có máy chiếu") })
                FilterChip(selected = false, onClick = {}, label = { Text("Có máy lạnh") })
                FilterChip(selected = false, onClick = {}, label = { Text("Dãy A") })

                AssistChip(
                    onClick = onFilterClick,
                    label = { Text("Bộ lọc nâng cao") },
                    leadingIcon = {
                        Icon(Icons.Default.Tune, contentDescription = null)
                    }
                )
            }
        }
    }
}



data class ArticleData(
    val title: String,
    val description: String,
    val imageRes: Int
)
@Composable
fun ArticleCardDemo(
    imagePainter: Painter,
    title: String,
    description: String,
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Image(
                painter = imagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Spacer(Modifier.height(12.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text("Xem bài viết", color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}




@Composable
fun LocationCardDemo(
    address: String = "401 West Springfield Ave",
    locationDetail: String = "Philadelphia, PA 19118, USA",
    onNavigateClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 🔹 Background map image
            Image(
                painter = painterResource(id = R.drawable.mapdemo), // ảnh giả lập map
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 🔹 Vùng overlay mờ phía dưới chứa thông tin
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text("My Current Location", style = MaterialTheme.typography.labelSmall)
                    Text(address, style = MaterialTheme.typography.titleMedium)
                    Text(locationDetail, style = MaterialTheme.typography.bodySmall)
                }
            }

            // 🔹 Nút chia sẻ hoặc dẫn đường
            IconButton(
                onClick = onNavigateClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                        shape = CircleShape
                    )
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = "Chia sẻ",
                    tint = Color.White
                )
            }
        }
    }
}

