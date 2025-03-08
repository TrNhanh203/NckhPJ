package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 15. AnhMinhChungBaoCaoRepository
@Singleton
class AnhMinhChungBaoCaoRepository @Inject constructor(private val anhMinhChungBaoCaoDao: AnhMinhChungBaoCaoDao) {
    fun getAllAnhMinhChungBaoCao(): Flow<List<AnhMinhChungBaoCao>> = anhMinhChungBaoCaoDao.getAll()
    suspend fun insert(anhMinhChungBaoCao: AnhMinhChungBaoCao) = anhMinhChungBaoCaoDao.insert(anhMinhChungBaoCao)
    suspend fun update(anhMinhChungBaoCao: AnhMinhChungBaoCao) = anhMinhChungBaoCaoDao.update(anhMinhChungBaoCao)
    suspend fun delete(anhMinhChungBaoCao: AnhMinhChungBaoCao) = anhMinhChungBaoCaoDao.delete(anhMinhChungBaoCao)
}