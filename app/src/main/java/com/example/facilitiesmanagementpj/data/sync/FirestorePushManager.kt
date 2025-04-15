package com.example.facilitiesmanagementpj.data.sync

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FirestorePushManager @Inject constructor(
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
    private val vaiTroSync: BaseFirestoreSyncService<VaiTro>
) {
    suspend fun pushAllToCloud() {
        baiVietDao.getAll().first().forEach { baiVietSync.pushToCloud(it, it.id.toString()) }
        thongBaoDao.getAll().first().forEach { thongBaoSync.pushToCloud(it, it.id) }
        yeuCauDao.getAll().first().forEach { yeuCauSync.pushToCloud(it, it.id.toString()) }
        phanCongDao.getAll().first().forEach { phanCongSync.pushToCloud(it, it.id.toString()) }
        phanCongKtvDao.getAll().first().forEach { phanCongKtvSync.pushToCloud(it, it.id.toString()) }
        taiKhoanDao.getAll().first().forEach { taiKhoanSync.pushToCloud(it, it.id.toString()) }
        kyThuatVienDao.getAll().first().forEach { kyThuatVienSync.pushToCloud(it, it.id.toString()) }
        thietBiDao.getAll().first().forEach { thietBiSync.pushToCloud(it, it.id.toString()) }
        phongDao.getAll().first().forEach { phongSync.pushToCloud(it, it.id.toString()) }
        tangDao.getAll().first().forEach { tangSync.pushToCloud(it, it.id.toString()) }
        dayDao.getAll().first().forEach { daySync.pushToCloud(it, it.id.toString()) }
        loaiPhongDao.getAll().first().forEach { loaiPhongSync.pushToCloud(it, it.id.toString()) }
        loaiThietBiDao.getAll().first().forEach { loaiThietBiSync.pushToCloud(it, it.id.toString()) }
        chuyenMonDao.getAll().first().forEach { chuyenMonSync.pushToCloud(it, it.id.toString()) }
        chuyenMonKtvDao.getAll().first().forEach { chuyenMonKtvSync.pushToCloud(it, it.id.toString()) }
        danhGiaKtvDao.getAll().first().forEach { danhGiaKtvSync.pushToCloud(it, it.id.toString()) }
        bienBanYeuCauDao.getAll().first().forEach { bienBanYeuCauSync.pushToCloud(it, it.id.toString()) }
        chiTietYeuCauDao.getAll().first().forEach { chiTietYeuCauSync.pushToCloud(it, it.id.toString()) }
        anhMinhChungBaoCaoDao.getAll().first().forEach { anhMinhChungBaoCaoSync.pushToCloud(it, it.id.toString()) }
        anhMinhChungLamViecDao.getAll().first().forEach { anhMinhChungLamViecSync.pushToCloud(it, it.id.toString()) }
        donViDao.getAll().first().forEach { donViSync.pushToCloud(it, it.id.toString()) }
        vaiTroDao.getAll().first().forEach { vaiTroSync.pushToCloud(it, it.id.toString()) }
    }
}
