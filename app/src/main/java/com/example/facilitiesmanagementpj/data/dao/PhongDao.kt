package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 5. PhongDao
@Dao
interface PhongDao {
    @Query("SELECT * FROM phong")
    fun getAll(): Flow<List<Phong>>

    @Query("SELECT * FROM phong")
    suspend fun getAllList(): List<Phong> // ✅ Trả về List thay vì Flow


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(phong: Phong)

    @Update
    suspend fun update(phong: Phong)

    @Delete
    suspend fun delete(phong: Phong)

    // ✅ Thêm phương thức cập nhật chỉ `donViId`
    @Query("UPDATE phong SET donViId = :donViId WHERE id = :phongId")
    suspend fun capNhatDonViId(phongId: Int, donViId: Int)
}