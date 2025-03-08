package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

@Dao
interface BienBanBaoLoiDao {
    @Query("SELECT * FROM bien_ban_bao_loi WHERE id = :baoCaoId")
    fun getBienBanByBaoCao(baoCaoId: Int): Flow<List<BienBanBaoLoi>>

    @Insert
    suspend fun insert(bienBanBaoLoi: BienBanBaoLoi)

    @Update
    suspend fun update(bienBanBaoLoi: BienBanBaoLoi)

    @Delete
    suspend fun delete(bienBanBaoLoi: BienBanBaoLoi)
}
