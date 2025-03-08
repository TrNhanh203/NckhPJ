
package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 19. DayViewModel
@HiltViewModel
class DayViewModel @Inject constructor(private val repository: DayRepository) : ViewModel() {
    val allDay: Flow<List<Day>> = repository.getAllDay()

    fun insert(day: Day) = viewModelScope.launch {
        repository.insert(day)
    }
    fun update(day: Day) = viewModelScope.launch {
        repository.update(day)
    }
    fun delete(day: Day) = viewModelScope.launch {
        repository.delete(day)
    }
}