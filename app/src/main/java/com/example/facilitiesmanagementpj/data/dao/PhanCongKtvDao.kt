package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 13. PhanCongKTVDao
@Dao
interface PhanCongKtvDao {
    @Query("SELECT * FROM phan_cong_ktv")
    fun getAll(): Flow<List<PhanCongKtv>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(phanCongKTV: PhanCongKtv)

    @Update
    suspend fun update(phanCongKTV: PhanCongKtv)

    @Delete
    suspend fun delete(phanCongKTV: PhanCongKtv)
}