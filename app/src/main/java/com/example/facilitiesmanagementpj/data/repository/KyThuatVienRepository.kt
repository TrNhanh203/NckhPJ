package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.KyThuatVienDao
import com.example.facilitiesmanagementpj.data.entity.KyThuatVien
import com.example.facilitiesmanagementpj.data.entity.KyThuatVienWithTaiKhoan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KyThuatVienRepository @Inject constructor(private val kyThuatVienDao: KyThuatVienDao, private val chuyenMonRepo: ChuyenMonRepository) {

    suspend fun getByTrangThaiWithTaiKhoan(trangThai: String?): List<KyThuatVienWithTaiKhoan> {
        return kyThuatVienDao.getByTrangThaiWithTaiKhoan(trangThai)
    }

    suspend fun updateNgayBatDauLam(taiKhoanId: Int, ngay: Long) {
        kyThuatVienDao.updateNgayBatDauLamByTaiKhoanId(taiKhoanId, ngay)
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

    suspend fun insert(kyThuatVien: KyThuatVien) = kyThuatVienDao.insert(kyThuatVien)
    suspend fun update(kyThuatVien: KyThuatVien) = kyThuatVienDao.update(kyThuatVien)
    suspend fun delete(kyThuatVien: KyThuatVien) = kyThuatVienDao.delete(kyThuatVien)

    suspend fun hasAnyChuyenMon(kyThuatVienId: Int, chuyenMonIds: Set<Int>): Boolean {
        val current = chuyenMonRepo.getChuyenMonIdsCuaKTV(kyThuatVienId)
        return current.any { chuyenMonIds.contains(it) }
    }

}
