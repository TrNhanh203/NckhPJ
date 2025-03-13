package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

@Dao
interface BienBanYeuCauDao {
    @Query("SELECT * FROM bien_ban_yeu_cau WHERE id = :yeuCauId")
    fun getBienBanByYeuCau(yeuCauId: Int): Flow<List<BienBanYeuCau>>

    @Insert
    suspend fun insert(bienBanYeuCau: BienBanYeuCau)

    @Update
    suspend fun update(bienBanYeuCau: BienBanYeuCau)

    @Delete
    suspend fun delete(bienBanYeuCau: BienBanYeuCau)
}
