package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 3. DayRepository
@Singleton
class DayRepository @Inject constructor(private val dayDao: DayDao) {
    fun getAllDay(): Flow<List<Day>> = dayDao.getAll()
    suspend fun insert(day: Day) = dayDao.insert(day)
    suspend fun update(day: Day) = dayDao.update(day)
    suspend fun delete(day: Day) = dayDao.delete(day)
}