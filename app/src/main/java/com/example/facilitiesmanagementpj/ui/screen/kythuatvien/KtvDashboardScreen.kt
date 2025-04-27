package com.example.facilitiesmanagementpj.ui.screen.kythuatvien

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.screen.admin.DashboardOption
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.ui.screen.common.KtvLamViecViewCard
import com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel.DashBoardOptionviewViewMode


@Composable
fun KtvDashboardScreen(navController: NavController) {
    val viewModel: DashBoardOptionviewViewMode = hiltViewModel()
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()
    val taskStatusCounts by viewModel.taskStatusCounts.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.let {
            viewModel.loadPhanCongKtv(it.id)
        }
    }



    ScaffoldLayout(
        title = "",
        navController = navController,
        showBottomBar = true
    ) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .then(modifier),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "KTV Dashboard",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold // tăng độ đậm
                ),
                color = MaterialTheme.colorScheme.primary // đổi màu
            )
            KtvLamViecViewCard(
                statusCounts = taskStatusCounts,
                onClickDetail = {
                    currentUser?.let { user ->
                        navController.navigate(
                            Screen.KtvDanhSachCongViec.createRoute(user.id)
                        )
                    }
                }
            )
            DashboardOption(
                title = "Chuyên Môn",
                imgIcon = painterResource(id = R.drawable.abilities),
                onClick = {
                    currentUser?.let { user ->
                        navController.navigate(Screen.ChuyenMonKyThuatVien.createRoute(user.id))
                    }
                }
            )

//            DashboardOption(
//                title = "Công Việc",
//                icon = Icons.Default.AccountBox,
//                onClick = {
//                    currentUser?.let { user ->
//                        navController.navigate(
//                            Screen.KtvDanhSachCongViec.createRoute(user.id)
//                        )
//                    }
//                }
//            )


//            DashboardOption(
//                title = "Bảng Công Việc",
//                icon = Icons.Default.Settings,
//                onClick = { }
//            )


        }
    }
}

//@Composable
//fun DashboardOption(
//    title: String,
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    color: Color,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(80.dp)
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f))
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Start
//        ) {
//            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(40.dp))
//            Spacer(modifier = Modifier.width(16.dp))
//            Text(title, fontSize = 20.sp, color = color)
//        }
//    }
//}