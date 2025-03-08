// AnhMinhChungLamViecViewModel.kt
    package com.example.facilitiesmanagementpj.ui.viewmodel

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungLamViec
    import com.example.facilitiesmanagementpj.data.repository.AnhMinhChungLamViecRepository
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    @HiltViewModel
    class AnhMinhChungLamViecViewModel @Inject constructor(
        private val repository: AnhMinhChungLamViecRepository
    ) : ViewModel() {
        val allAnhMinhChungLamViec: Flow<List<AnhMinhChungLamViec>> = repository.getAllAnhMinhChungLamViec()

        fun insert(anhMinhChungLamViec: AnhMinhChungLamViec) = viewModelScope.launch {
            repository.insert(anhMinhChungLamViec)
        }
        fun update(anhMinhChungLamViec: AnhMinhChungLamViec) = viewModelScope.launch {
            repository.update(anhMinhChungLamViec)
        }
        fun delete(anhMinhChungLamViec: AnhMinhChungLamViec) = viewModelScope.launch {
            repository.delete(anhMinhChungLamViec)
        }
    }