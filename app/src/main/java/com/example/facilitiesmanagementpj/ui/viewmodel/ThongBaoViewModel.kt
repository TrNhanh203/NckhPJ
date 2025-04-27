package com.example.facilitiesmanagementpj.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.ThongBao
import com.example.facilitiesmanagementpj.data.repository.ThongBaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ThongBaoViewModel @Inject constructor(
    private val thongBaoRepository: ThongBaoRepository
) : ViewModel() {

    private val _newThongBao = MutableSharedFlow<ThongBao>()
    val newThongBao = _newThongBao.asSharedFlow()

    fun createAndPushThongBao(thongBao: ThongBao) {
        viewModelScope.launch {
            // 1. Lưu vào Room
            thongBaoRepository.insertThongBao(thongBao)

            // 2. Emit để hiện banner
            _newThongBao.emit(thongBao)
        }
    }

    fun pushFakeThongBao() {
        viewModelScope.launch {
            val fakeThongBao = ThongBao(
                id = UUID.randomUUID().toString(),
                nguoiNhanId = 1, // Giả định user id = 1
                tieuDe = "Thông báo giả",
                noiDung = "Đây là một thông báo test để kiểm tra banner.",
                loai = "he_thong"
            )

            _newThongBao.emit(fakeThongBao)
        }
    }

}
