// BaoCaoSuCoViewModel.kt
        package com.example.facilitiesmanagementpj.ui.viewmodel

        import androidx.lifecycle.ViewModel
        import androidx.lifecycle.viewModelScope
        import com.example.facilitiesmanagementpj.data.entity.YeuCau
        import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
        import dagger.hilt.android.lifecycle.HiltViewModel
        import kotlinx.coroutines.flow.Flow
        import kotlinx.coroutines.launch
        import javax.inject.Inject

        @HiltViewModel
        class YeuCauViewModel @Inject constructor(
            private val repository: YeuCauRepository
        ) : ViewModel() {
            val allYeuCau: Flow<List<YeuCau>> = repository.getAllYeuCau()

            fun insert(yeuCau: YeuCau) = viewModelScope.launch {
                repository.insert(yeuCau)
            }
            fun update(yeuCau: YeuCau) = viewModelScope.launch {
                repository.update(yeuCau)
            }
            fun delete(yeuCau: YeuCau) = viewModelScope.launch {
                repository.delete(yeuCau)
            }
        }