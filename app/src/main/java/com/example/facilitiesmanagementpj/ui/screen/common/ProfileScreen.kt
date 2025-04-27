package com.example.facilitiesmanagementpj.ui.screen.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.ProfileViewModel
import com.example.facilitiesmanagementpj.ui.navigation.Screen


@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val taiKhoan by viewModel.taiKhoan.collectAsState()

    if (taiKhoan == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    ScaffoldLayout(title = "Hồ sơ", navController = navController, showBottomBar = true) { modifier ->
        Column(
            modifier = Modifier

                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .then(modifier),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                AvatarWithStatus(
                    imageUrl = null, // hoặc tài khoản có avatar thì truyền link
                    isOnline = taiKhoan!!.trangThai == "Trực Tuyến"
                )
            }
            Spacer(modifier = Modifier.height(24.dp))


            Text(
                "Hồ sơ cá nhân",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primaryContainer
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileInfoItem("Tên tài khoản", taiKhoan!!.tenTaiKhoan)
            ProfileInfoItem("Họ và Tên", taiKhoan!!.hoTen ?: "Chưa cập nhật")
            ProfileInfoItem("Email", taiKhoan!!.email ?: "Chưa cập nhật")
            ProfileInfoItem("Số điện thoại", taiKhoan!!.soDienThoai ?: "Chưa cập nhật")
            ProfileInfoItem("Trạng thái", taiKhoan!!.trangThai)
            ProfileInfoItem("Lần đăng nhập cuối", taiKhoan!!.lastLogin?.toString() ?: "Chưa đăng nhập")

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Cập nhật thông tin")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {  },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text("Đổi mật khẩu")
            }
        }
    }
}


@Composable
fun ProfileInfoItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Composable
fun AvatarWithStatus(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    isOnline: Boolean
) {
    Box(
        modifier = modifier.size(96.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Avatar
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Chấm trạng thái
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(
                    color = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336),
                    shape = CircleShape
                )
                .border(2.dp, MaterialTheme.colorScheme.surfaceContainer, CircleShape)
        )
    }
}

