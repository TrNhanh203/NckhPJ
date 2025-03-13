package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

@Dao
interface YeuCauDao {
    @Query("SELECT * FROM yeu_cau")
    fun getAll(): Flow<List<YeuCau>>

    @Query("SELECT * FROM yeu_cau WHERE donViId = :donViId")
    fun getReportsByUnit(donViId: Int): Flow<List<YeuCau>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(yeuCau: YeuCau)

    @Update
    suspend fun update(yeuCau: YeuCau)

    @Delete
    suspend fun delete(yeuCau: YeuCau)
}