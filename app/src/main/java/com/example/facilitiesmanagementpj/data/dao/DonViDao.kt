package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

@Dao
interface DonViDao {
    @Query("SELECT * FROM don_vi")
    fun getAll(): Flow<List<DonVi>>

    @Insert
    suspend fun insert(donVi: DonVi)

    @Update
    suspend fun update(donVi: DonVi)

    @Delete
    suspend fun delete(donVi: DonVi)
}
