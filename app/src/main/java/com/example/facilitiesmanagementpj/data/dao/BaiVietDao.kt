package com.example.facilitiesmanagementpj.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.facilitiesmanagementpj.data.entity.BaiViet
import kotlinx.coroutines.flow.Flow

@Dao
interface BaiVietDao {
    @Insert
    suspend fun insertAndReturnId(baiViet: BaiViet): Long

    @Query("SELECT * FROM bai_viet ORDER BY thoiGianTao DESC")
    fun getAll(): Flow<List<BaiViet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(baiViet: BaiViet)

    @Delete
    suspend fun delete(baiViet: BaiViet)

    @Query("DELETE FROM bai_viet")
    suspend fun deleteAll()
}
