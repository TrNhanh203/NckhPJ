package com.example.facilitiesmanagementpj.ui.screen.common


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.viewmodel.RegisterViewModel
import com.example.facilitiesmanagementpj.ui.navigation.Screen

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    var tenTaiKhoan by remember { mutableStateOf("") }
    var matKhau by remember { mutableStateOf("") }
    var hoTen by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var soDienThoai by remember { mutableStateOf("") }
    var vaiTro by remember { mutableStateOf("Kỹ thuật viên") }
    var donVi by remember { mutableStateOf<String?>(null) }

    val registerState = viewModel.registerState.collectAsState().value
    val donViList = viewModel.donViList.collectAsState(initial = emptyList()).value

    LaunchedEffect(registerState) {
        if (registerState == "success") {
            navController.navigate(Screen.Login.route)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Đăng ký tài khoản", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = tenTaiKhoan, onValueChange = { tenTaiKhoan = it }, label = { Text("Tên tài khoản") })
        OutlinedTextField(value = matKhau, onValueChange = { matKhau = it }, label = { Text("Mật khẩu") })
        OutlinedTextField(value = hoTen, onValueChange = { hoTen = it }, label = { Text("Họ và tên") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = soDienThoai, onValueChange = { soDienThoai = it }, label = { Text("Số điện thoại") })

        DropdownMenuFilter(
            label = "Vai trò",
            items = listOf("Kỹ thuật viên", "Quản lý đơn vị"),
            selected = vaiTro,
            onSelectedChange = {
                if (it != null) {
                    vaiTro = it
                }
            }
        )

        if (vaiTro == "Quản lý đơn vị") {
            DropdownMenuFilter(
                label = "Đơn vị",
                items = donViList.map { it.tenDonVi },
                selected = donVi,
                onSelectedChange = { donVi = it }
            )
        }

        Button(onClick = {
            viewModel.register(
                tenTaiKhoan, matKhau, hoTen, email, soDienThoai,
                if (vaiTro == "Kỹ thuật viên") 2 else 3,
                donViList.find { it.tenDonVi == donVi }?.id
            )
        }) {
            Text("Đăng ký")
        }
    }
}




