package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 14. BaoCaoSuCoDao
@Dao
interface BaoCaoSuCoDao {
    @Query("SELECT * FROM bao_cao_su_co")
    fun getAll(): Flow<List<BaoCaoSuCo>>

    @Query("SELECT * FROM bao_cao_su_co WHERE donViId = :donViId")
    fun getReportsByUnit(donViId: Int): Flow<List<BaoCaoSuCo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(baoCaoSuCo: BaoCaoSuCo)

    @Update
    suspend fun update(baoCaoSuCo: BaoCaoSuCo)

    @Delete
    suspend fun delete(baoCaoSuCo: BaoCaoSuCo)
}