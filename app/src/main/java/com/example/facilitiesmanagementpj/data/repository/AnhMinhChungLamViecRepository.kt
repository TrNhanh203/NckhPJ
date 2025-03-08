package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 16. AnhMinhChungLamViecRepository
@Singleton
class AnhMinhChungLamViecRepository @Inject constructor(private val anhMinhChungLamViecDao: AnhMinhChungLamViecDao) {
    fun getAllAnhMinhChungLamViec(): Flow<List<AnhMinhChungLamViec>> = anhMinhChungLamViecDao.getAll()
    suspend fun insert(anhMinhChungLamViec: AnhMinhChungLamViec) = anhMinhChungLamViecDao.insert(anhMinhChungLamViec)
    suspend fun update(anhMinhChungLamViec: AnhMinhChungLamViec) = anhMinhChungLamViecDao.update(anhMinhChungLamViec)
    suspend fun delete(anhMinhChungLamViec: AnhMinhChungLamViec) = anhMinhChungLamViecDao.delete(anhMinhChungLamViec)
}