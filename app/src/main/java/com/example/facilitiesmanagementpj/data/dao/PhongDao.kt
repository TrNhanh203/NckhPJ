package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 5. PhongDao
@Dao
interface PhongDao {
    @Query("SELECT * FROM phong")
    fun getAll(): Flow<List<Phong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(phong: Phong)

    @Update
    suspend fun update(phong: Phong)

    @Delete
    suspend fun delete(phong: Phong)
}