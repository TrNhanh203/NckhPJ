package com.example.facilitiesmanagementpj.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.facilitiesmanagementpj.data.database.AppDatabase
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
//    fun provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext context: Context): AppDatabase {
//
//        return AppDatabase.getDatabase(context)
//    }
    fun provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
            .build()
        //return AppDatabase.getDatabase(context)
    }


    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE yeu_cau ADD COLUMN moTa TEXT NOT NULL DEFAULT ''")
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 1. Đổi tên bảng 'anh_minh_chung_bao_cao' thành 'minh_chung_bao_cao' và thêm trường 'type'
            database.execSQL("ALTER TABLE anh_minh_chung_bao_cao ADD COLUMN type TEXT NOT NULL DEFAULT ''")

            // 2. Đổi tên bảng 'anh_minh_chung_lam_viec' thành 'minh_chung_lam_viec' và thêm trường 'type'
            database.execSQL("ALTER TABLE anh_minh_chung_lam_viec ADD COLUMN type TEXT NOT NULL DEFAULT ''")
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE yeu_cau ADD COLUMN lyDoTuChoi TEXT")
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val cursor = database.query("PRAGMA table_info(anh_minh_chung_lam_viec)")
            var columnExists = false
            while (cursor.moveToNext()) {
                val columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                if (columnName == "ghiChu") {
                    columnExists = true
                    break
                }
            }
            cursor.close()

            if (!columnExists) {
                database.execSQL("ALTER TABLE anh_minh_chung_lam_viec ADD COLUMN ghiChu TEXT")
            }

            database.execSQL("ALTER TABLE phan_cong ADD COLUMN trangThai TEXT NOT NULL DEFAULT '${TrangThaiPhanCong.CHO_PHAN_HOI}'")
            database.execSQL("ALTER TABLE phan_cong ADD COLUMN soLuongKTVThamGia INTEGER")
            // Xóa bảng cũ
            database.execSQL("DROP TABLE IF EXISTS phan_cong_ktv")

            // Tạo lại bảng mới với đầy đủ trường mới
            database.execSQL("""
            CREATE TABLE IF NOT EXISTS phan_cong_ktv (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                phanCongId INTEGER NOT NULL,
                taiKhoanKTVId INTEGER NOT NULL,
                trangThai TEXT NOT NULL,
                thoiGianDuKien INTEGER NOT NULL,
                thoiGianPhatSinh INTEGER NOT NULL DEFAULT 0,
                thoiGianBatDau INTEGER,
                thoiGianHoanThien INTEGER,
                dangXinGiaHan INTEGER NOT NULL DEFAULT 0,
                soThoiGianXinGiaHan INTEGER NOT NULL DEFAULT 0,
                soLanGiaHan INTEGER NOT NULL DEFAULT 0,
                tongThoiGianDaXinGiaHan INTEGER NOT NULL DEFAULT 0,
                moTaCongViec TEXT,
                daChapNhan INTEGER NOT NULL DEFAULT 0,
                thoiGianTuChoi INTEGER,
                lyDoTuChoi TEXT,
                thoiGianLamViecThucTe INTEGER NOT NULL DEFAULT 0,
                trangThaiCuoiCung TEXT
            )
        """.trimIndent())
        }
    }

    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("""
            CREATE TABLE IF NOT EXISTS `bai_viet` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `tieuDe` TEXT NOT NULL,
                `moTa` TEXT NOT NULL,
                `anhDaiDien` TEXT NOT NULL,
                `link` TEXT,
                `noiDungHtml` TEXT,
                `thoiGianTao` INTEGER NOT NULL,
                `nguoiTaoId` INTEGER
            )
        """.trimIndent())

            database.execSQL("""
            CREATE TABLE IF NOT EXISTS `danh_gia_ktv` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `phanCongKtvId` INTEGER NOT NULL,
                `nguoiDanhGiaId` INTEGER NOT NULL,
                `diem` INTEGER NOT NULL,
                `nhanXet` TEXT,
                `thoiGian` INTEGER NOT NULL
            )
        """.trimIndent())
        }
    }

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("""
            CREATE TABLE IF NOT EXISTS thong_bao (
                id TEXT NOT NULL PRIMARY KEY,
                nguoiNhanId INTEGER NOT NULL,
                tieuDe TEXT NOT NULL,
                noiDung TEXT NOT NULL,
                loai TEXT NOT NULL,
                thoiGian INTEGER NOT NULL,
                daDoc INTEGER NOT NULL
            )
        """.trimIndent())
        }
    }


    @Provides
    fun provideThongBaoDao(database: AppDatabase): ThongBaoDao = database.thongBaoDao()

    @Provides
    fun provideBaiVietDao(database: AppDatabase): BaiVietDao = database.baiVietDao()

    @Provides
    fun provideDanhGiaKTVDao(database: AppDatabase): DanhGiaKTVDao = database.danhGiaKTVDao()


    @Provides
    fun provideLoaiThietBiDao(database: AppDatabase): LoaiThietBiDao = database.loaiThietBiDao()

    @Provides
    fun provideLoaiPhongDao(database: AppDatabase): LoaiPhongDao = database.loaiPhongDao()

    @Provides
    fun provideDayDao(database: AppDatabase): DayDao = database.dayDao()

    @Provides
    fun provideTangDao(database: AppDatabase): TangDao = database.tangDao()

    @Provides
    fun providePhongDao(database: AppDatabase): PhongDao = database.phongDao()

    @Provides
    fun provideVaiTroDao(database: AppDatabase): VaiTroDao = database.vaiTroDao()

    @Provides
    fun provideTaiKhoanDao(database: AppDatabase): TaiKhoanDao = database.taiKhoanDao()

    @Provides
    fun provideThietBiDao(database: AppDatabase): ThietBiDao = database.thietBiDao()

    @Provides
    fun provideChuyenMonDao(database: AppDatabase): ChuyenMonDao = database.chuyenMonDao()

    @Provides
    fun provideKyThuatVienDao(database: AppDatabase): KyThuatVienDao = database.kyThuatVienDao()

    @Provides
    fun providePhanCongDao(database: AppDatabase): PhanCongDao = database.phanCongDao()


    @Provides
    fun providePhanCongKTVDao(database: AppDatabase): PhanCongKtvDao = database.phanCongKTVDao()

    @Provides
    fun provideYeuCauDao(database: AppDatabase): YeuCauDao = database.yeuCauDao()

    @Provides
    fun provideChiTietYeuCauDao(database: AppDatabase): ChiTietYeuCauDao = database.chiTietYeuCauDao()

    @Provides
    fun provideAnhMinhChungBaoCaoDao(database: AppDatabase): AnhMinhChungBaoCaoDao = database.anhMinhChungBaoCaoDao()

    @Provides
    fun provideAnhMinhChungLamViecDao(database: AppDatabase): AnhMinhChungLamViecDao = database.anhMinhChungLamViecDao()

    @Provides
    fun provideChuyenMonKTVDao(database: AppDatabase): ChuyenMonKtvDao = database.chuyenMonKTVDao()

    @Provides
    fun provideDonViDao(database: AppDatabase): DonViDao = database.donViDao()

    @Provides
    fun provideBienBanBaoLoiDao(database: AppDatabase): BienBanYeuCauDao = database.bienBanBaoLoiDao()
}
