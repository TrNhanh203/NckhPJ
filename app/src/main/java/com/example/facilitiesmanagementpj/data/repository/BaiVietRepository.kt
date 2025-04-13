package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.BaiVietDao
import com.example.facilitiesmanagementpj.data.entity.BaiViet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BaiVietRepository @Inject constructor(
    private val baiVietDao: BaiVietDao
) {

    fun getAll(): Flow<List<BaiViet>> = baiVietDao.getAll()

    suspend fun insert(baiViet: BaiViet) {
        baiVietDao.insert(baiViet)
    }

    suspend fun delete(baiViet: BaiViet) {
        baiVietDao.delete(baiViet)
    }

    suspend fun deleteAll() {
        baiVietDao.deleteAll()
    }
}
