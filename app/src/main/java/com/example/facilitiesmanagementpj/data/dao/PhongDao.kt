package com.example.facilitiesmanagementpj.data.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.facilitiesmanagementpj.data.entity.*

data class PhongWithDetails(
    val id: Int,
    val tenPhong: String,
    val tenDay: String,
    val tenTang: String,
    val soLuongThietBi: Int
)


// 5. PhongDao
@Dao
interface PhongDao {
    @Query("""
    SELECT phong.id, phong.tenPhong, day.tenDay, tang.tenTang, COUNT(thiet_bi.id) as soLuongThietBi
    FROM phong
    LEFT JOIN thiet_bi ON phong.id = thiet_bi.phongId
    INNER JOIN day ON phong.dayId = day.id
    INNER JOIN tang ON phong.tangId = tang.id
    WHERE phong.donViId = :donViId
    GROUP BY phong.id, phong.tenPhong, day.tenDay, tang.tenTang
    """)
    fun getPhongByDonVi(donViId: Int): Flow<List<PhongWithDetails>>



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

    @Query("SELECT * FROM phong WHERE donViId = :donViId")
    fun getPhongTheoDonVi(donViId: Int): Flow<List<Phong>>


    @Query("""
        SELECT phong.id AS phongId, phong.tenPhong, phong.tangId, phong.dayId, 
               phong.loaiPhongId, phong.donViId, loai_phong.tenLoaiPhong
        FROM phong
        LEFT JOIN loai_phong ON phong.loaiPhongId = loai_phong.id
    """)
    fun getPhongWithLoaiPhong(): Flow<List<PhongWithLoaiPhong>>

//    @Query("""
//        SELECT phong.id AS phongId, phong.tenPhong,
//               loai_phong.tenLoaiPhong, don_vi.tenDonVi
//        FROM phong
//        LEFT JOIN loai_phong ON phong.loaiPhongId = loai_phong.id
//        LEFT JOIN don_vi ON phong.donViId = don_vi.id
//    """)
//    fun getPhongWithDetails(): Flow<List<PhongWithDetails>>


    @Query("SELECT * FROM phong WHERE tangId = :tangId")
    fun getPhongByTangId(tangId: Int): Flow<List<Phong>>

}