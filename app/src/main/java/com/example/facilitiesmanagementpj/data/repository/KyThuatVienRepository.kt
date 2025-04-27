package com.example.facilitiesmanagementpj.data.repository

import android.util.Log
import com.example.facilitiesmanagementpj.data.dao.KyThuatVienDao
import com.example.facilitiesmanagementpj.data.dao.PhanCongKtvDao
import com.example.facilitiesmanagementpj.data.entity.KyThuatVien
import com.example.facilitiesmanagementpj.data.entity.KyThuatVienWithTaiKhoan
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import com.example.facilitiesmanagementpj.data.utils.TrangThaiKtv
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class KyThuatVienRepository @Inject constructor(private val kyThuatVienDao: KyThuatVienDao, private val chuyenMonRepo: ChuyenMonRepository) {
//
//    suspend fun getByTrangThaiWithTaiKhoan(trangThai: String?): List<KyThuatVienWithTaiKhoan> {
//        return kyThuatVienDao.getByTrangThaiWithTaiKhoan(trangThai)
//    }
//
//    suspend fun updateNgayBatDauLam(taiKhoanId: Int, ngay: Long) {
//        kyThuatVienDao.updateNgayBatDauLamByTaiKhoanId(taiKhoanId, ngay)
//    }
//
//    fun getKyThuatVienByTaiKhoanId(taiKhoanId: Int): Flow<KyThuatVien?> {
//        return flow {
//            emit(kyThuatVienDao.getKyThuatVienByTaiKhoanId(taiKhoanId))
//        }.flowOn(Dispatchers.IO)
//    }
//
//    fun getByTaiKhoanId(taiKhoanId: Int): Flow<KyThuatVien?> {
//        return kyThuatVienDao.getByTaiKhoanId(taiKhoanId)
//    }
//
//    fun getChuyenMonCuaKTV(kyThuatVienId: Int): Flow<List<String>> {
//        return kyThuatVienDao.getChuyenMonCuaKTV(kyThuatVienId)
//    }
//
//    fun getAllKyThuatVien(): Flow<List<KyThuatVien>> = kyThuatVienDao.getAll()
//
//    suspend fun insert(kyThuatVien: KyThuatVien) = kyThuatVienDao.insert(kyThuatVien)
//    suspend fun update(kyThuatVien: KyThuatVien) = kyThuatVienDao.update(kyThuatVien)
//    suspend fun delete(kyThuatVien: KyThuatVien) = kyThuatVienDao.delete(kyThuatVien)
//
//    suspend fun hasAnyChuyenMon(kyThuatVienId: Int, chuyenMonIds: Set<Int>): Boolean {
//        val current = chuyenMonRepo.getChuyenMonIdsCuaKTV(kyThuatVienId)
//        return current.any { chuyenMonIds.contains(it) }
//    }
//
//}

@Singleton
class KyThuatVienRepository @Inject constructor(
    private val kyThuatVienDao: KyThuatVienDao,
    private val chuyenMonRepo: ChuyenMonRepository,
    private val phanCongKtvDao: PhanCongKtvDao,
    private val syncService: BaseFirestoreSyncService<KyThuatVien>
) {

    suspend fun capNhatTrangThaiKTV(taiKhoanId: Int) {
        Log.d("capNhatTrangThaiKTV", "Bắt đầu cập nhật trạng thái cho tài khoản ID = $taiKhoanId")

        // 🔹 Lấy danh sách tất cả các phân công KTV
        val dsPhanCongKtv = phanCongKtvDao.getPhanCongDangLamByKtv(taiKhoanId)
        Log.d("capNhatTrangThaiKTV", "Số lượng phân công KTV lấy được: ${dsPhanCongKtv.size}")

        // 🔹 Kiểm tra trạng thái
        val coDangLamViec = dsPhanCongKtv.any { it.trangThai == TrangThaiPhanCong.DANG_THUC_HIEN }
        Log.d("capNhatTrangThaiKTV", "Có đang làm việc hay không? $coDangLamViec")

        val newTrangThai = when {
            coDangLamViec -> TrangThaiKtv.DANG_LAM_VIEC
            else -> TrangThaiKtv.DANG_NGHI
        }
        Log.d("capNhatTrangThaiKTV", "Trạng thái mới xác định: $newTrangThai")

        // 🔹 Cập nhật vào DB
        kyThuatVienDao.updateTrangThaiKTV(taiKhoanId, newTrangThai)
        Log.d("capNhatTrangThaiKTV", "Đã update trạng thái vào database")

        // 🔹 Lấy lại thông tin KTV mới
        val updatedKtv = kyThuatVienDao.getKyThuatVienByTaiKhoanId(taiKhoanId)
        if (updatedKtv != null) {
            Log.d("capNhatTrangThaiKTV", "Lấy được KTV sau cập nhật, tiến hành sync Firestore: $updatedKtv")
            SyncDispatcher.dispatch(syncService, updatedKtv, SyncDispatcher.SyncType.UPDATE)
            Log.d("capNhatTrangThaiKTV", "Đã sync Firestore thành công")
        } else {
            Log.w("capNhatTrangThaiKTV", "Không tìm thấy KTV sau khi update!")
        }
    }



    suspend fun getByTrangThaiWithTaiKhoan(trangThai: String?): List<KyThuatVienWithTaiKhoan> {
        return kyThuatVienDao.getByTrangThaiWithTaiKhoan(trangThai)
    }

    suspend fun updateNgayBatDauLam(taiKhoanId: Int, ngay: Long) {
        kyThuatVienDao.updateNgayBatDauLamByTaiKhoanId(taiKhoanId, ngay)
        val ktv = kyThuatVienDao.getKyThuatVienByTaiKhoanId(taiKhoanId)
        if (ktv != null) {
            SyncDispatcher.dispatch(syncService, ktv, SyncDispatcher.SyncType.UPDATE)
        }
    }

    fun getKyThuatVienByTaiKhoanId(taiKhoanId: Int): Flow<KyThuatVien?> {
        return flow {
            emit(kyThuatVienDao.getKyThuatVienByTaiKhoanId(taiKhoanId))
        }.flowOn(Dispatchers.IO)
    }

    fun getByTaiKhoanId(taiKhoanId: Int): Flow<KyThuatVien?> {
        return kyThuatVienDao.getByTaiKhoanId(taiKhoanId)
    }

    fun getChuyenMonCuaKTV(kyThuatVienId: Int): Flow<List<String>> {
        return kyThuatVienDao.getChuyenMonCuaKTV(kyThuatVienId)
    }

    fun getAllKyThuatVien(): Flow<List<KyThuatVien>> = kyThuatVienDao.getAll()

//    suspend fun insert(ktv: KyThuatVien) {
//        kyThuatVienDao.insert(ktv)
//        SyncDispatcher.dispatch(syncService, ktv, SyncDispatcher.SyncType.INSERT)
//    }

    suspend fun insert(entity: KyThuatVien) {
        val id = kyThuatVienDao.insertAndReturnId(entity).toInt()
        val entityWithId = entity.copy(id = id)
        SyncDispatcher.dispatch(syncService, entityWithId, SyncDispatcher.SyncType.INSERT)
    }


    suspend fun update(ktv: KyThuatVien) {
        kyThuatVienDao.update(ktv)
        SyncDispatcher.dispatch(syncService, ktv, SyncDispatcher.SyncType.UPDATE)
    }

    suspend fun delete(ktv: KyThuatVien) {
        kyThuatVienDao.delete(ktv)
        SyncDispatcher.dispatch(syncService, ktv, SyncDispatcher.SyncType.DELETE)
    }

    suspend fun hasAnyChuyenMon(kyThuatVienId: Int, chuyenMonIds: Set<Int>): Boolean {
        val current = chuyenMonRepo.getChuyenMonIdsCuaKTV(kyThuatVienId)
        return current.any { chuyenMonIds.contains(it) }
    }
}

