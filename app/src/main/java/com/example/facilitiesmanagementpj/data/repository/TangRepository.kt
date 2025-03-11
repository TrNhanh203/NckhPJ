package com.example.facilitiesmanagementpj.data.repository


import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 4. TangRepository
@Singleton
class TangRepository @Inject constructor(private val tangDao: TangDao) {
    fun getAllTang(): Flow<List<Tang>> = tangDao.getAll()
    suspend fun insert(tang: Tang) = tangDao.insert(tang)
    suspend fun update(tang: Tang) = tangDao.update(tang)
    suspend fun delete(tang: Tang) = tangDao.delete(tang)

    val getTangWithDay: Flow<List<TangWithDay>> = tangDao.getTangWithDay()

    fun getTangByDayId(dayId: Int): Flow<List<Tang>> = tangDao.getTangByDayId(dayId)
}