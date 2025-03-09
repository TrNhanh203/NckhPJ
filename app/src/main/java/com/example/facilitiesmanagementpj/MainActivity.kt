package com.example.facilitiesmanagementpj

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.facilitiesmanagementpj.data.database.AppDatabase
import com.example.facilitiesmanagementpj.ui.screen.AddThietBiScreen
import com.example.facilitiesmanagementpj.ui.screen.PhongScreen
import com.example.facilitiesmanagementpj.ui.screen.TangScreen
import com.example.facilitiesmanagementpj.ui.screen.VaiTroScreen
import com.example.facilitiesmanagementpj.ui.viewmodel.ThietBiViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.VaiTroViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ThietBiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //MyApp()
            //TangScreen()
            //PhongScreen()
            AddThietBiScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val vaiTroViewModel: VaiTroViewModel = hiltViewModel() // ⚠️ Inject ViewModel bằng Hilt

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            VaiTroScreen(vaiTroViewModel)
        }
    }
}
