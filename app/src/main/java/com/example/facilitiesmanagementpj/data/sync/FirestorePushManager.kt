package com.example.facilitiesmanagementpj.data.sync

import android.util.Log
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FirestorePushManager @Inject constructor(
    private val syncMetadataDao: SyncMetadataDao,
    private val baiVietDao: BaiVietDao,
    private val thongBaoDao: ThongBaoDao,
    private val yeuCauDao: YeuCauDao,
    private val phanCongDao: PhanCongDao,
    private val phanCongKtvDao: PhanCongKtvDao,
    private val taiKhoanDao: TaiKhoanDao,
    private val kyThuatVienDao: KyThuatVienDao,
    private val thietBiDao: ThietBiDao,
    private val phongDao: PhongDao,
    private val tangDao: TangDao,
    private val dayDao: DayDao,
    private val loaiPhongDao: LoaiPhongDao,
    private val loaiThietBiDao: LoaiThietBiDao,
    private val chuyenMonDao: ChuyenMonDao,
    private val chuyenMonKtvDao: ChuyenMonKtvDao,
    private val danhGiaKtvDao: DanhGiaKTVDao,
    private val bienBanYeuCauDao: BienBanYeuCauDao,
    private val chiTietYeuCauDao: ChiTietYeuCauDao,
    private val anhMinhChungBaoCaoDao: AnhMinhChungBaoCaoDao,
    private val anhMinhChungLamViecDao: AnhMinhChungLamViecDao,
    private val donViDao: DonViDao,
    private val vaiTroDao: VaiTroDao,

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
    private val vaiTroSync: BaseFirestoreSyncService<VaiTro>,
) {
    suspend fun pushAllToCloud() {
        Log.d("PUSH_ALL", "🚀 Bắt đầu push toàn bộ dữ liệu lên Firestore")

        val startTime = System.currentTimeMillis()

        val pushList = listOf(
            "BaiViet" to baiVietDao.getAll().first(),
            "ThongBao" to thongBaoDao.getAll().first(),
            "YeuCau" to yeuCauDao.getAll().first(),
            "PhanCong" to phanCongDao.getAll().first(),
            "PhanCongKtv" to phanCongKtvDao.getAll().first(),
            "TaiKhoan" to taiKhoanDao.getAll().first(),
            "KyThuatVien" to kyThuatVienDao.getAll().first(),
            "ThietBi" to thietBiDao.getAll().first(),
            "Phong" to phongDao.getAll().first(),
            "Tang" to tangDao.getAll().first(),
            "Day" to dayDao.getAll().first(),
            "LoaiPhong" to loaiPhongDao.getAll().first(),
            "LoaiThietBi" to loaiThietBiDao.getAll().first(),
            "ChuyenMon" to chuyenMonDao.getAll().first(),
            "ChuyenMonKtv" to chuyenMonKtvDao.getAll().first(),
            "DanhGiaKTV" to danhGiaKtvDao.getAll().first(),
            "BienBanYeuCau" to bienBanYeuCauDao.getAll().first(),
            "ChiTietYeuCau" to chiTietYeuCauDao.getAll().first(),
            "AnhMinhChungBaoCao" to anhMinhChungBaoCaoDao.getAll().first(),
            "AnhMinhChungLamViec" to anhMinhChungLamViecDao.getAll().first(),
            "DonVi" to donViDao.getAll().first(),
            "VaiTro" to vaiTroDao.getAll().first()
        )

        for ((name, list) in pushList) {
            for (item in list) {
                when (item) {
                    is BaiViet -> baiVietSync.pushToCloud(item, item.id.toString())
                    is ThongBao -> thongBaoSync.pushToCloud(item, item.id)
                    is YeuCau -> yeuCauSync.pushToCloud(item, item.id.toString())
                    is PhanCong -> phanCongSync.pushToCloud(item, item.id.toString())
                    is PhanCongKtv -> phanCongKtvSync.pushToCloud(item, item.id.toString())
                    is TaiKhoan -> taiKhoanSync.pushToCloud(item, item.id.toString())
                    is KyThuatVien -> kyThuatVienSync.pushToCloud(item, item.id.toString())
                    is ThietBi -> thietBiSync.pushToCloud(item, item.id.toString())
                    is Phong -> phongSync.pushToCloud(item, item.id.toString())
                    is Tang -> tangSync.pushToCloud(item, item.id.toString())
                    is Day -> daySync.pushToCloud(item, item.id.toString())
                    is LoaiPhong -> loaiPhongSync.pushToCloud(item, item.id.toString())
                    is LoaiThietBi -> loaiThietBiSync.pushToCloud(item, item.id.toString())
                    is ChuyenMon -> chuyenMonSync.pushToCloud(item, item.id.toString())
                    is ChuyenMonKtv -> chuyenMonKtvSync.pushToCloud(item, item.id.toString())
                    is DanhGiaKTV -> danhGiaKtvSync.pushToCloud(item, item.id.toString())
                    is BienBanYeuCau -> bienBanYeuCauSync.pushToCloud(item, item.id.toString())
                    is ChiTietYeuCau -> chiTietYeuCauSync.pushToCloud(item, item.id.toString())
                    is AnhMinhChungBaoCao -> anhMinhChungBaoCaoSync.pushToCloud(item, item.id.toString())
                    is AnhMinhChungLamViec -> anhMinhChungLamViecSync.pushToCloud(item, item.id.toString())
                    is DonVi -> donViSync.pushToCloud(item, item.id.toString())
                    is VaiTro -> vaiTroSync.pushToCloud(item, item.id.toString())
                }
            }
            Log.d("PUSH_ALL", "✅ Đã push ${list.size} bản ghi cho bảng $name")
        }

        val totalTime = System.currentTimeMillis() - startTime
        Log.d("PUSH_ALL", "🏁 Hoàn tất push toàn bộ! Thời gian: ${totalTime}ms")
    }


    // Hàm helper: Push và cập nhật sync time
    private suspend fun pushAndUpdate(collectionName: String, block: suspend () -> Unit) {
        block()
        syncMetadataDao.updateSyncTime(collectionName, System.currentTimeMillis())
    }


}
