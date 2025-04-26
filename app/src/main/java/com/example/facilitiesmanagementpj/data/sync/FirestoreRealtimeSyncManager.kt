package com.example.facilitiesmanagementpj.data.sync

import android.util.Log
import com.example.facilitiesmanagementpj.data.dao.AnhMinhChungBaoCaoDao
import com.example.facilitiesmanagementpj.data.dao.AnhMinhChungLamViecDao
import com.example.facilitiesmanagementpj.data.dao.BaiVietDao
import com.example.facilitiesmanagementpj.data.dao.ChiTietYeuCauDao
import com.example.facilitiesmanagementpj.data.dao.DanhGiaKTVDao
import com.example.facilitiesmanagementpj.data.dao.KyThuatVienDao
import com.example.facilitiesmanagementpj.data.dao.PhanCongDao
import com.example.facilitiesmanagementpj.data.dao.PhanCongKtvDao
import com.example.facilitiesmanagementpj.data.dao.SyncMetadataDao
import com.example.facilitiesmanagementpj.data.dao.TaiKhoanDao
import com.example.facilitiesmanagementpj.data.dao.ThietBiDao
import com.example.facilitiesmanagementpj.data.dao.ThongBaoDao
import com.example.facilitiesmanagementpj.data.dao.YeuCauDao
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungBaoCao
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungLamViec
import com.example.facilitiesmanagementpj.data.entity.BaiViet
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCau
import com.example.facilitiesmanagementpj.data.entity.DanhGiaKTV
import com.example.facilitiesmanagementpj.data.entity.KyThuatVien
import com.example.facilitiesmanagementpj.data.entity.PhanCong
import com.example.facilitiesmanagementpj.data.entity.PhanCongKtv
import com.example.facilitiesmanagementpj.data.entity.TaiKhoan
import com.example.facilitiesmanagementpj.data.entity.ThietBi
import com.example.facilitiesmanagementpj.data.entity.ThongBao
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirestoreRealtimeSyncManager(
    private val firestore: FirebaseFirestore,
    private val yeuCauDao: YeuCauDao,
    private val syncMetadataDao: SyncMetadataDao,

    private val phanCongDao: PhanCongDao,
    private val phanCongKtvDao: PhanCongKtvDao,
    private val thietBiDao: ThietBiDao,
    private val kyThuatVienDao: KyThuatVienDao,
    private val baiVietDao: BaiVietDao,
    private val thongBaoDao: ThongBaoDao,
    private val danhGiaKtvDao: DanhGiaKTVDao,
    private val taiKhoanDao: TaiKhoanDao,
    private val anhMinhChungBaoCaoDao: AnhMinhChungBaoCaoDao,
    private val anhMinhChungLamViecDao: AnhMinhChungLamViecDao,
    private val chiTietYeuCauDao: ChiTietYeuCauDao



    // Bạn có thể thêm các Dao khác ở đây nếu cần mở rộng
) {
    private val listeners = mutableListOf<ListenerRegistration>()

    fun startAllListeners() {
        startListeningYeuCau()
        startListeningPhanCong()
        startListeningPhanCongKtv()
        startListeningThietBi()
        startListeningKyThuatVien()
        startListeningBaiViet()
        startListeningThongBao()
        startListeningDanhGiaKtv()
        startListeningTaiKhoan()
        startListeningAnhMinhChungBaoCao()
        startListeningAnhMinhChungLamViec()
        startListeningChiTietYeuCau()


        // Có thể gọi thêm startListeningXyz() sau này
    }


    fun stopAllListeners() {
        listeners.forEach { it.remove() }
        listeners.clear()
    }


    private fun startListeningAnhMinhChungBaoCao() {
        startListening(
            collection = "anh_minh_chung_bao_cao",
            key = "anh_minh_chung_bao_cao",
            clazz = AnhMinhChungBaoCao::class.java,
            daoInsert = { anhMinhChungBaoCaoDao.insert(it) },
            daoDeleteById = { anhMinhChungBaoCaoDao.deleteById(it) }
        )
    }

    private fun startListeningAnhMinhChungLamViec() {
        startListening(
            collection = "anh_minh_chung_lam_viec",
            key = "anh_minh_chung_lam_viec",
            clazz = AnhMinhChungLamViec::class.java,
            daoInsert = { anhMinhChungLamViecDao.insert(it) },
            daoDeleteById = { anhMinhChungLamViecDao.deleteById(it) }
        )
    }

    private fun startListeningChiTietYeuCau() {
        startListening(
            collection = "chi_tiet_yeu_cau",
            key = "chi_tiet_yeu_cau",
            clazz = ChiTietYeuCau::class.java,
            daoInsert = { chiTietYeuCauDao.insert(it) },
            daoDeleteById = { chiTietYeuCauDao.deleteById(it) }
        )
    }

    private fun startListeningYeuCau() {
        startListening(
            collection = "yeu_cau",
            key = "yeu_cau",
            clazz = YeuCau::class.java,
            daoInsert = { yeuCauDao.insert(it) },
            daoDeleteById = { yeuCauDao.deleteById(it) }
        )
    }

    private fun startListeningPhanCong() {
        startListening(
            collection = "phan_cong",
            key = "phan_cong",
            clazz = PhanCong::class.java,
            daoInsert = { phanCongDao.insert(it) },
            daoDeleteById = { phanCongDao.deleteById(it) }
        )
    }

    private fun startListeningPhanCongKtv() {
        startListening(
            collection = "phan_cong_ktv",
            key = "phan_cong_ktv",
            clazz = PhanCongKtv::class.java,
            daoInsert = { phanCongKtvDao.insert(it) },
            daoDeleteById = { phanCongKtvDao.deleteById(it) }
        )
    }

    private fun startListeningThietBi() {
        startListening(
            collection = "thiet_bi",
            key = "thiet_bi",
            clazz = ThietBi::class.java,
            daoInsert = { thietBiDao.insert(it) },
            daoDeleteById = { thietBiDao.deleteById(it) }
        )
    }

    private fun startListeningKyThuatVien() {
        startListening(
            collection = "ky_thuat_vien",
            key = "ky_thuat_vien",
            clazz = KyThuatVien::class.java,
            daoInsert = { kyThuatVienDao.insert(it) },
            daoDeleteById = { kyThuatVienDao.deleteById(it) }
        )
    }


    private fun startListeningBaiViet() {
        startListening(
            collection = "bai_viet",
            key = "bai_viet",
            clazz = BaiViet::class.java,
            daoInsert = { baiVietDao.insert(it) },
            daoDeleteById = { baiVietDao.deleteById(it) }
        )
    }

    private fun startListeningThongBao() {
        startListening(
            collection = "thong_bao",
            key = "thong_bao",
            clazz = ThongBao::class.java,
            daoInsert = { thongBaoDao.insert(it) },
            daoDeleteById = { thongBaoDao.deleteById(it) }
        )
    }

    private fun startListeningDanhGiaKtv() {
        startListening(
            collection = "danh_gia_ktv",
            key = "danh_gia_ktv",
            clazz = DanhGiaKTV::class.java,
            daoInsert = { danhGiaKtvDao.insert(it) },
            daoDeleteById = { danhGiaKtvDao.deleteById(it) }
        )
    }

    private fun startListeningTaiKhoan() {
        startListening(
            collection = "tai_khoan",
            key = "tai_khoan",
            clazz = TaiKhoan::class.java,
            daoInsert = { taiKhoanDao.insert(it) },
            daoDeleteById = { taiKhoanDao.deleteById(it) }
        )
    }


    //    private fun startListening(lastSyncTime: Long) {
//        val listener = firestore.collection("yeu_cau")
//            .whereGreaterThan("thoiGianCapNhat", lastSyncTime)
//            .addSnapshotListener { snapshots, e ->
//                if (e != null) {
//                    Log.e("RealtimeSync", "Listen failed: ${e.localizedMessage}", e)
//                    return@addSnapshotListener
//                }
//
////                if (snapshots != null) {
////                    Log.d("RealtimeSync", "Received snapshot with ${snapshots.size()} documents")
////                    for (change in snapshots.documentChanges) {
////                        val thoiGianCapNhat = change.document.getLong("thoiGianCapNhat") ?: 0L
////                        if (thoiGianCapNhat > lastSyncTime) {
////                            val yeuCau = change.document.toObject(YeuCau::class.java)
////                            Log.d("RealtimeSync", "Change detected: ${change.type} - $yeuCau")
////                            when (change.type) {
////                                DocumentChange.Type.ADDED,
////                                DocumentChange.Type.MODIFIED -> {
////                                    CoroutineScope(Dispatchers.IO).launch {
////                                        yeuCauDao.insert(yeuCau)
////                                    }
////                                }
////                                DocumentChange.Type.REMOVED -> {
////                                    CoroutineScope(Dispatchers.IO).launch {
////                                        yeuCauDao.delete(yeuCau)
////                                    }
////                                }
////                            }
////                        } else {
////                            Log.d("RealtimeSync", "Ignored document id=${change.document.id} vì thoiGianCapNhat <= lastSyncTime")
////                        }
////                    }
////                }
//                if (snapshots != null) {
//                    Log.d("RealtimeSync", "Received snapshot with ${snapshots.size()} documents")
//                    for (change in snapshots.documentChanges) {
//                        when (change.type) {
//                            DocumentChange.Type.REMOVED -> {
//                                val yeuCau = change.document.toObject(YeuCau::class.java)
//                                Log.d("RealtimeSync", "Change detected: REMOVED - $yeuCau")
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    yeuCauDao.delete(yeuCau)
//                                }
//                            }
//
//                            DocumentChange.Type.ADDED,
//                            DocumentChange.Type.MODIFIED -> {
//                                val thoiGianCapNhat = change.document.getLong("thoiGianCapNhat") ?: 0L
//                                if (thoiGianCapNhat > lastSyncTime) {
//                                    val yeuCau = change.document.toObject(YeuCau::class.java)
//                                    Log.d("RealtimeSync", "Change detected: ${change.type} - $yeuCau")
//                                    CoroutineScope(Dispatchers.IO).launch {
//                                        yeuCauDao.insert(yeuCau)
//                                    }
//                                } else {
//                                    Log.d(
//                                        "RealtimeSync",
//                                        "Ignored document id=${change.document.id} (${change.type}) vì thoiGianCapNhat <= lastSyncTime"
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//        listeners.add(listener)
//    }
//private fun startListening(lastSyncTime: Long) {
//    val listener = firestore.collection("yeu_cau")
//        .whereGreaterThan("thoiGianCapNhat", lastSyncTime)
//        .addSnapshotListener { snapshots, e ->
//            if (e != null) {
//                Log.e("RealtimeSync", "Listen failed: ${e.localizedMessage}", e)
//                return@addSnapshotListener
//            }
//
//            if (snapshots != null) {
//                Log.d("RealtimeSync", "Received snapshot with ${snapshots.size()} documents")
//
//                for (change in snapshots.documentChanges) {
//                    when (change.type) {
//                        DocumentChange.Type.REMOVED -> {
//                            //  Với REMOVE thì cứ xử lý luôn
//                            val id = change.document.getLong("id")?.toInt()
//                            if (id != null) {
//                                Log.d("RealtimeSync", "Change detected: REMOVED - id=$id")
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    yeuCauDao.deleteById(id)
//                                }
//                            } else {
//                                Log.w("RealtimeSync", "REMOVED change but no valid id found")
//                            }
//                        }
//
//                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
//                            val thoiGianCapNhat = change.document.getLong("thoiGianCapNhat") ?: 0L
//                            if (thoiGianCapNhat > lastSyncTime) {
//                                val yeuCau = change.document.toObject(YeuCau::class.java)
//                                Log.d("RealtimeSync", "Change detected: ${change.type} - $yeuCau")
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    yeuCauDao.insert(yeuCau)
//                                }
//                            } else {
//                                Log.d("RealtimeSync", "Ignored ${change.type} id=${change.document.id} vì thoiGianCapNhat <= lastSyncTime")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    listeners.add(listener)
//}
private fun <T : Any> startListening(
    collection: String,
    key: String,
    clazz: Class<T>,
    daoInsert: suspend (T) -> Unit,
    daoDeleteById: suspend (Int) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val lastSyncTime = syncMetadataDao.getLastSyncTime(key) ?: 0L
        withContext(Dispatchers.Main) {
            val listener = firestore.collection(collection)
                .whereGreaterThan("thoiGianCapNhat", lastSyncTime)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.e("RealtimeSync", "Listen failed: ${e.localizedMessage}", e)
                        return@addSnapshotListener
                    }

                    if (snapshots != null) {
                        Log.d("RealtimeSync", "[$collection] Received snapshot: ${snapshots.size()} items")
                        for (change in snapshots.documentChanges) {
                            when (change.type) {
                                DocumentChange.Type.REMOVED -> {
                                    val id = change.document.getLong("id")?.toInt()
                                    if (id != null) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            daoDeleteById(id)
                                        }
                                    }
                                }

                                DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                                    val capNhat = change.document.getLong("thoiGianCapNhat") ?: 0L
                                    if (capNhat > lastSyncTime) {
                                        val item = change.document.toObject(clazz)
                                        CoroutineScope(Dispatchers.IO).launch {
                                            daoInsert(item)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            listeners.add(listener)
        }
    }
}



}
