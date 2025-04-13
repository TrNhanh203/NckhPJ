package com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.BaiViet
import com.example.facilitiesmanagementpj.data.repository.BaiVietRepository
import com.example.facilitiesmanagementpj.data.utils.deleteFileFromFirebaseStorage
import com.example.facilitiesmanagementpj.data.utils.uploadFileToFirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BaiVietViewModel @Inject constructor(
    private val repo: BaiVietRepository
) : ViewModel() {

    val dsBaiViet = repo.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _editingItem = MutableStateFlow<BaiViet?>(null)
    val editingItem: StateFlow<BaiViet?> = _editingItem

    fun setEditing(item: BaiViet?) {
        _editingItem.value = item
    }

    /**
     * Lưu bài viết mới hoặc cập nhật bài viết cũ
     * - Nếu ảnh mới được chọn → upload ảnh mới
     * - Nếu bài viết đã có ảnh cũ → xóa ảnh cũ trên Firebase
     */
    fun saveBaiViet(
        tieuDe: String,
        moTa: String,
        link: String?,
        imageUri: Uri?,
        context: Context,
        onDone: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val current = editingItem.value
                var finalImageUrl = current?.anhDaiDien ?: ""

                // Nếu chọn ảnh mới
                if (imageUri != null) {
                    // Xoá ảnh cũ (nếu có)
                    if (finalImageUrl.isNotBlank()) {
                        deleteFileFromFirebaseStorage(finalImageUrl)
                    }

                    // Upload ảnh mới
                    val fileName = "article_${System.currentTimeMillis()}.jpg"
                    val newUrl = uploadFileToFirebaseStorage(imageUri, fileName, "bai_viet")

                    if (newUrl == null) {
                        onError("Tải ảnh thất bại")
                        return@launch
                    }

                    finalImageUrl = newUrl
                }

                // Lưu vào Room
                val baiViet = BaiViet(
                    id = current?.id ?: 0,
                    tieuDe = tieuDe,
                    moTa = moTa,
                    anhDaiDien = finalImageUrl,
                    link = link?.takeIf { it.isNotBlank() },
                    noiDungHtml = null,
                    nguoiTaoId = current?.nguoiTaoId
                )

                repo.insert(baiViet)
                onDone()
            } catch (e: Exception) {
                onError("Lỗi: ${e.localizedMessage}")
            }
        }
    }

    fun loadBaiVietById(id: Int) {
        viewModelScope.launch {
            val item = repo.getAll().first().find { it.id == id }
            _editingItem.value = item
        }
    }


    suspend fun delete(item: BaiViet) {
        // Xoá ảnh khỏi Firebase
        if (item.anhDaiDien.isNotBlank()) {
            deleteFileFromFirebaseStorage(item.anhDaiDien)
        }
        repo.delete(item)
    }
}
