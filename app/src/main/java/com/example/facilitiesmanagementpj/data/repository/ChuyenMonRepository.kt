package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 9. ChuyenMonRepository
@Singleton
class ChuyenMonRepository @Inject constructor(
    private val chuyenMonDao: ChuyenMonDao,
    private val chuyenMonKtvDao: ChuyenMonKtvDao
) {
    suspend fun insert(chuyenMon: ChuyenMon) = chuyenMonDao.insert(chuyenMon)
    suspend fun update(chuyenMon: ChuyenMon) = chuyenMonDao.update(chuyenMon)
    suspend fun delete(chuyenMon: ChuyenMon) = chuyenMonDao.delete(chuyenMon)

    suspend fun getAllChuyenMon(): List<ChuyenMon> {
        return chuyenMonDao.getAll()
    }

    suspend fun getChuyenMonIdsByKtv(kyThuatVienId: Int): List<Int> {
        return chuyenMonKtvDao.getChuyenMonIdsByKtvId(kyThuatVienId)
    }

    suspend fun updateChuyenMonForKtv(kyThuatVienId: Int, selectedIds: List<Int>) {
        chuyenMonKtvDao.deleteAllByKtvId(kyThuatVienId)
        selectedIds.forEach { chuyenMonId ->
            chuyenMonKtvDao.insert(
                ChuyenMonKtv(chuyenMonId = chuyenMonId, kyThuatVienId = kyThuatVienId)
            )
        }
    }
}
