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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đối chiếu minh chứng",color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary // 🔷 nền primary
                )
            )
        },
        bottomBar = {
            BottomTabSwitch(
                selectedIndex = selectedTab,
                onSelect = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().then(Modifier.padding(innerPadding))) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Minh chứng yêu cầu") })
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Minh chứng làm việc") })
            }

            when (selectedTab) {
                0 -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Ảnh/Video trước khi xử lý",
                            style = MaterialTheme.typography.titleMedium
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(imageUris) { uri ->
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { showImagePreview = uri },
                                    contentScale = ContentScale.Crop
                                )
                            }

                            videoUri?.let { uri ->
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.Black)
                                            .clickable { showVideoPreview = uri },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("▶", color = Color.White, fontSize = 40.sp)
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

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    ) {
                        groupedByKtv.forEach { (ktvId, mediaList) ->
                            item {
                                val avatarIndex = remember(ktvId) { (ktvId % 6) + 1 }
                                val avatarResId = when (avatarIndex) {
                                    1 -> R.drawable.engineer01
                                    2 -> R.drawable.engineer02
                                    3 -> R.drawable.engineer03
                                    4 -> R.drawable.engineer04
                                    5 -> R.drawable.engineer05
                                    else -> R.drawable.engineer06
                                }

                                var expanded by remember { mutableStateOf(false) }
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    elevation = CardDefaults.cardElevation(6.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                                ) {
                                    Column {
                                        // 🔵 Thanh header với tên + icon toggle
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.primaryContainer)
                                                .clickable { expanded = !expanded }
                                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                        ) {

                                        }

                                        // 🔹 Mô tả phân công
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 16.dp)
                                                .clickable { expanded = !expanded },
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                painter = painterResource(id = avatarResId),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .border(
                                                        1.5.dp,
                                                        MaterialTheme.colorScheme.primary,
                                                        RoundedCornerShape(12.dp)
                                                    )
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = viewModel.getKtvNameByPhanCongKtvId(ktvId),
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 18.sp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(Modifier.height(4.dp))
                                                Text(
                                                    text = "Phụ trách: ${
                                                        viewModel.getMoTaByPhanCongKtvId(
                                                            ktvId
                                                        )
                                                    }" ?: "Không có mô tả công việc",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontSize = 15.sp
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            Icon(
                                                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                                contentDescription = null,
                                                modifier = Modifier.size(28.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )

                                        }


                                        // 🔽 Danh sách ảnh khi mở
                                        if (expanded) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = 380.dp) // 🔹 Giới hạn chiều cao
                                                    .verticalScroll(rememberScrollState()) // 🔹 Cho phép cuộn nội dung bên trong
                                            ) {
                                                Column(modifier = Modifier.fillMaxWidth()) {
                                                    mediaList.forEach { media ->
                                                        Column(
                                                            modifier = Modifier.padding(
                                                                horizontal = 12.dp,
                                                                vertical = 6.dp
                                                            )
                                                        ) {
                                                            Card(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(vertical = 8.dp),
                                                                shape = RoundedCornerShape(12.dp),
                                                                elevation = CardDefaults.cardElevation(
                                                                    4.dp
                                                                ),
                                                                colors = CardDefaults.cardColors(
                                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                                                )
                                                            ) {
                                                                Column {
                                                                    // 🔹 Header với loại ảnh + thời gian
                                                                    Row(
                                                                        modifier = Modifier
                                                                            .fillMaxWidth()
                                                                            .background(
                                                                                MaterialTheme.colorScheme.secondary
                                                                            )
                                                                            .padding(
                                                                                horizontal = 12.dp,
                                                                                vertical = 4.dp
                                                                            ),
                                                                        verticalAlignment = Alignment.CenterVertically
                                                                    ) {
                                                                        Icon(
                                                                            imageVector = Icons.Default.Image,
                                                                            contentDescription = null,
                                                                            tint = MaterialTheme.colorScheme.onSecondary
                                                                        )
                                                                        Spacer(
                                                                            modifier = Modifier.width(
                                                                                8.dp
                                                                            )
                                                                        )

                                                                        val formattedTime =
                                                                            remember(media.thoiGianTaiLen) {
                                                                                media.thoiGianTaiLen?.let {
                                                                                    val date =
                                                                                        Date(it)
                                                                                    val formatter =
                                                                                        SimpleDateFormat(
                                                                                            "dd/MM/yyyy HH:mm",
                                                                                            Locale.getDefault()
                                                                                        )
                                                                                    formatter.format(
                                                                                        date
                                                                                    )
                                                                                }
                                                                                    ?: "Không rõ thời gian"
                                                                            }

                                                                        Text(
                                                                            text = "${media.loaiAnh} – $formattedTime",
                                                                            style = MaterialTheme.typography.labelMedium,
                                                                            color = MaterialTheme.colorScheme.onSecondary
                                                                        )

                                                                    }

                                                                    // 🔸 Ảnh minh chứng
                                                                    Image(
                                                                        painter = rememberAsyncImagePainter(
                                                                            media.urlAnh
                                                                        ),
                                                                        contentDescription = null,
                                                                        modifier = Modifier
                                                                            .fillMaxWidth()
                                                                            .height(200.dp)
                                                                            .clip(
                                                                                RoundedCornerShape(
                                                                                    0.dp
                                                                                )
                                                                            ),
                                                                        contentScale = ContentScale.Crop
                                                                    )

                                                                    // 🔻 Ghi chú dưới ảnh nếu có
                                                                    media.ghiChu?.takeIf { it.isNotBlank() }
                                                                        ?.let {
                                                                            Box(
                                                                                modifier = Modifier
                                                                                    .fillMaxWidth()
                                                                                    .background(
                                                                                        MaterialTheme.colorScheme.secondary
                                                                                    )
                                                                                    .padding(
                                                                                        horizontal = 12.dp,
                                                                                        vertical = 4.dp
                                                                                    )
                                                                            ) {
                                                                                Text(
                                                                                    text = "Mô tả: $it",
                                                                                    style = MaterialTheme.typography.bodySmall,
                                                                                    color = MaterialTheme.colorScheme.onSecondary,
                                                                                    maxLines = 5,
                                                                                )
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


    }


@Composable
fun BottomTabSwitch(
    modifier: Modifier = Modifier,
    options: List<String> = listOf("Minh chứng yêu cầu", "Minh chứng làm việc"),
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.primary
    val selectedColor = Color.White
    val unselectedColor = Color.White.copy(alpha = 0.7f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(backgroundColor)
                .padding(4.dp)
        ) {
            options.forEachIndexed { index, text ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) selectedColor else Color.Transparent)
                        .clickable { onSelect(index) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = if (isSelected) backgroundColor else Color.White,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    }
}
