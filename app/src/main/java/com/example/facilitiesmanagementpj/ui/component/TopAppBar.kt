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
    isHomeScreen: Boolean = false,
    onNavigationIconClick: (() -> Unit)? = null,
    onBackClick: () -> Unit = { navController.popBackStack() } // Custom back navigation function
) {
    val loggedInUser = SessionManager.currentUser
    TopAppBar(
        title = { Text("${title} vt:${loggedInUser?.vaiTroId }", color = Color.White) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Black
        ),
        navigationIcon = {
            if (isHomeScreen) {
                if (onNavigationIconClick != null) {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                }
            } else {

                    IconButton(onClick = onBackClick) { // Use custom back navigation function
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CustomTopAppBar(
//    title: String,
//    navController: NavController,
//    isHomeScreen: Boolean = false,
//    onNavigationIconClick: (() -> Unit)? = null
//) {
//    val loggedInUser = SessionManager.currentUser
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
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
//                }
//            }
//        },
//        modifier = Modifier.fillMaxWidth()
//    )
//}



