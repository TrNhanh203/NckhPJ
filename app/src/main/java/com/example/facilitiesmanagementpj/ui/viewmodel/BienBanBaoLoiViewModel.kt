// BienBanBaoLoiViewModel.kt
    package com.example.facilitiesmanagementpj.ui.viewmodel

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.facilitiesmanagementpj.data.entity.BienBanBaoLoi
    import com.example.facilitiesmanagementpj.data.repository.BienBanBaoLoiRepository
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    @HiltViewModel
    class BienBanBaoLoiViewModel @Inject constructor(
        private val repository: BienBanBaoLoiRepository
    ) : ViewModel() {
        fun getBienBanByBaoCao(baoCaoId: Int): Flow<List<BienBanBaoLoi>> = repository.getBienBanByBaoCao(baoCaoId)

        fun insert(bienBanBaoLoi: BienBanBaoLoi) {
            viewModelScope.launch {
                repository.insert(bienBanBaoLoi)
            }
        }

        fun update(bienBanBaoLoi: BienBanBaoLoi) {
            viewModelScope.launch {
                repository.update(bienBanBaoLoi)
            }
        }

        fun delete(bienBanBaoLoi: BienBanBaoLoi) {
            viewModelScope.launch {
                repository.delete(bienBanBaoLoi)
            }
        }
    }