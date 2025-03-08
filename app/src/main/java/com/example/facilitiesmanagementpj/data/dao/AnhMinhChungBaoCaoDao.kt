package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*
// 16. AnhMinhChungDao
@Dao
interface AnhMinhChungBaoCaoDao {
    @Query("SELECT * FROM anh_minh_chung_bao_cao")
    fun getAll(): Flow<List<AnhMinhChungBaoCao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anhMinhChung: AnhMinhChungBaoCao)

    @Update
    suspend fun update(anhMinhChung: AnhMinhChungBaoCao)

    @Delete
    suspend fun delete(anhMinhChung: AnhMinhChungBaoCao)
}