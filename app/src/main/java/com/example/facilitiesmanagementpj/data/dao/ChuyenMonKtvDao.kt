package com.example.facilitiesmanagementpj.data.dao


import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*



// 18. ChuyenMonKTVDao
@Dao
interface ChuyenMonKtvDao {
    @Query("SELECT * FROM chuyen_mon_ky_thuat_vien")
    fun getAll(): Flow<List<ChuyenMonKtv>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chuyenMonKTV: ChuyenMonKtv)

    @Update
    suspend fun update(chuyenMonKTV: ChuyenMonKtv)

    @Delete
    suspend fun delete(chuyenMonKTV: ChuyenMonKtv)
}
