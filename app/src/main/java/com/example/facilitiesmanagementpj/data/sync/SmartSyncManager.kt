import android.content.Context
import android.util.Log
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit
import com.example.facilitiesmanagementpj.data.dao.SyncMetadataDao

class SmartSyncManager(private val syncMetadataDao: SyncMetadataDao) {


    suspend fun <T> smartSync(
        name: String,
        syncService: BaseFirestoreSyncService<T>
    ): Int {
        return withContext(Dispatchers.IO) {
            try {
                val lastSync = syncMetadataDao.getLastSyncTime(name) ?: 0L
                Log.d("SMART_SYNC", "▶️ Sync $name từ $lastSync")
                val items = syncService.syncUpdatedOnly(lastSync)
                Log.d("SMART_SYNC", "✅ Đã sync $name: ${items.size} bản ghi mới")
                syncMetadataDao.updateSyncTime(name, System.currentTimeMillis())
                items.size
            } catch (e: Exception) {
                Log.e("SMART_SYNC", "❌ Lỗi khi sync $name", e)
                0
            }
        }
    }

    suspend fun syncSmartAll(
        services: Map<String, BaseFirestoreSyncService<*>>
    ): Map<String, Int> {
        val results = mutableMapOf<String, Int>()
        for ((name, service) in services) {
            val count = withContext(Dispatchers.IO) {
                try {
                    val lastSync = syncMetadataDao.getLastSyncTime(name) ?: 0L
                    Log.d("SMART_SYNC", "▶️ Sync $name từ $lastSync")
                    val items = service.syncUpdatedOnly(lastSync)
                    Log.d("SMART_SYNC", "✅ Đã sync $name: ${items.size} bản ghi mới")
                    syncMetadataDao.insertOrUpdate(SyncMetadata(collectionName = name, lastSyncTime = System.currentTimeMillis()))
                    items.size
                } catch (e: Exception) {
                    Log.e("SMART_SYNC", "❌ Lỗi khi sync $name", e)
                    0
                }
            }
            results[name] = count
        }
        return results
    }

    suspend fun syncSmartAll(
        baiVietSync: BaseFirestoreSyncService<BaiViet>,
        thongBaoSync: BaseFirestoreSyncService<ThongBao>,
        yeuCauSync: BaseFirestoreSyncService<YeuCau>,
        phanCongSync: BaseFirestoreSyncService<PhanCong>,
        phanCongKtvSync: BaseFirestoreSyncService<PhanCongKtv>,
        taiKhoanSync: BaseFirestoreSyncService<TaiKhoan>,
        kyThuatVienSync: BaseFirestoreSyncService<KyThuatVien>,
        thietBiSync: BaseFirestoreSyncService<ThietBi>,
        phongSync: BaseFirestoreSyncService<Phong>,
        tangSync: BaseFirestoreSyncService<Tang>,
        daySync: BaseFirestoreSyncService<Day>,
        loaiPhongSync: BaseFirestoreSyncService<LoaiPhong>,
        loaiThietBiSync: BaseFirestoreSyncService<LoaiThietBi>,
        chuyenMonSync: BaseFirestoreSyncService<ChuyenMon>,
        chuyenMonKtvSync: BaseFirestoreSyncService<ChuyenMonKtv>,
        danhGiaKtvSync: BaseFirestoreSyncService<DanhGiaKTV>,
        bienBanYeuCauSync: BaseFirestoreSyncService<BienBanYeuCau>,
        chiTietYeuCauSync: BaseFirestoreSyncService<ChiTietYeuCau>,
        anhMinhChungBaoCaoSync: BaseFirestoreSyncService<AnhMinhChungBaoCao>,
        anhMinhChungLamViecSync: BaseFirestoreSyncService<AnhMinhChungLamViec>,
        donViSync: BaseFirestoreSyncService<DonVi>,
        vaiTroSync: BaseFirestoreSyncService<VaiTro>
    ) {
//        syncSmartAll(
//            linkedMapOf( // old là mapof
//                "bai_viet" to baiVietSync,
//                "thong_bao" to thongBaoSync,
//                "loai_phong" to loaiPhongSync,
//                "loai_thiet_bi" to loaiThietBiSync,
//                "chuyen_mon" to chuyenMonSync,
//                "yeu_cau" to yeuCauSync,
//                "phan_cong" to phanCongSync,
//                "phan_cong_ktv" to phanCongKtvSync,
//                "don_vi" to donViSync,
//                "vai_tro" to vaiTroSync,
//                "tai_khoan" to taiKhoanSync,
//                "ky_thuat_vien" to kyThuatVienSync,
//                "thiet_bi" to thietBiSync,
//                "day" to daySync,
//                "tang" to tangSync,
//                "phong" to phongSync,
//                "chuyen_mon_ktv" to chuyenMonKtvSync,
//                "danh_gia_ktv" to danhGiaKtvSync,
//                "bien_ban_yeu_cau" to bienBanYeuCauSync,
//                "chi_tiet_yeu_cau" to chiTietYeuCauSync,
//                "anh_minh_chung_bao_cao" to anhMinhChungBaoCaoSync,
//                "anh_minh_chung_lam_viec" to anhMinhChungLamViecSync,
//
//            )
//        )
        syncSmartAll(
            linkedMapOf(
                "bai_viet" to baiVietSync,
                "thong_bao" to thongBaoSync,
                "loai_phong" to loaiPhongSync,
                "loai_thiet_bi" to loaiThietBiSync,
                "chuyen_mon" to chuyenMonSync,

                "don_vi" to donViSync,
                "vai_tro" to vaiTroSync,
                "tai_khoan" to taiKhoanSync,
                "ky_thuat_vien" to kyThuatVienSync,
                "chuyen_mon_ktv" to chuyenMonKtvSync,
                "danh_gia_ktv" to danhGiaKtvSync,

                "day" to daySync,
                "tang" to tangSync,
                "phong" to phongSync,
                "thiet_bi" to thietBiSync,

                "yeu_cau" to yeuCauSync,
                "chi_tiet_yeu_cau" to chiTietYeuCauSync,
                "phan_cong" to phanCongSync,
                "phan_cong_ktv" to phanCongKtvSync,

                "bien_ban_yeu_cau" to bienBanYeuCauSync,
                "anh_minh_chung_bao_cao" to anhMinhChungBaoCaoSync,
                "anh_minh_chung_lam_viec" to anhMinhChungLamViecSync,
            )
        )

    }
}
