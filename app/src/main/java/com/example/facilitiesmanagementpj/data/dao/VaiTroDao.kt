package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

// 6. VaiTroDao
@Dao
interface VaiTroDao {
    @Query("SELECT * FROM vai_tro")
    fun getAll(): Flow<List<VaiTro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vaiTro: VaiTro)

    @Update
    suspend fun update(vaiTro: VaiTro)

    @Delete
    suspend fun delete(vaiTro: VaiTro)
}