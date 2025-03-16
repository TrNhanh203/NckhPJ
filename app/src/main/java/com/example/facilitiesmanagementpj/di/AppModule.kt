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
        ).addMigrations(MIGRATION_1_2)
            .build()
        //return AppDatabase.getDatabase(context)
    }


    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE yeu_cau ADD COLUMN moTa TEXT NOT NULL DEFAULT ''")
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
