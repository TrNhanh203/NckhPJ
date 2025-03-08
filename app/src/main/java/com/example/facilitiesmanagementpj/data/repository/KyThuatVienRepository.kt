package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 10. KyThuatVienRepository
@Singleton
class KyThuatVienRepository @Inject constructor(private val kyThuatVienDao: KyThuatVienDao) {
    fun getAllKyThuatVien(): Flow<List<KyThuatVien>> = kyThuatVienDao.getAll()
    suspend fun insert(kyThuatVien: KyThuatVien) = kyThuatVienDao.insert(kyThuatVien)
    suspend fun update(kyThuatVien: KyThuatVien) = kyThuatVienDao.update(kyThuatVien)
    suspend fun delete(kyThuatVien: KyThuatVien) = kyThuatVienDao.delete(kyThuatVien)
}