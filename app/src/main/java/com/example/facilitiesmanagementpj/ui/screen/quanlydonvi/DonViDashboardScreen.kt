package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.screen.admin.DashboardOption
import com.example.facilitiesmanagementpj.ui.screen.common.DeviceOverviewCard
import com.example.facilitiesmanagementpj.ui.screen.common.YeuCauOverviewCard
import com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel.DashBoardOptionviewViewMode
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel

@Composable
fun DonViDashboardScreen(navController: NavController) {
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

    ScaffoldLayout(
        title = "",
        navController = navController,
        showBottomBar = true
    ) { modifier ->
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
                text = "QLDV Dashboard",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold // tăng độ đậm
                ),
                color = MaterialTheme.colorScheme.primary // đổi màu
            )
            Spacer(modifier = Modifier.height(2.dp))

            DeviceOverviewCard(
                statusCounts = deviceStatusCounts,
                modifier = Modifier.fillMaxWidth(),
                onClickDetail = { navController.navigate(Screen.QLDVThietBiTheoDV.route) }
            )

            DashboardOption(
                title = "Danh sách phòng",
                imgIcon = painterResource(id = R.drawable.university),
                onClick = { navController.navigate(Screen.QLDVPhong.route) }
            )

//            DashboardOption(
//                title = "Danh sách báo cáo",
//                icon = Icons.Default.Settings,
//                color = Color.DarkGray,
//                onClick = { navController.navigate(Screen.QLDVDanhSachYeuCau.route) }
//            )
            Spacer(modifier = Modifier.height(16.dp))
            YeuCauOverviewCard(
                statusCounts = statusCounts,
                onClickDetail = { navController.navigate(Screen.QLDVDanhSachYeuCau.route) }
            )

            DashboardOption(
                title = "Tạo Yêu Cầu Mới",
                imgIcon = painterResource(id = R.drawable.createyeucau),
                onClick = { navController.navigate(Screen.ThemYeuCauMoi.createRoute(null)) }
            )

//            DashboardOption(
//                title = "Ds thiết bị của dvi",
//                icon = Icons.Default.Settings,
//                color = Color.DarkGray,
//                onClick = { navController.navigate(Screen.QLDVThietBiTheoDV.route) }
//            )



        }

    }
}