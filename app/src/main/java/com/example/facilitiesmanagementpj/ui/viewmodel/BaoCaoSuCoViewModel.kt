// BaoCaoSuCoViewModel.kt
        package com.example.facilitiesmanagementpj.ui.viewmodel

        import androidx.lifecycle.ViewModel
        import androidx.lifecycle.viewModelScope
        import com.example.facilitiesmanagementpj.data.entity.BaoCaoSuCo
        import com.example.facilitiesmanagementpj.data.repository.BaoCaoSuCoRepository
        import dagger.hilt.android.lifecycle.HiltViewModel
        import kotlinx.coroutines.flow.Flow
        import kotlinx.coroutines.launch
        import javax.inject.Inject

        @HiltViewModel
        class BaoCaoSuCoViewModel @Inject constructor(
            private val repository: BaoCaoSuCoRepository
        ) : ViewModel() {
            val allBaoCaoSuCo: Flow<List<BaoCaoSuCo>> = repository.getAllBaoCaoSuCo()

            fun insert(baoCaoSuCo: BaoCaoSuCo) = viewModelScope.launch {
                repository.insert(baoCaoSuCo)
            }
            fun update(baoCaoSuCo: BaoCaoSuCo) = viewModelScope.launch {
                repository.update(baoCaoSuCo)
            }
            fun delete(baoCaoSuCo: BaoCaoSuCo) = viewModelScope.launch {
                repository.delete(baoCaoSuCo)
            }
        }