package com.example.facilitiesmanagementpj.ui.screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.dao.TaiKhoanWithRole
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.AuthViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.TaiKhoanViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun DebugLoginScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel(),
                     taiKhoanViewModel: TaiKhoanViewModel = hiltViewModel()) {
    val taiKhoanList by taiKhoanViewModel.taiKhoanList.collectAsState()
    var selectedTaiKhoan by remember { mutableStateOf<TaiKhoanWithRole?>(null) }
    val loginResult by viewModel.loginResult




    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Chọn tài khoản để đăng nhập", style = MaterialTheme.typography.headlineMedium)

        LazyColumn {
            items(taiKhoanList) { taiKhoan ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { selectedTaiKhoan = taiKhoan }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tên: ${taiKhoan.tenTaiKhoan}")
                        Text("Vai trò: ${taiKhoan.tenVaiTro}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                selectedTaiKhoan?.let {
                    viewModel.login(it.tenTaiKhoan, it.matKhau) // ✅ Gọi login như đăng nhập thật
                }
            },
            enabled = selectedTaiKhoan != null
        ) {
            Text("Đăng nhập")
        }

        if (loginResult.success) {
            LaunchedEffect(Unit) {
                navController.navigate(Screen.Home.route) // ✅ Chuyển hướng sau khi đăng nhập thành công
            }
        } else if (loginResult.errorMessage != null) {
            Text(
                text = loginResult.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

