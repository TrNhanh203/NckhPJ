package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

// 10. KyThuatVienRepository
@Singleton
class KyThuatVienRepository @Inject constructor(private val kyThuatVienDao: KyThuatVienDao) {

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
}