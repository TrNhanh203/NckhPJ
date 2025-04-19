package com.example.facilitiesmanagementpj.data.sync

import SmartSyncManager
import android.content.Context
import com.example.facilitiesmanagementpj.data.dao.SyncMetadataDao
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungBaoCao
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungLamViec
import com.example.facilitiesmanagementpj.data.entity.BaiViet
import com.example.facilitiesmanagementpj.data.entity.BienBanYeuCau
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCau
import com.example.facilitiesmanagementpj.data.entity.ChuyenMon
import com.example.facilitiesmanagementpj.data.entity.ChuyenMonKtv
import com.example.facilitiesmanagementpj.data.entity.DanhGiaKTV
import com.example.facilitiesmanagementpj.data.entity.Day
import com.example.facilitiesmanagementpj.data.entity.DonVi
import com.example.facilitiesmanagementpj.data.entity.KyThuatVien
import com.example.facilitiesmanagementpj.data.entity.LoaiPhong
import com.example.facilitiesmanagementpj.data.entity.LoaiThietBi
import com.example.facilitiesmanagementpj.data.entity.PhanCong
import com.example.facilitiesmanagementpj.data.entity.PhanCongKtv
import com.example.facilitiesmanagementpj.data.entity.Phong
import com.example.facilitiesmanagementpj.data.entity.TaiKhoan
import com.example.facilitiesmanagementpj.data.entity.Tang
import com.example.facilitiesmanagementpj.data.entity.ThietBi
import com.example.facilitiesmanagementpj.data.entity.ThongBao
import com.example.facilitiesmanagementpj.data.entity.VaiTro
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreSyncCoordinator @Inject constructor(
    private val syncMetadataDao: SyncMetadataDao,
    private val baiVietSync: BaseFirestoreSyncService<BaiViet>,
    private val thongBaoSync: BaseFirestoreSyncService<ThongBao>,
    private val yeuCauSync: BaseFirestoreSyncService<YeuCau>,
    private val phanCongSync: BaseFirestoreSyncService<PhanCong>,
    private val phanCongKtvSync: BaseFirestoreSyncService<PhanCongKtv>,
    private val taiKhoanSync: BaseFirestoreSyncService<TaiKhoan>,
    private val kyThuatVienSync: BaseFirestoreSyncService<KyThuatVien>,
    private val thietBiSync: BaseFirestoreSyncService<ThietBi>,
    private val phongSync: BaseFirestoreSyncService<Phong>,
    private val tangSync: BaseFirestoreSyncService<Tang>,
    private val daySync: BaseFirestoreSyncService<Day>,
    private val loaiPhongSync: BaseFirestoreSyncService<LoaiPhong>,
    private val loaiThietBiSync: BaseFirestoreSyncService<LoaiThietBi>,
    private val chuyenMonSync: BaseFirestoreSyncService<ChuyenMon>,
    private val chuyenMonKtvSync: BaseFirestoreSyncService<ChuyenMonKtv>,
    private val danhGiaKtvSync: BaseFirestoreSyncService<DanhGiaKTV>,
    private val bienBanYeuCauSync: BaseFirestoreSyncService<BienBanYeuCau>,
    private val chiTietYeuCauSync: BaseFirestoreSyncService<ChiTietYeuCau>,
    private val anhMinhChungBaoCaoSync: BaseFirestoreSyncService<AnhMinhChungBaoCao>,
    private val anhMinhChungLamViecSync: BaseFirestoreSyncService<AnhMinhChungLamViec>,
    private val donViSync: BaseFirestoreSyncService<DonVi>,
    private val vaiTroSync: BaseFirestoreSyncService<VaiTro>
) {
//    suspend fun syncAll() {
//        safeSync<BaiViet>("bai_viet") { baiVietSync.syncFromCloudToRoom() }
//        safeSync<ThongBao>("thong_bao") { thongBaoSync.syncFromCloudToRoom() }
//        safeSync<YeuCau>("yeu_cau") { yeuCauSync.syncFromCloudToRoom() }
//        safeSync<PhanCong>("phan_cong") { phanCongSync.syncFromCloudToRoom() }
//        safeSync<PhanCongKtv>("phan_cong_ktv") { phanCongKtvSync.syncFromCloudToRoom() }
//        safeSync<TaiKhoan>("tai_khoan") { taiKhoanSync.syncFromCloudToRoom() }
//        safeSync<KyThuatVien>("ky_thuat_vien") { kyThuatVienSync.syncFromCloudToRoom() }
//        safeSync<ThietBi>("thiet_bi") { thietBiSync.syncFromCloudToRoom() }
//        safeSync<Phong>("phong") { phongSync.syncFromCloudToRoom() }
//        safeSync<Tang>("tang") { tangSync.syncFromCloudToRoom() }
//        safeSync<Day>("day") { daySync.syncFromCloudToRoom() }
//        safeSync<LoaiPhong>("loai_phong") { loaiPhongSync.syncFromCloudToRoom() }
//        safeSync<LoaiThietBi>("loai_thiet_bi") { loaiThietBiSync.syncFromCloudToRoom() }
//        safeSync<ChuyenMon>("chuyen_mon") { chuyenMonSync.syncFromCloudToRoom() }
//        safeSync<ChuyenMonKtv>("chuyen_mon_ktv") { chuyenMonKtvSync.syncFromCloudToRoom() }
//        safeSync<DanhGiaKTV>("danh_gia_ktv") { danhGiaKtvSync.syncFromCloudToRoom() }
//        safeSync<BienBanYeuCau>("bien_ban_yeu_cau") { bienBanYeuCauSync.syncFromCloudToRoom() }
//        safeSync<ChiTietYeuCau>("chi_tiet_yeu_cau") { chiTietYeuCauSync.syncFromCloudToRoom() }
//        safeSync<AnhMinhChungBaoCao>("anh_minh_chung_bao_cao") { anhMinhChungBaoCaoSync.syncFromCloudToRoom() }
//        safeSync<AnhMinhChungLamViec>("anh_minh_chung_lam_viec") { anhMinhChungLamViecSync.syncFromCloudToRoom() }
//        safeSync<DonVi>("don_vi") { donViSync.syncFromCloudToRoom() }
//        safeSync<VaiTro>("vai_tro") { vaiTroSync.syncFromCloudToRoom() }
//    }
private val smartSyncManager = SmartSyncManager(syncMetadataDao)
suspend fun syncSmartAll() {
    smartSyncManager.syncSmartAll(
        baiVietSync,
        thongBaoSync,
        yeuCauSync,
        phanCongSync,
        phanCongKtvSync,
        taiKhoanSync,
        kyThuatVienSync,
        thietBiSync,
        phongSync,
        tangSync,
        daySync,
        loaiPhongSync,
        loaiThietBiSync,
        chuyenMonSync,
        chuyenMonKtvSync,
        danhGiaKtvSync,
        bienBanYeuCauSync,
        chiTietYeuCauSync,
        anhMinhChungBaoCaoSync,
        anhMinhChungLamViecSync,
        donViSync,
        vaiTroSync
    )
}


}