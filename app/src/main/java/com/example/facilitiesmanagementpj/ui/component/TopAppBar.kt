package com.example.facilitiesmanagementpj.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.data.session.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    navController: NavController,
    onNavigationIconClick: (() -> Unit)? = null
) {
    val loggedInUser = SessionManager.currentUser
    TopAppBar(
        title = { Text("${title} vt:${loggedInUser?.vaiTroId }", color = Color.White) }, // Màu chữ trắng
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Black // Màu nền đen
        ),
//        navigationIcon = {
//            onNavigationIconClick?.let {
//                IconButton(onClick = it) {
//                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
//                }
//            }
//        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
