package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.grid.items
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.dao.ThietBiWithDetails
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBi
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVThietBiViewModel
import kotlinx.coroutines.launch
//
//@Composable
//fun QLDVThietBiTheoPhongScreen(navController: NavController,
//                               phongId: Int,
//                               phongName: String,
//                               viewModel: QLDVThietBiViewModel = hiltViewModel()) {
//    val thietBiList by viewModel.thietBiList.collectAsState()
//
//    LaunchedEffect(phongId) {
//        viewModel.loadThietBiListByPhong(phongId)
//    }
//    com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout("Danh Sách Thiết Bị", navController, showTopBar = true,showBottomBar = false,showDrawer = false)
//    { modifier ->
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp).then(modifier)) {
//            Text("Danh sách thiết bị trong phòng", style = MaterialTheme.typography.headlineMedium)
//
//            LazyColumn {
//                items(thietBiList) { thietBi ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                            .clickable { navController.navigate(Screen.ThietBiDetail.createRoute(thietBi.id, isEditMode = false, yeuCauId = null)) }
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text("Tên thiết bị: ${thietBi.tenThietBi}")
//                            Text("Loại: ${thietBi.tenLoai}")
//                            Text("Trạng thái: ${thietBi.trangThai}")
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//}

@Composable
fun QLDVThietBiTheoPhongScreen(
    phongId: Int,
    phongName: String,
    navController: NavController,
    viewModel: QLDVThietBiViewModel = hiltViewModel()
) {
    val thietBiList by viewModel.filteredThietBiList.collectAsState()

    LaunchedEffect(phongId) {
        viewModel.loadThietBiListByPhong(phongId)
    }

    val formattedTitle = remember(phongName) {
        if (phongName.length > 30) {
            "Thiết Bị Phòng ${phongName.take(30)}..."
        } else {
            "Thiết Bị Phòng $phongName"
        }
    }

    ScaffoldLayout(
        title = "Thiết bị theo phòng",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
        onBackClick = { navController.navigateUp() }
    ) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            // Filter chỉ trạng thái và loại thiết bị
            FilterSectionPhongScreen(
                viewModel = viewModel,
                thietBiList = thietBiList
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    items(thietBiList) { thietBi ->
                        DeviceCard(
                            thietBi = thietBi,
                            isSelected = false,
                            isSelectMode = false,
                            yeuCauId = null,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSectionPhongScreen(
    viewModel: QLDVThietBiViewModel,
    thietBiList: List<ThietBiWithDetails>
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()
    var currentFilterKey by remember { mutableStateOf<String?>(null) }

    val filterMap = mapOf(
        "Trạng thái" to TrangThaiThietBi.ALL,
        "Loại Thiết Bị" to thietBiList.map { it.tenLoai }.distinct()
    )

    val selectedMap = mapOf(
        "Trạng thái" to viewModel.selectedTrangThai.collectAsState().value,
        "Loại Thiết Bị" to viewModel.selectedLoaiThietBi.collectAsState().value
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f), thickness = 1.dp)

        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 2.dp)
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState())
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    filterMap.forEach { (key, _) ->
                        val selected = selectedMap[key]
                        val isSelected = !selected.isNullOrBlank()

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                currentFilterKey = key
                                scope.launch { bottomSheetState.show() }
                            },
                            label = {
                                Text(
                                    text = if (isSelected) "$key: $selected" else key,
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected)
                                    Color.Transparent
                                else
                                    MaterialTheme.colorScheme.primary,
                                borderWidth = 2.dp
                            ),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .widthIn(min = 80.dp, max = 160.dp)
                                .height(32.dp)
                        )
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { viewModel.resetFilters() }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Xóa lọc",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
        Divider(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f), thickness = 1.dp)

        currentFilterKey?.let { key ->
            val items = filterMap[key] ?: emptyList()
            ModalBottomSheet(
                onDismissRequest = { currentFilterKey = null },
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Chọn $key",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    items.forEach { item ->
                        ListItem(
                            headlineContent = {
                                Text(item, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            },
                            modifier = Modifier.clickable {
                                when (key) {
                                    "Trạng thái" -> viewModel.setTrangThaiFilter(item)
                                    "Loại Thiết Bị" -> viewModel.setLoaiThietBiFilter(item)
                                }
                                currentFilterKey = null
                            }
                        )
                    }
                }
            }
        }
    }
}
