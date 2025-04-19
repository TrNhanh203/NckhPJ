package com.example.facilitiesmanagementpj.di

import SmartSyncManager
import android.content.Context
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.adapter.AnhMinhChungBaoCaoAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.AnhMinhChungLamViecAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.BaiVietAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.BienBanYeuCauAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.ChiTietYeuCauAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.ChuyenMonAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.ChuyenMonKtvAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.DanhGiaKtvAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.DayAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.DonViAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.KyThuatVienAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.LoaiPhongAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.LoaiThietBiAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.PhanCongAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.PhanCongKtvAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.PhongAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.TaiKhoanAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.TangAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.ThietBiAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.ThongBaoAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.VaiTroAdapter
import com.example.facilitiesmanagementpj.data.sync.adapter.YeuCauAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {

    @Provides fun provideBaiVietSync(dao: BaiVietDao) = BaseFirestoreSyncService("bai_viet", BaiViet::class.java, BaiVietAdapter(dao))
    @Provides fun provideThongBaoSync(dao: ThongBaoDao) = BaseFirestoreSyncService("thong_bao", ThongBao::class.java, ThongBaoAdapter(dao))
    @Provides fun provideYeuCauSync(dao: YeuCauDao) = BaseFirestoreSyncService("yeu_cau", YeuCau::class.java, YeuCauAdapter(dao))
    @Provides fun providePhanCongSync(dao: PhanCongDao) = BaseFirestoreSyncService("phan_cong", PhanCong::class.java, PhanCongAdapter(dao))
    @Provides fun providePhanCongKtvSync(dao: PhanCongKtvDao) = BaseFirestoreSyncService("phan_cong_ktv", PhanCongKtv::class.java, PhanCongKtvAdapter(dao))
    @Provides fun provideTaiKhoanSync(dao: TaiKhoanDao) = BaseFirestoreSyncService("tai_khoan", TaiKhoan::class.java, TaiKhoanAdapter(dao))
    @Provides fun provideKyThuatVienSync(dao: KyThuatVienDao) = BaseFirestoreSyncService("ky_thuat_vien", KyThuatVien::class.java, KyThuatVienAdapter(dao))
    @Provides fun provideThietBiSync(dao: ThietBiDao) = BaseFirestoreSyncService("thiet_bi", ThietBi::class.java, ThietBiAdapter(dao))
    @Provides fun providePhongSync(dao: PhongDao) = BaseFirestoreSyncService("phong", Phong::class.java, PhongAdapter(dao))
    @Provides fun provideTangSync(dao: TangDao) = BaseFirestoreSyncService("tang", Tang::class.java, TangAdapter(dao))
    @Provides fun provideDaySync(dao: DayDao) = BaseFirestoreSyncService("day", Day::class.java, DayAdapter(dao))
    @Provides fun provideLoaiPhongSync(dao: LoaiPhongDao) = BaseFirestoreSyncService("loai_phong", LoaiPhong::class.java, LoaiPhongAdapter(dao))
    @Provides fun provideLoaiThietBiSync(dao: LoaiThietBiDao) = BaseFirestoreSyncService("loai_thiet_bi", LoaiThietBi::class.java, LoaiThietBiAdapter(dao))
    @Provides fun provideChuyenMonSync(dao: ChuyenMonDao) = BaseFirestoreSyncService("chuyen_mon", ChuyenMon::class.java, ChuyenMonAdapter(dao))
    @Provides fun provideChuyenMonKtvSync(dao: ChuyenMonKtvDao) = BaseFirestoreSyncService("chuyen_mon_ktv", ChuyenMonKtv::class.java, ChuyenMonKtvAdapter(dao))
    @Provides fun provideDanhGiaKtvSync(dao: DanhGiaKTVDao) = BaseFirestoreSyncService("danh_gia_ktv", DanhGiaKTV::class.java, DanhGiaKtvAdapter(dao))
    @Provides fun provideBienBanYeuCauSync(dao: BienBanYeuCauDao) = BaseFirestoreSyncService("bien_ban_yeu_cau", BienBanYeuCau::class.java, BienBanYeuCauAdapter(dao))
    @Provides fun provideChiTietYeuCauSync(dao: ChiTietYeuCauDao) = BaseFirestoreSyncService("chi_tiet_yeu_cau", ChiTietYeuCau::class.java, ChiTietYeuCauAdapter(dao))
    @Provides fun provideAnhMinhChungBaoCaoSync(dao: AnhMinhChungBaoCaoDao) = BaseFirestoreSyncService("anh_minh_chung_bao_cao", AnhMinhChungBaoCao::class.java, AnhMinhChungBaoCaoAdapter(dao))
    @Provides fun provideAnhMinhChungLamViecSync(dao: AnhMinhChungLamViecDao) = BaseFirestoreSyncService("anh_minh_chung_lam_viec", AnhMinhChungLamViec::class.java, AnhMinhChungLamViecAdapter(dao))
    @Provides fun provideDonViSync(dao: DonViDao) = BaseFirestoreSyncService("don_vi", DonVi::class.java, DonViAdapter(dao))
    @Provides fun provideVaiTroSync(dao: VaiTroDao) = BaseFirestoreSyncService("vai_tro", VaiTro::class.java, VaiTroAdapter(dao))
}
