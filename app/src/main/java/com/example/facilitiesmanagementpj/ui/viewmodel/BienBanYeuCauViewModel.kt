// BienBanBaoLoiViewModel.kt
    package com.example.facilitiesmanagementpj.ui.viewmodel

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.facilitiesmanagementpj.data.entity.BienBanYeuCau
    import com.example.facilitiesmanagementpj.data.repository.BienBanYeuCauRepository
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    @HiltViewModel
    class BienBanYeuCauViewModel @Inject constructor(
        private val repository: BienBanYeuCauRepository
    ) : ViewModel() {
        fun getBienBanByYeuCau(yeuCauId: Int): Flow<List<BienBanYeuCau>> = repository.getBienBanByYeuCau(yeuCauId)

        fun insert(bienBanYeuCau: BienBanYeuCau) {
            viewModelScope.launch {
                repository.insert(bienBanYeuCau)
            }
        }

        fun update(bienBanYeuCau: BienBanYeuCau) {
            viewModelScope.launch {
                repository.update(bienBanYeuCau)
            }
        }

        fun delete(bienBanYeuCau: BienBanYeuCau) {
            viewModelScope.launch {
                repository.delete(bienBanYeuCau)
            }
        }
    }