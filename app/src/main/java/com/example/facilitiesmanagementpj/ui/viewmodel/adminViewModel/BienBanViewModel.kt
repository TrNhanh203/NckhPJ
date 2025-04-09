package com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel


import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.facilitiesmanagementpj.data.repository.*
import com.example.facilitiesmanagementpj.data.utils.TrangThaiChungCuaPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungBaoCao
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungLamViec


@HiltViewModel
class BienBanViewModel @Inject constructor(
    private val yeuCauRepo: YeuCauRepository,
    private val chiTietRepo: ChiTietYeuCauRepository,
    private val thietBiRepo: ThietBiRepository,
    private val phanCongRepo: PhanCongRepository,
    private val phanCongKtvRepo: PhanCongKtvRepository,
    private val taiKhoanRepo: TaiKhoanRepository,
    private val donViRepo: DonViRepository,
    private val anhBaoCaoRepo: AnhMinhChungBaoCaoRepository,
    private val anhLamViecRepo: AnhMinhChungLamViecRepository,
    private val phongRepository: PhongRepository,
    private val tangRepository: TangRepository,
    private val dayRepository: DayRepository
) : ViewModel() {

    data class ThietBiDaXuLy(
        val chiTietYeuCauId: Int,
        val tenThietBi: String,
        val viTri: String,
        val noiDung: String
    )

    data class ThongTinNghiemThu(
        val tenBenA: String,
        val donViBenA: String,
        val tenBenB: String,
        val danhSachThietBi: List<ThietBiDaXuLy>
    )

    var thongTinNghiemThu by mutableStateOf<ThongTinNghiemThu?>(null)
        private set

    val anhTheoChiTietMap = mutableStateMapOf<Int, Pair<List<AnhMinhChungBaoCao>, List<AnhMinhChungLamViec>>>()

    fun loadBienBan(yeuCauId: Int, nguoiXacNhanId: Int) {
        viewModelScope.launch {
            val yeuCau = yeuCauRepo.getById(yeuCauId) ?: return@launch
            val taiKhoanA = taiKhoanRepo.getTkById(yeuCau.taiKhoanId)
            val donViA = taiKhoanA?.donViId?.let { donViRepo.getById(it) }
            val taiKhoanB = taiKhoanRepo.getTkById(nguoiXacNhanId)

            val chiTietList = chiTietRepo.getAllByYeuCauId(yeuCauId)
            val danhSach = mutableListOf<ThietBiDaXuLy>()

            for (chiTiet in chiTietList) {
                val thietBi = thietBiRepo.getById(chiTiet.thietBiId) ?: continue
                val phanCong = phanCongRepo.getPhanCongByChiTietYeuCau(chiTiet.id) ?: continue
                val phong = phongRepository.getById(thietBi.phongId ?: return@launch)
                val tang = tangRepository.getById(phong?.tangId ?: return@launch)
                val day = dayRepository.getById(tang?.dayId ?: return@launch)

                danhSach.add(
                    ThietBiDaXuLy(
                        chiTietYeuCauId = chiTiet.id,
                        tenThietBi = thietBi.tenThietBi,
                        viTri = "${day?.tenDay} - Tầng ${tang.tenTang} - Phòng ${phong.tenPhong}",
                        noiDung = phanCong.ghiChu ?: chiTiet.moTa
                    )
                )
            }

            thongTinNghiemThu = ThongTinNghiemThu(
                tenBenA = taiKhoanA?.hoTen ?: "Không rõ",
                donViBenA = donViA?.tenDonVi ?: "Không rõ",
                tenBenB = taiKhoanB?.hoTen ?: "Không rõ",
                danhSachThietBi = danhSach
            )
        }
    }

//    fun loadAnhTheoChiTiet(chiTietId: Int) {
//        viewModelScope.launch {
//            if (anhTheoChiTietMap.containsKey(chiTietId)) return@launch
//
//            val anhBaoCao = anhBaoCaoRepo.getByChiTietId(chiTietId)
//
//            val phanCong = phanCongRepo.getPhanCongByChiTietYeuCau(chiTietId) ?: return@launch
//            val phanCongKtvs = phanCongKtvRepo.getAllByPhanCongId(phanCong.id)
//
//            val anhLamViec = phanCongKtvs.flatMap { ktv ->
//                anhLamViecRepo.getByPhanCongKtvId(ktv.id)
//            }.filter {
//                it.loaiAnh in listOf("CHECKIN", "MINH_CHUNG", "CHECKOUT")
//            }.sortedBy { it.thoiGianTaiLen }
//
//            anhTheoChiTietMap[chiTietId] = Pair(anhBaoCao, anhLamViec)
//
//        }
//    }
    // giữ nguyên các phần khác trong ViewModel

    // Lưu map ktvId -> tên và mô tả phân công
    val tenKtvMap = mutableStateMapOf<Int, String>()
    val moTaKtvMap = mutableStateMapOf<Int, String>()

    fun loadAnhTheoChiTiet(chiTietId: Int) {
        viewModelScope.launch {
            if (anhTheoChiTietMap.containsKey(chiTietId)) return@launch

            val anhBaoCao = anhBaoCaoRepo.getByChiTietId(chiTietId)

            val phanCong = phanCongRepo.getPhanCongByChiTietYeuCau(chiTietId) ?: return@launch
            val phanCongKtvs = phanCongKtvRepo.getAllByPhanCongId(phanCong.id)

            // Cập nhật map tên và mô tả phân công kỹ thuật viên
            for (pc in phanCongKtvs) {
                val taiKhoan = taiKhoanRepo.getTkById(pc.id)
                tenKtvMap[pc.id] = taiKhoan?.hoTen ?: "Ẩn danh"
                moTaKtvMap[pc.id] = pc.moTaCongViec ?: "Không có mô tả"
            }

            val anhLamViec = phanCongKtvs.flatMap { ktv ->
                anhLamViecRepo.getByPhanCongKtvId(ktv.id)
            }.filter {
                it.loaiAnh in listOf("CHECKIN", "MINH_CHUNG", "CHECKOUT")
            }.sortedBy { it.thoiGianTaiLen }

            anhTheoChiTietMap[chiTietId] = Pair(anhBaoCao, anhLamViec)
        }
    }

    fun getKtvNameByPhanCongKtvId(id: Int): String {
        return tenKtvMap[id] ?: "Ẩn danh"
    }

    fun getMoTaByPhanCongKtvId(id: Int): String {
        return moTaKtvMap[id] ?: "Không có mô tả"
    }

}
