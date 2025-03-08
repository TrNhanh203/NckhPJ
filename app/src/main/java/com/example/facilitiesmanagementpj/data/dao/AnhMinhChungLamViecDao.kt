package com.example.facilitiesmanagementpj.data.dao


import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 17. AnhMinhChungLamViecDao
@Dao
interface AnhMinhChungLamViecDao {
    @Query("SELECT * FROM anh_minh_chung_lam_viec")
    fun getAll(): Flow<List<AnhMinhChungLamViec>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anhMinhChungLamViec: AnhMinhChungLamViec)

    @Update
    suspend fun update(anhMinhChungLamViec: AnhMinhChungLamViec)

    @Delete
    suspend fun delete(anhMinhChungLamViec: AnhMinhChungLamViec)
}