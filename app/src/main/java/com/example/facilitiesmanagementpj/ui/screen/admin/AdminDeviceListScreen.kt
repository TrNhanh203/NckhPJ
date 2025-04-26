package com.example.facilitiesmanagementpj.ui.screen.admin

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter

import com.example.facilitiesmanagementpj.data.entity.DonVi
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBiColor


@Composable
fun AdminDeviceListScreen(
    navController: NavController,
    viewModel: AdminDeviceListViewModel = hiltViewModel()
) {

    val thietBiList by viewModel.filteredThietBiList.collectAsState()
    val donViList by viewModel.donViList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setSelectedDonViId(null)
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
            val selectedDonViId by viewModel.selectedDonViId.collectAsState()
            DonViFilterBar(
                selectedDonViId = selectedDonViId,
                donViList = donViList,
                onDonViSelected = { viewModel.setSelectedDonViId(it) }
            )

            FilterSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                viewModel = viewModel,
                thietBiList = thietBiList
            )
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
                            color = MaterialTheme.colorScheme.primary
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonViFilterBar(
    selectedDonViId: Int?,
    donViList: List<DonVi>,
    onDonViSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    val selectedDonViName = remember(selectedDonViId) {
        donViList.firstOrNull { it.id == selectedDonViId }?.tenDonVi ?: "Tất cả"
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow, // nhẹ hơn trắng
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Text giới hạn chiều ngang
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đơn vị:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(6.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = selectedDonViName,
                        modifier = Modifier
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                            .widthIn(max = 200.dp), // 👈 chống tràn
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }



            Spacer(modifier = Modifier.width(8.dp))

            // Nút mở sheet với chiều rộng vừa phải
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = 1.dp,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { showBottomSheet = true }
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dropdown),
                        contentDescription = "Dropdown",
                        modifier = Modifier.size(18.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                }
            }
        }

        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        // BottomSheet chọn đơn vị
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()) // ⚠️ scroll tại đây
                ) {
                    Text("Chọn đơn vị", style = MaterialTheme.typography.titleMedium)

                    ListItem(
                        headlineContent = { Text("Tất cả đơn vị") },
                        modifier = Modifier.clickable {
                            onDonViSelected(null)
                            showBottomSheet = false
                        }
                    )

                    donViList.forEach { donVi ->
                        ListItem(
                            headlineContent = { Text(donVi.tenDonVi) },
                            modifier = Modifier.clickable {
                                onDonViSelected(donVi.id)
                                showBottomSheet = false
                            }
                        )
                    }
                }
            }
        }
    }
}



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
        Divider(
            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
            thickness = 1.dp
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 2.dp)
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
        Divider(
            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
            thickness = 1.dp
        )



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
                        .verticalScroll(rememberScrollState()) // ⚠️ scroll tại đây
                ) {
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
