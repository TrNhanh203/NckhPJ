package com.example.facilitiesmanagementpj.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*

@Database(
    entities = [
        LoaiThietBi::class,
        LoaiPhong::class,
        Day::class,
        Tang::class,
        Phong::class,
        VaiTro::class,
        TaiKhoan::class,
        ThietBi::class,
        ChuyenMon::class,
        KyThuatVien::class,
        PhanCong::class,
        PhanCongKtv::class,
        YeuCau::class,
        ChiTietYeuCau::class,
        AnhMinhChungBaoCao::class,
        AnhMinhChungLamViec::class,
        ChuyenMonKtv::class,
        DonVi::class,
        BienBanYeuCau::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loaiThietBiDao(): LoaiThietBiDao
    abstract fun loaiPhongDao(): LoaiPhongDao
    abstract fun dayDao(): DayDao
    abstract fun tangDao(): TangDao
    abstract fun phongDao(): PhongDao
    abstract fun vaiTroDao(): VaiTroDao
    abstract fun taiKhoanDao(): TaiKhoanDao
    abstract fun thietBiDao(): ThietBiDao
    abstract fun chuyenMonDao(): ChuyenMonDao
    abstract fun kyThuatVienDao(): KyThuatVienDao
    abstract fun phanCongDao(): PhanCongDao
    abstract fun phanCongKTVDao(): PhanCongKtvDao
    abstract fun yeuCauDao(): YeuCauDao
    abstract fun chiTietYeuCauDao(): ChiTietYeuCauDao
    abstract fun anhMinhChungBaoCaoDao(): AnhMinhChungBaoCaoDao
    abstract fun anhMinhChungLamViecDao(): AnhMinhChungLamViecDao
    abstract fun chuyenMonKTVDao(): ChuyenMonKtvDao
    abstract fun donViDao(): DonViDao
    abstract fun bienBanBaoLoiDao(): BienBanYeuCauDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null



        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }



    }
}
