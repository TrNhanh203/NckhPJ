package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.screen.common.YeuCauOverviewCard
import com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel.DashBoardOptionviewViewMode
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.facilitiesmanagementpj.ui.screen.common.DeviceOverviewCard
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel
import com.example.facilitiesmanagementpj.R


@Composable
fun AdminDashboardScreen(navController: NavController) {
    val viewModel: DashBoardOptionviewViewMode = hiltViewModel()
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.let {
            viewModel.loadData(it)
            viewModel.loadDeviceData(it)
        }
    }
    val statusCounts by viewModel.statusCounts.collectAsState()
    val deviceStatusCounts by viewModel.deviceStatusCounts.collectAsState()


    ScaffoldLayout(title = "", navController = navController, showBottomBar = true, isHomeScreen = false) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .then(modifier),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Admin Dashboard",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold // tăng độ đậm
                ),
                color = MaterialTheme.colorScheme.primary // đổi màu
            )
            Spacer(modifier = Modifier.height(2.dp))


//            DashboardOption(
//                title = "Quản lý yêu cầu",
//                icon = Icons.Default.AccountBox,
//                color = Color.Blue,
//                onClick = { navController.navigate(Screen.AdminRequestList.route) }
//            )
            YeuCauOverviewCard(
                statusCounts = statusCounts,
                onClickDetail = { navController.navigate(Screen.AdminRequestList.route) }
            )

//            DashboardOption(
//                title = "Quản lý thiết bị",
//                icon = Icons.Default.Settings,
//                color = Color.Blue,
//                onClick = { navController.navigate(Screen.AdminDeviceList.route) }
//            )
            Spacer(modifier = Modifier.height(8.dp))
            DeviceOverviewCard(
                statusCounts = deviceStatusCounts,
                modifier = Modifier.fillMaxWidth(),
                onClickDetail = { navController.navigate(Screen.AdminDeviceList.route) }
            )


            DashboardOption(
                title = "Danh sách KTV",
                imgIcon = painterResource(id = R.drawable.crew),
                onClick = { navController.navigate(Screen.DanhSachKyThuatVien.route) }
            )

            DashboardOption(
                title = "Quản lý tài khoản",
                imgIcon = painterResource(id = R.drawable.account),
                onClick = { navController.navigate(Screen.AdminAccount.route) }
            )

            DashboardOption(
                title = "Quản lý bài viết",
                imgIcon = painterResource(id = R.drawable.post),
                onClick = { navController.navigate(Screen.AdminBaiViet.route) }
            )
        }
    }
}

// Component hiển thị từng tùy chọn trong Dashboard
//@Composable
//fun DashboardOption(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(80.dp)
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f))
//    ) {
//        Row(
//            modifier = Modifier.fillMaxSize().padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Start
//        ) {
//            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(40.dp))
//            Spacer(modifier = Modifier.width(16.dp))
//            Text(title, fontSize = 20.sp, color = color)
//        }
//    }
//}

@Composable
fun DashboardOption(
    title: String,
    imgIcon: Painter? = null,
    icon: ImageVector = Icons.Default.AccountCircle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(86.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon + Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (imgIcon != null) {
                    Image(
                        painter = imgIcon,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp) // icon nhỏ hơn
                    )
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp), // chữ to hơn
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // IconButton hình vuông bo góc nhẹ
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Xem chi tiết",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}


