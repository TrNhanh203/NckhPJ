package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.data.dao.ThietBiWithDetails
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBi
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminDeviceListViewModel
import kotlinx.coroutines.launch
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBiColor


@Composable
fun AdminDeviceListScreen(
    navController: NavController,
    viewModel: AdminDeviceListViewModel = hiltViewModel()
) {
    val thietBiList by viewModel.filteredThietBiList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadThietBiList()
    }

    BackHandler {
        navController.navigateUp()
    }

    com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout(
        title = "Danh Sách Thiết Bị",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
    ) { innerPadding ->

        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).then(innerPadding)) {
            FilterSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                viewModel = viewModel,
                thietBiList = thietBiList
            )
//            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 0.dp)) {
//                items(thietBiList) { thietBi ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(0.dp)
//                            .clickable {
//                                navController.navigate(Screen.AdminDeviceDetail.createRoute(thietBi.id, 0))
//                            },
//                        colors = CardDefaults.cardColors(
//                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
//                            contentColor = MaterialTheme.colorScheme.onSurface
//                        )
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text("Tên: ${thietBi.tenThietBi}")
//                            Text("Loại: ${thietBi.tenLoai}")
//                            Text("Phòng: ${thietBi.tenPhong} - Tầng: ${thietBi.tenTang} - Dãy: ${thietBi.tenDay}")
//                            Text("Trạng thái: ${thietBi.trangThai}")
//                        }
//                    }
//                }
//            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = thietBiList,
                ) { thietBi ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.85f) // tỉ lệ card (cao hơn rộng 1 chút)
                            .clickable {
                                navController.navigate(Screen.AdminDeviceDetail.createRoute(thietBi.id, 0))
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        ),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(8.dp), // padding để icon không đụng viền
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = getIllustrationForDeviceType(thietBi.tenLoai)),
                                    contentDescription = null,
                                    modifier = Modifier.size(56.dp), // icon kích thước cố định
                                    contentScale = ContentScale.Fit
                                )
                            }


                            // Nội dung bên dưới
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Loại thiết bị
                                    Text(
                                        text = thietBi.tenLoai,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Chấm tròn màu theo trạng thái
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(TrangThaiThietBiColor.getColor(thietBi.trangThai))
                                    )
                                }




                                Text(
                                    text = thietBi.tenThietBi,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "P.${thietBi.tenPhong} - T.${thietBi.tenTang} - D.${thietBi.tenDay}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                            }
                        }
                    }
                }
            }

        }
    }
}

//@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
//@Composable
//fun FilterSection(
//    modifier: Modifier = Modifier,
//    viewModel: AdminDeviceListViewModel,
//    thietBiList: List<ThietBiWithDetails>
//) {
//    val scope = rememberCoroutineScope()
//    val bottomSheetState = rememberModalBottomSheetState()
//    var currentFilterKey by remember { mutableStateOf<String?>(null) }
//
//    val filterMap = mapOf(
//        "Dãy" to thietBiList.map { it.tenDay }.distinct(),
//        "Tầng" to thietBiList.map { it.tenTang }.distinct(),
//        "Phòng" to thietBiList.map { it.tenPhong }.distinct(),
//        "Trạng thái" to TrangThaiThietBi.ALL,
//        "Loại Thiết Bị" to thietBiList.map { it.tenLoai }.distinct()
//    )
//
//    val selectedMap = mapOf(
//        "Dãy" to viewModel.selectedDay.collectAsState().value,
//        "Tầng" to viewModel.selectedTang.collectAsState().value,
//        "Phòng" to viewModel.selectedPhong.collectAsState().value,
//        "Trạng thái" to viewModel.selectedTrangThai.collectAsState().value,
//        "Loại Thiết Bị" to viewModel.selectedLoaiThietBi.collectAsState().value
//    )
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 8.dp, ),
//            tonalElevation = 2.dp,
//            color = MaterialTheme.colorScheme.surfaceContainerHigh,
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(end = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Chip filters
//                FlowRow(
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(horizontal = 16.dp , vertical = 4.dp),
//                    mainAxisSpacing = 8.dp,
//                    crossAxisSpacing = 0.dp
//                ) {
//                    filterMap.forEach { (key, _) ->
//                        val selected = selectedMap[key]
//                        AssistChip(
//                            onClick = {
//                                currentFilterKey = key
//                                scope.launch { bottomSheetState.show() }
//                            },
//                            label = {
//                                Text(
//                                    text = if (!selected.isNullOrBlank()) "$key: $selected" else key,
//                                    style = MaterialTheme.typography.labelMedium,
//                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis // <-- dấu ba chấm nếu text dài
//                                )
//                            },
//                            colors = AssistChipDefaults.assistChipColors(
//                                containerColor = MaterialTheme.colorScheme.surfaceVariant
//                            ),
//                            modifier = Modifier.widthIn(min = 80.dp, max = 160.dp) // giới hạn width tránh bể layout
//                        )
//
//                    }
//                }
//
//                // Spacer nhỏ
//                Spacer(modifier = Modifier.width(4.dp))
//
//                // Divider dọc để ngăn cách
//                Box(
//                    modifier = Modifier
//                        .height(48.dp)
//                        .width(1.dp)
//                        .background(MaterialTheme.colorScheme.outlineVariant)
//                )
//
//                // Spacer giữa divider và icon
//                Spacer(modifier = Modifier.width(8.dp))
//
//                // Icon xóa lọc
//                Surface(
//                    shape = MaterialTheme.shapes.small,
//                    color = MaterialTheme.colorScheme.surfaceVariant,
//                    tonalElevation = 2.dp,
//                    shadowElevation = 4.dp,
//                    modifier = Modifier
//                        .height(48.dp)
//                        .width(56.dp) // tăng ngang
//                ) {
//                    IconButton(
//                        onClick = { viewModel.resetFilters() },
//                        modifier = Modifier.fillMaxSize() // chiếm toàn bộ vùng Surface
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.clear_filter),
//                            contentDescription = "Xóa lọc",
//                            tint = MaterialTheme.colorScheme.primary,
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
//                }
//
//            }
//
//        }
//
//
//
//
//
//
//        // BottomSheet như cũ
//        currentFilterKey?.let { key ->
//            val items = filterMap[key] ?: emptyList()
//            ModalBottomSheet(
//                onDismissRequest = { currentFilterKey = null },
//                sheetState = bottomSheetState,
//                containerColor = MaterialTheme.colorScheme.surface
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        "Chọn $key",
//                        style = MaterialTheme.typography.titleMedium,
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//
//                    items.forEach { item ->
//                        ListItem(
//                            headlineContent = { Text(item, color = MaterialTheme.colorScheme.onSurfaceVariant)
//                            },
//                            modifier = Modifier.clickable {
//                                when (key) {
//                                    "Dãy" -> viewModel.setDayFilter(item)
//                                    "Tầng" -> viewModel.setTangFilter(item)
//                                    "Phòng" -> viewModel.setPhongFilter(item)
//                                    "Trạng thái" -> viewModel.setTrangThaiFilter(item)
//                                    "Loại Thiết Bị" -> viewModel.setLoaiThietBiFilter(item)
//                                }
//                                currentFilterKey = null
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    modifier: Modifier = Modifier,
    viewModel: AdminDeviceListViewModel,
    thietBiList: List<ThietBiWithDetails>
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()
    var currentFilterKey by remember { mutableStateOf<String?>(null) }

    val filterMap = mapOf(
        "Dãy" to thietBiList.map { it.tenDay }.distinct(),
        "Tầng" to thietBiList.map { it.tenTang }.distinct(),
        "Phòng" to thietBiList.map { it.tenPhong }.distinct(),
        "Trạng thái" to TrangThaiThietBi.ALL,
        "Loại Thiết Bị" to thietBiList.map { it.tenLoai }.distinct()
    )

    val selectedMap = mapOf(
        "Dãy" to viewModel.selectedDay.collectAsState().value,
        "Tầng" to viewModel.selectedTang.collectAsState().value,
        "Phòng" to viewModel.selectedPhong.collectAsState().value,
        "Trạng thái" to viewModel.selectedTrangThai.collectAsState().value,
        "Loại Thiết Bị" to viewModel.selectedLoaiThietBi.collectAsState().value
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 8.dp)
                    .padding(start = 16.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                // Chip filter scroll ngang
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
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .widthIn(min = 80.dp, max = 160.dp)
                                .height(40.dp)
                        )
                    }
                }

                // Divider dọc ngăn cách chip và nút xóa
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Nút xóa lọc
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { viewModel.resetFilters() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.clear_filter),
                            contentDescription = "Xóa lọc",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }



        // Bottom Sheet như cũ
        currentFilterKey?.let { key ->
            val items = filterMap[key] ?: emptyList()
            ModalBottomSheet(
                onDismissRequest = { currentFilterKey = null },
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Chọn $key",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    items.forEach { item ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    item,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier.clickable {
                                when (key) {
                                    "Dãy" -> viewModel.setDayFilter(item)
                                    "Tầng" -> viewModel.setTangFilter(item)
                                    "Phòng" -> viewModel.setPhongFilter(item)
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


fun getIllustrationForDeviceType(type: String): Int {
    return when (type.lowercase()) {
        "điều hòa" -> R.drawable.airconditioner

        else -> R.drawable.ic_device_placeholder
    }
}
