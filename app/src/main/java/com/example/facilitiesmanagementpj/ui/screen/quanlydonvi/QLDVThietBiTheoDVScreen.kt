package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBi
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVThietBiViewModel
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.navigation.Screen

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

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(3.dp)) {
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

            DropdownMenuFilter( // ✅ Thêm bộ lọc Loại Thiết Bị
                label = "Loại Thiết Bị",
                items = thietBiList.map { it.tenLoai }.distinct(),
                selected = viewModel.selectedLoaiThietBi.collectAsState().value,
                onSelectedChange = { viewModel.setLoaiThietBiFilter(it) }
            )
        }

        LazyColumn {
            items(thietBiList) { thietBi ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            if (isSelectMode && yeuCauId != null) {
                                //navController.navigate("edit_chi_tiet_bao_cao/$yeuCauId/${thietBi.id}")
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
//    isSelectMode: Boolean = false, // ✅ Thêm chế độ chọn thiết bị
//    yeuCauId: Int? = null, // ✅ Nếu chọn thiết bị, cần biết yêu cầu nào
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
//
//
//            DropdownMenuFilter(
//                label = "Trạng thái",
//                items = listOf(TrangThaiThietBi.MOI_TIEP_NHAN,
//                    TrangThaiThietBi.SAN_SANG_SU_DUNG,
//                    TrangThaiThietBi.DANG_SU_DUNG,
//                    TrangThaiThietBi.DANG_BAO_TRI,
//                    TrangThaiThietBi.CHO_BAO_TRI,
//                    TrangThaiThietBi.DANG_BAO_DUONG,
//                    TrangThaiThietBi.CHO_BAO_DUONG,
//                    TrangThaiThietBi.CHO_SUA_CHUA,
//                    TrangThaiThietBi.DANG_SUA_CHUA,
//                    TrangThaiThietBi.HONG,
//                    TrangThaiThietBi.KHONG_KHA_DUNG,
//                    TrangThaiThietBi.DA_NGUNG_SU_DUNG,
//                    TrangThaiThietBi.THANH_LY,
//                    TrangThaiThietBi.CHO_KIEM_DINH,
//                    TrangThaiThietBi.DANG_KIEM_DINH,
//                    TrangThaiThietBi.DANG_THU_NGHIEM,
//                    TrangThaiThietBi.CHO_DUYET,
//                    TrangThaiThietBi.THAT_LAC,
//                    TrangThaiThietBi.DANG_TIM_KIEM),
//                selected = viewModel.selectedTrangThai.collectAsState().value,
//                onSelectedChange = { viewModel.setTrangThaiFilter(it) }
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
//                                navController.navigate("edit_chi_tiet_bao_cao/$yeuCauId/${thietBi.id}")
//                            } else {
//                                navController.navigate("chi_tiet_thiet_bi/${thietBi.id}")
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
//            }
//        }
//    }
//}
//
//
//

