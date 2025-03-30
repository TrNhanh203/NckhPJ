package com.example.facilitiesmanagementpj.ui.screen.admin
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungLamViec
import com.example.facilitiesmanagementpj.ui.viewmodel.YeuCauGiaHanViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XemYeuCauGiaHanScreen(
    phanCongKtvId: Int,
    navController: NavController,
) {
    val viewModel: YeuCauGiaHanViewModel = hiltViewModel()
    val anhList by viewModel.anhGiaHan.collectAsState()
    val soPhut by viewModel.soPhutXinGiaHan.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    // Load khi vào màn hình
    LaunchedEffect(phanCongKtvId) {
        viewModel.loadYeuCauGiaHan(phanCongKtvId)
    }

    val isLoading = anhList.isEmpty() && soPhut == 0

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Yêu cầu gia hạn") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() })
                    {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        bottomBar = {
            if (!isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                viewModel.tuChoiYeuCau(phanCongKtvId)
                                snackbarHostState.showSnackbar("Đã từ chối yêu cầu gia hạn")
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Từ chối")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.duyetYeuCau(phanCongKtvId)
                                snackbarHostState.showSnackbar("Đã duyệt yêu cầu gia hạn")
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Duyệt")
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            // Hiển thị vòng tròn loading
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Nội dung chính như cũ
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Kỹ thuật viên xin thêm: $soPhut phút",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(anhList) { anh ->
                        Column(
                            modifier = Modifier
                                .width(280.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF5F5F5))
                                .padding(12.dp)
                        ) {
                            AsyncImage(
                                model = anh.urlAnh,
                                contentDescription = "Ảnh minh chứng",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = anh.ghiChu ?: "Không có ghi chú",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

