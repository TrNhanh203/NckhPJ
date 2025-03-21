package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*



@Dao
interface YeuCauDao {

    @Query("UPDATE yeu_cau SET trangThai = :status WHERE id = :yeuCauId")
    suspend fun updateYeuCauStatus(yeuCauId: Int, status: String)

    @Query("DELETE FROM yeu_cau WHERE id = :yeuCauId")
    suspend fun deleteYeuCau(yeuCauId: Int)

    @Query("SELECT * FROM yeu_cau WHERE donViId = :donViId")
    fun getYeuCauByDonVi(donViId: Int): Flow<List<YeuCau>>

    @Insert
    suspend fun insertYeuCau(yeuCau: YeuCau): Long // tra ve id yeu cau moi vua them

    @Query("SELECT * FROM yeu_cau WHERE id = :id")
    suspend fun getYeuCauById(id: Int): YeuCau?

    @Query("SELECT * FROM yeu_cau")
    fun getAll(): Flow<List<YeuCau>>

    @Query("SELECT * FROM yeu_cau WHERE trangThai != 'nhap'")
    fun getAllYeuCauTruNhap(): Flow<List<YeuCau>>

    @Query("SELECT * FROM yeu_cau WHERE donViId = :donViId")
    fun getReportsByUnit(donViId: Int): Flow<List<YeuCau>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(yeuCau: YeuCau)

    @Update
    suspend fun update(yeuCau: YeuCau)

    @Delete
    suspend fun delete(yeuCau: YeuCau)
}