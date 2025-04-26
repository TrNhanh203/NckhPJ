package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVPhongViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel


@Composable
fun QLDVPhongScreen(navController: NavController, viewModel: QLDVPhongViewModel = hiltViewModel()) {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()
    val phongList by viewModel.phongList.collectAsState()
    val donViId = currentUser?.donViId ?: 0

    LaunchedEffect(currentUser) {
        viewModel.loadPhongList(donViId)
    }
    com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout("Danh Sách Phòng", navController, showTopBar = true,showBottomBar = false,showDrawer = false)
    { modifier ->
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp).then(modifier)) {
            //Text("Danh sách phòng", style = MaterialTheme.typography.headlineMedium)

//            LazyColumn {
//                items(phongList) { phong ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                            .clickable { navController.navigate(Screen.QLDVThietBiTheoPhong.createRoute(phongId = phong.id)) }
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text("Tên phòng: ${phong.tenPhong}")
//                            Text("Dãy: ${phong.tenDay} - Tầng: ${phong.tenTang}")
//                            Text("Số lượng thiết bị: ${phong.soLuongThietBi}")
//                        }
//                    }
//                }
//            }

            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(phongList) { phong ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Screen.QLDVThietBiTheoPhong.createRoute(phongId = phong.id, phongName = phong.tenPhong)) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column {
                            // Thanh màu primary phía trên
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            )

                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    shape = CircleShape
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            phong.tenPhong.firstOrNull()?.toString() ?: "?",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                Spacer(Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = phong.tenPhong,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1 // tránh tràn
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = "Dãy: ${phong.tenDay} - Tầng: ${phong.tenTang}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    Text(
                                        text = "Số lượng thiết bị: ${phong.soLuongThietBi}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal
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
