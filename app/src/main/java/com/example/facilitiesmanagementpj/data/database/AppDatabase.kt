package com.example.facilitiesmanagementpj.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        PhanCongThietBi::class,
        PhanCongKtv::class,
        BaoCaoSuCo::class,
        ChiTietBaoCao::class,
        AnhMinhChungBaoCao::class,
        AnhMinhChungLamViec::class,
        ChuyenMonKtv::class,
        DonVi::class,
        BienBanBaoLoi::class
    ],
    version = 4,
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
    abstract fun phanCongThietBiDao(): PhanCongThietBiDao
    abstract fun phanCongKTVDao(): PhanCongKtvDao
    abstract fun baoCaoSuCoDao(): BaoCaoSuCoDao
    abstract fun chiTietBaoCaoDao(): ChiTietBaoCaoDao
    abstract fun anhMinhChungBaoCaoDao(): AnhMinhChungBaoCaoDao
    abstract fun anhMinhChungLamViecDao(): AnhMinhChungLamViecDao
    abstract fun chuyenMonKTVDao(): ChuyenMonKtvDao
    abstract fun donViDao(): DonViDao
    abstract fun bienBanBaoLoiDao(): BienBanBaoLoiDao

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
