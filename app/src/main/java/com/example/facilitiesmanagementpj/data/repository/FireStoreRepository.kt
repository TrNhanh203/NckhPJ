package com.example.facilitiesmanagementpj.data.repository

import android.util.Log
import com.example.facilitiesmanagementpj.data.entity.BaiViet
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class BaiVietFirestoreRepository @Inject constructor(){

    private val db = Firebase.firestore
    private val baiVietRef = db.collection("bai_viet")

    fun pushBaiVietToCloud(baiViet: BaiViet) {
        val baiVietMap = hashMapOf(
            "id" to baiViet.id,
            "tieuDe" to baiViet.tieuDe,
            "moTa" to baiViet.moTa,
            "anhDaiDien" to baiViet.anhDaiDien,
            "link" to baiViet.link,
            "noiDungHtml" to baiViet.noiDungHtml,
            "thoiGianTao" to baiViet.thoiGianTao,
            "nguoiTaoId" to baiViet.nguoiTaoId
        )

        baiVietRef.document(baiViet.id.toString())
            .set(baiVietMap)
            .addOnSuccessListener {
                Log.d("SyncBaiViet", "Đồng bộ thành công lên Firestore.")
            }
            .addOnFailureListener {
                Log.e("SyncBaiViet", "Lỗi khi đồng bộ: ${it.message}")
            }
    }



}
