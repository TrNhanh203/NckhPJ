package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BienBanBaoLoiRepository @Inject constructor(private val bienBanBaoLoiDao: BienBanBaoLoiDao) {
    fun getBienBanByBaoCao(baoCaoId: Int): Flow<List<BienBanBaoLoi>> = bienBanBaoLoiDao.getBienBanByBaoCao(baoCaoId)

    suspend fun insert(bienBanBaoLoi: BienBanBaoLoi) {
        bienBanBaoLoiDao.insert(bienBanBaoLoi)
    }

    suspend fun update(bienBanBaoLoi: BienBanBaoLoi) {
        bienBanBaoLoiDao.update(bienBanBaoLoi)
    }

    suspend fun delete(bienBanBaoLoi: BienBanBaoLoi) {
        bienBanBaoLoiDao.delete(bienBanBaoLoi)
    }
}
