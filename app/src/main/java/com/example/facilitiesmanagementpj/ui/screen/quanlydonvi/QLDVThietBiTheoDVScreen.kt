    package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi


    import android.util.Log
    import androidx.activity.compose.BackHandler
    import androidx.compose.foundation.BorderStroke
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.lazy.grid.items
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.hilt.navigation.compose.hiltViewModel
    import androidx.navigation.NavController
    import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBi
    import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVThietBiViewModel
    import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
    import com.example.facilitiesmanagementpj.ui.navigation.Screen
    import com.google.accompanist.flowlayout.FlowRow
    import androidx.compose.foundation.horizontalScroll
    import androidx.compose.foundation.lazy.grid.GridCells
    import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Clear
    import androidx.compose.material.icons.filled.Devices
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.style.TextOverflow
    import com.example.facilitiesmanagementpj.data.dao.ThietBiWithDetails
    import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBiColor
    import com.example.facilitiesmanagementpj.ui.component.CustomTopAppBar
    import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
    import com.example.facilitiesmanagementpj.ui.screen.admin.FilterSection
    import com.example.facilitiesmanagementpj.ui.screen.admin.getIllustrationForDeviceType
    import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel
    import kotlinx.coroutines.launch

    @Composable
    fun QLDVThietBiTheoDVScreen(
        navController: NavController,
        isSelectMode: Boolean = false,
        yeuCauId: Int? = null,
        viewModel: QLDVThietBiViewModel = hiltViewModel()
    ) {
        val sessionViewModel: SessionViewModel = hiltViewModel()
        val currentUser by sessionViewModel.currentUser.collectAsState()
        val thietBiList by viewModel.filteredThietBiList.collectAsState()
        val selectedThietBiList by viewModel.selectedThietBiList.collectAsState()
        val donViId = currentUser?.donViId ?: 0

        LaunchedEffect(currentUser) {
            viewModel.loadThietBiList(donViId)
            if (yeuCauId != null) {
                viewModel.loadChiTietYeuCau(yeuCauId)
            }
        }

        val handleBackNavigation: () -> Unit = {
            if (yeuCauId != null) {
                navController.previousBackStackEntry?.savedStateHandle?.set("yeuCauId", yeuCauId)
            }
            navController.navigateUp()
        }

        BackHandler { handleBackNavigation() }

        ScaffoldLayout(
            title = if (isSelectMode) "Chọn thiết bị" else "Danh sách thiết bị",
            navController = navController,
            showTopBar = true,
            showBottomBar = false,
            showDrawer = false,
            onBackClick = handleBackNavigation
        ) { modifier ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(modifier)
            ) {

                // Bộ lọc
                FilterSectionQLDV(
                    viewModel = viewModel,
                    thietBiList = thietBiList
                )

                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    // Danh sách thiết bị
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        items(
                            items = thietBiList,
                        ) { thietBi ->
                            val isSelected = selectedThietBiList.contains(thietBi.id)

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.85f) // tỉ lệ card (cao hơn rộng 1 chút)
                                    .clickable {
                                        if (isSelectMode && yeuCauId != null) {
                                            navController.navigate(Screen.ThietBiDetail.createRoute(thietBi.id, true, yeuCauId))
                                        } else {
                                            navController.navigate(Screen.ThietBiDetail.createRoute(thietBi.id))
                                        }
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

                                        if (isSelected) {
                                            Text(
                                                "\u2713 Đã chọn",
                                                color = Color.Red,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }
                }



            }
        }
    }

    @Composable
    fun DeviceCard(
        thietBi: ThietBiWithDetails,
        isSelected: Boolean,
        isSelectMode: Boolean,
        yeuCauId: Int?,
        navController: NavController
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.9f)
                .clickable {
                    if (isSelectMode && yeuCauId != null) {
                        navController.navigate(Screen.ThietBiDetail.createRoute(thietBi.id, true, yeuCauId))
                    } else {
                        navController.navigate(Screen.ThietBiDetail.createRoute(thietBi.id))
                    }
                },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = getIllustrationForDeviceType(thietBi.tenLoai)),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp), // icon kích thước cố định
                        contentScale = ContentScale.Fit
                    )
                }

                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = thietBi.tenLoai,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

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

                    if (isSelected) {
                        Text(
                            "\u2713 Đã chọn",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FilterSectionQLDV(
        viewModel: QLDVThietBiViewModel,
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


    @Composable
    fun FilterDropdownMenu(
        label: String,
        items: List<String>,
        selected: String?,
        onSelectedChange: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selected ?: label)
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                items.forEach { item ->
                    DropdownMenuItem(text = { Text(item) }, onClick = {
                        expanded = false
                        onSelectedChange(item)
                    })
                }
            }
        }
    }
