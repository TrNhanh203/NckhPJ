package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*


// 9. ChuyenMonDao
@Dao
interface ChuyenMonDao {
    @Query("SELECT * FROM chuyen_mon")
    fun getAll(): Flow<List<ChuyenMon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chuyenMon: ChuyenMon)

    @Update
    suspend fun update(chuyenMon: ChuyenMon)

    @Delete
    suspend fun delete(chuyenMon: ChuyenMon)
}