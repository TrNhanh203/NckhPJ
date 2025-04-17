package com.example.facilitiesmanagementpj.data.sync

import android.util.Log

//suspend fun <T> safeSync(
//    name: String,
//    syncAction: suspend () -> Unit
//) {
//    try {
//        Log.d("SYNC", "Bắt đầu sync bảng: $name")
//        syncAction()
//        Log.d("SYNC", "✅ Sync thành công: $name")
//    } catch (e: Exception) {
//        Log.e("SYNC", "❌ Lỗi khi sync bảng: $name", e)
//    }
//}
suspend fun <T> safeSync(name: String, syncAction: suspend () -> List<T>) {
    try {
        Log.d("SYNC", "▶️ Bắt đầu sync: $name")
        val items = syncAction()
        Log.d("SYNC", "✅ Sync $name: ${items.size} items")
    } catch (e: Exception) {
        Log.e("SYNC", "❌ Lỗi khi sync $name", e)
    }
}
