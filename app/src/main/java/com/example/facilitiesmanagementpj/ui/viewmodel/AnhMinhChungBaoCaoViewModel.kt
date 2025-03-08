// AnhMinhChungBaoCaoViewModel.kt
    package com.example.facilitiesmanagementpj.ui.viewmodel

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungBaoCao
    import com.example.facilitiesmanagementpj.data.repository.AnhMinhChungBaoCaoRepository
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    @HiltViewModel
    class AnhMinhChungBaoCaoViewModel @Inject constructor(
        private val repository: AnhMinhChungBaoCaoRepository
    ) : ViewModel() {
        val allAnhMinhChungBaoCao: Flow<List<AnhMinhChungBaoCao>> = repository.getAllAnhMinhChungBaoCao()

        fun insert(anhMinhChungBaoCao: AnhMinhChungBaoCao) = viewModelScope.launch {
            repository.insert(anhMinhChungBaoCao)
        }
        fun update(anhMinhChungBaoCao: AnhMinhChungBaoCao) = viewModelScope.launch {
            repository.update(anhMinhChungBaoCao)
        }
        fun delete(anhMinhChungBaoCao: AnhMinhChungBaoCao) = viewModelScope.launch {
            repository.delete(anhMinhChungBaoCao)
        }
    }