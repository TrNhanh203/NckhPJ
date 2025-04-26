package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

@Dao
interface DonViDao {
    @Query("SELECT * FROM don_vi")
    fun getAll(): Flow<List<DonVi>>

    @Query("SELECT * FROM don_vi ORDER BY tenDonVi ASC")
    fun getAllDonVi(): Flow<List<DonVi>>

    @Query("SELECT * FROM don_vi WHERE id = :id")
    suspend fun getById(id: Int): DonVi?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(donVi: DonVi)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndReturnId(entity: DonVi): Long


    @Update
    suspend fun update(donVi: DonVi)

    @Delete
    suspend fun delete(donVi: DonVi)
}
