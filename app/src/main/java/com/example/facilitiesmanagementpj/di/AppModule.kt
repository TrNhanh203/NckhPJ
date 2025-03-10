package com.example.facilitiesmanagementpj.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.facilitiesmanagementpj.data.database.AppDatabase
import com.example.facilitiesmanagementpj.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
            .build()
        //return AppDatabase.getDatabase(context)
    }

    // Tạo Migration từ version 1 lên 2
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 1. Tạo bảng mới với cột "dayId" có foreign key
            db.execSQL("""
            CREATE TABLE IF NOT EXISTS `tang_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `tenTang` TEXT NOT NULL,
                `dayId` INTEGER NOT NULL DEFAULT 1,
                FOREIGN KEY(`dayId`) REFERENCES `day`(`id`) ON DELETE CASCADE
            )
        """)

            // 2. Sao chép dữ liệu từ bảng cũ sang bảng mới
            db.execSQL("""
            INSERT INTO tang_new (id, tenTang, dayId)
            SELECT id, tenTang, 1 FROM tang
        """)

            // 3. Xóa bảng cũ
            db.execSQL("DROP TABLE tang")

            // 4. Đổi tên bảng mới thành bảng cũ
            db.execSQL("ALTER TABLE tang_new RENAME TO tang")

            // 5. Tạo Index
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_tang_dayId` ON `tang` (`dayId`)")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // ✅ Thêm cột moTa vào bảng thiet_bi
            db.execSQL("ALTER TABLE thiet_bi ADD COLUMN moTa TEXT")
        }
    }

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // ✅ Thêm cột `donViId` vào bảng `phong`
            db.execSQL("ALTER TABLE phong ADD COLUMN donViId INTEGER")

            // ✅ Xóa cột `donViId` khỏi bảng `thiet_bi`
            // Cách Room xử lý xóa cột: tạo bảng mới, sao chép dữ liệu, xóa bảng cũ, đổi tên bảng mới thành bảng cũ
            db.execSQL("""
            CREATE TABLE thiet_bi_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                tenThietBi TEXT NOT NULL,
                loaiThietBiId INTEGER NOT NULL,
                phongId INTEGER,
                tangId INTEGER,
                trangThai TEXT NOT NULL DEFAULT 'binh_thuong',
                ngayDaCat INTEGER,
                ngayDungSuDung INTEGER,
                ghiChu TEXT,
                ngayBaoDuongGanNhat INTEGER,
                ngayBaoDuongTiepTheo INTEGER,
                baoDuongDinhKy INTEGER,
                loaiBaoDuong TEXT,
                ghiChuBaoDuong TEXT,
                moTa TEXT
            )
        """)

            // ✅ Sao chép dữ liệu từ bảng cũ sang bảng mới (ngoại trừ cột `donViId` bị loại bỏ)
            db.execSQL("""
            INSERT INTO thiet_bi_new (id, tenThietBi, loaiThietBiId, phongId, tangId, trangThai, ngayDaCat, ngayDungSuDung, ghiChu, ngayBaoDuongGanNhat, ngayBaoDuongTiepTheo, baoDuongDinhKy, loaiBaoDuong, ghiChuBaoDuong, moTa)
            SELECT id, tenThietBi, loaiThietBiId, phongId, tangId, trangThai, ngayDaCat, ngayDungSuDung, ghiChu, ngayBaoDuongGanNhat, ngayBaoDuongTiepTheo, baoDuongDinhKy, loaiBaoDuong, ghiChuBaoDuong, moTa FROM thiet_bi
        """)

            // ✅ Xóa bảng `thiet_bi` cũ
            db.execSQL("DROP TABLE thiet_bi")

            // ✅ Đổi tên bảng `thiet_bi_new` thành `thiet_bi`
            db.execSQL("ALTER TABLE thiet_bi_new RENAME TO thiet_bi")
        }
    }


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
    fun providePhanCongThietBiDao(database: AppDatabase): PhanCongThietBiDao = database.phanCongThietBiDao()

    @Provides
    fun providePhanCongKTVDao(database: AppDatabase): PhanCongKtvDao = database.phanCongKTVDao()

    @Provides
    fun provideBaoCaoSuCoDao(database: AppDatabase): BaoCaoSuCoDao = database.baoCaoSuCoDao()

    @Provides
    fun provideChiTietBaoCaoDao(database: AppDatabase): ChiTietBaoCaoDao = database.chiTietBaoCaoDao()

    @Provides
    fun provideAnhMinhChungBaoCaoDao(database: AppDatabase): AnhMinhChungBaoCaoDao = database.anhMinhChungBaoCaoDao()

    @Provides
    fun provideAnhMinhChungLamViecDao(database: AppDatabase): AnhMinhChungLamViecDao = database.anhMinhChungLamViecDao()

    @Provides
    fun provideChuyenMonKTVDao(database: AppDatabase): ChuyenMonKtvDao = database.chuyenMonKTVDao()

    @Provides
    fun provideDonViDao(database: AppDatabase): DonViDao = database.donViDao()

    @Provides
    fun provideBienBanBaoLoiDao(database: AppDatabase): BienBanBaoLoiDao = database.bienBanBaoLoiDao()
}
