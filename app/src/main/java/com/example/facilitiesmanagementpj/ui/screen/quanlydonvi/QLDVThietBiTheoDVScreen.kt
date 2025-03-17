package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBi
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVThietBiViewModel
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState

@Composable
fun QLDVThietBiTheoDVScreen(
    navController: NavController,
    isSelectMode: Boolean = false,
    yeuCauId: Int? = null,
    viewModel: QLDVThietBiViewModel = hiltViewModel()
) {
    val thietBiList by viewModel.filteredThietBiList.collectAsState()
    val donViId = SessionManager.currentUser?.donViId ?: 0

    LaunchedEffect(Unit) {
        viewModel.loadThietBiList(donViId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            if (isSelectMode) "Chọn thiết bị cho yêu cầu" else "Danh sách thiết bị",
            style = MaterialTheme.typography.headlineMedium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropdownMenuFilter(
                label = "Dãy",
                items = thietBiList.map { it.tenDay }.distinct(),
                selected = viewModel.selectedDay.collectAsState().value,
                onSelectedChange = { viewModel.setDayFilter(it) }
            )

            DropdownMenuFilter(
                label = "Tầng",
                items = thietBiList.map { it.tenTang }.distinct(),
                selected = viewModel.selectedTang.collectAsState().value,
                onSelectedChange = { viewModel.setTangFilter(it) }
            )

            DropdownMenuFilter(
                label = "Phòng",
                items = thietBiList.map { it.tenPhong }.distinct(),
                selected = viewModel.selectedPhong.collectAsState().value,
                onSelectedChange = { viewModel.setPhongFilter(it) }
            )

            DropdownMenuFilter(
                label = "Trạng thái",
                items = listOf("Bình thường", "Hỏng", "Đang bảo trì"),
                selected = viewModel.selectedTrangThai.collectAsState().value,
                onSelectedChange = { viewModel.setTrangThaiFilter(it) }
            )

            DropdownMenuFilter(
                label = "Loại Thiết Bị",
                items = thietBiList.map { it.tenLoai }.distinct(),
                selected = viewModel.selectedLoaiThietBi.collectAsState().value,
                onSelectedChange = { viewModel.setLoaiThietBiFilter(it) }
            )

            Button(onClick = {
                viewModel.resetFilters()
            }) {
                Text("Reset")
            }
        }

        LazyColumn {
            items(thietBiList) { thietBi ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            if (isSelectMode && yeuCauId != null) {
                                navController.navigate(Screen.ThietBiDetail.createRoute(thietBi.id, isEditMode = true, yeuCauId = yeuCauId))
                            } else {
                                navController.navigate(Screen.ThietBiDetail.createRoute(thietBi.id))
                            }
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tên: ${thietBi.tenThietBi}")
                        Text("Loại: ${thietBi.tenLoai}")
                        Text("Phòng: ${thietBi.tenPhong} - Tầng: ${thietBi.tenTang} - Dãy: ${thietBi.tenDay}")
                        Text("Trạng thái: ${thietBi.trangThai}")
                    }
                }
            }
        }
    }
}


//@Composable
//fun QLDVThietBiTheoDVScreen(
//    navController: NavController,
//    isSelectMode: Boolean = false,
//    yeuCauId: Int? = null,
//    viewModel: QLDVThietBiViewModel = hiltViewModel()
//) {
//    val thietBiList by viewModel.filteredThietBiList.collectAsState()
//    val donViId = SessionManager.currentUser?.donViId ?: 0
//
//    LaunchedEffect(Unit) {
//        viewModel.loadThietBiList(donViId)
//    }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        Text(
//            if (isSelectMode) "Chọn thiết bị cho yêu cầu" else "Danh sách thiết bị",
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(3.dp)) {
//            DropdownMenuFilter(
//                label = "Dãy",
//                items = thietBiList.map { it.tenDay }.distinct(),
//                selected = viewModel.selectedDay.collectAsState().value,
//                onSelectedChange = { viewModel.setDayFilter(it) }
//            )
//
//            DropdownMenuFilter(
//                label = "Tầng",
//                items = thietBiList.map { it.tenTang }.distinct(),
//                selected = viewModel.selectedTang.collectAsState().value,
//                onSelectedChange = { viewModel.setTangFilter(it) }
//            )
//
//            DropdownMenuFilter(
//                label = "Phòng",
//                items = thietBiList.map { it.tenPhong }.distinct(),
//                selected = viewModel.selectedPhong.collectAsState().value,
//                onSelectedChange = { viewModel.setPhongFilter(it) }
//            )
//
//            DropdownMenuFilter(
//                label = "Trạng thái",
//                items = listOf("Bình thường", "Hỏng", "Đang bảo trì"),
//                selected = viewModel.selectedTrangThai.collectAsState().value,
//                onSelectedChange = { viewModel.setTrangThaiFilter(it) }
//            )
//
//            DropdownMenuFilter( // ✅ Thêm bộ lọc Loại Thiết Bị
//                label = "Loại Thiết Bị",
//                items = thietBiList.map { it.tenLoai }.distinct(),
//                selected = viewModel.selectedLoaiThietBi.collectAsState().value,
//                onSelectedChange = { viewModel.setLoaiThietBiFilter(it) }
//            )
//        }
//
//        LazyColumn {
//            items(thietBiList) { thietBi ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                        .clickable {
//                            if (isSelectMode && yeuCauId != null) {
//                                //navController.navigate("edit_chi_tiet_bao_cao/$yeuCauId/${thietBi.id}")
//                                navController.navigate(Screen.ThietBiDetail.createRoute(thietBi.id, isEditMode = true, yeuCauId = yeuCauId))
//                            } else {
//                                navController.navigate(Screen.ThietBiDetail.createRoute(thietBi.id))
//
//                            }
//                        }
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Text("Tên: ${thietBi.tenThietBi}")
//                        Text("Loại: ${thietBi.tenLoai}")
//                        Text("Phòng: ${thietBi.tenPhong} - Tầng: ${thietBi.tenTang} - Dãy: ${thietBi.tenDay}")
//                        Text("Trạng thái: ${thietBi.trangThai}")
//                    }
//                }
//
//            }
//        }
//    }
//}


