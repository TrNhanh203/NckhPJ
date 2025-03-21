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

    @Query("SELECT chuyenMonId FROM chuyen_mon_ky_thuat_vien WHERE kyThuatVienId = :ktvId")
    suspend fun getChuyenMonIdsByKtvId(ktvId: Int): List<Int>


    @Query("DELETE FROM chuyen_mon_ky_thuat_vien WHERE kyThuatVienId = :ktvId")
    suspend fun deleteAllByKtvId(ktvId: Int)
}
