package com.example.facilitiesmanagementpj.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    navController: NavController,
    isHomeScreen: Boolean = false,
    onNavigationIconClick: (() -> Unit)? = null,
    onBackClick: () -> Unit = { navController.popBackStack() }
) {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()

    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.CenterStart // hoặc Center nếu muốn cả trái-phải
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isHomeScreen) {
                    onNavigationIconClick?.let {
                        IconButton(onClick = it) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                } else {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        },
        actions = {
            if (isHomeScreen) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Thông báo",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    IconButton(onClick = {
                        // TODO: mở dropdown menu
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Tùy chọn",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    )
}








//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CustomTopAppBar(
//    title: String,
//    navController: NavController,
//    isHomeScreen: Boolean = false,
//    onNavigationIconClick: (() -> Unit)? = null,
//    onBackClick: () -> Unit = { navController.popBackStack() } // Custom back navigation function
//) {
//    val sessionViewModel: SessionViewModel = hiltViewModel()
//    val currentUser by sessionViewModel.currentUser.collectAsState()
//
//    val loggedInUser = currentUser
//    TopAppBar(
//        title = { Text("${title} vt:${loggedInUser?.vaiTroId }", color = Color.White) },
//        colors = TopAppBarDefaults.mediumTopAppBarColors(
//            containerColor = Color.Black
//        ),
//        navigationIcon = {
//            if (isHomeScreen) {
//                if (onNavigationIconClick != null) {
//                    IconButton(onClick = onNavigationIconClick) {
//                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
//                    }
//                }
//            } else {
//
//                    IconButton(onClick = onBackClick) { // Use custom back navigation function
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
//                    }
//
//            }
//        },
//        modifier = Modifier.fillMaxWidth()
//    )
//}
//
