package com.example.facilitiesmanagementpj.ui.viewmodel

                import androidx.lifecycle.ViewModel
                import androidx.lifecycle.viewModelScope
                import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCauWithThietBiAndLoaiThietBi
                import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
                import com.example.facilitiesmanagementpj.data.entity.LoaiThietBi
                import com.example.facilitiesmanagementpj.data.repository.LoaiThietBiRepository
                import com.example.facilitiesmanagementpj.data.utils.LoaiYeuCau
                import dagger.hilt.android.lifecycle.HiltViewModel
                import kotlinx.coroutines.flow.*
                import kotlinx.coroutines.launch
                import javax.inject.Inject

                @HiltViewModel
                class AdminRequestDetailViewModel @Inject constructor(
                    private val repository: YeuCauRepository,
                    private val loaiThietBiRepository: LoaiThietBiRepository
                ) : ViewModel() {

                    private val _chiTietYeuCauList = MutableStateFlow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>>(emptyList())
                    val chiTietYeuCauList: StateFlow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>> = _chiTietYeuCauList

                    private val _deviceTypes = MutableStateFlow<List<LoaiThietBi>>(emptyList())
                    val deviceTypes: StateFlow<List<LoaiThietBi>> = _deviceTypes

                    private val _selectedDeviceType = MutableStateFlow<String?>(null)
                    val selectedDeviceType: StateFlow<String?> = _selectedDeviceType

                    private val _selectedRequestType = MutableStateFlow<String?>(null)
                    val selectedRequestType: StateFlow<String?> = _selectedRequestType

                    val requestTypes = LoaiYeuCau.ALL

                    fun loadChiTietYeuCau(yeuCauId: Int) {
                        viewModelScope.launch {
                            repository.getChiTietYeuCauWithThietBiAndLoaiThietBi(yeuCauId).collect {
                                _chiTietYeuCauList.value = it
                            }
                        }
                    }

                    fun loadDeviceTypes() {
                        viewModelScope.launch {
                            loaiThietBiRepository.getAllLoaiThietBi().collect {
                                _deviceTypes.value = it
                            }
                        }
                    }

                    fun setDeviceTypeFilter(deviceType: String?) {
                        _selectedDeviceType.value = deviceType
                    }

                    fun setRequestTypeFilter(requestType: String?) {
                        _selectedRequestType.value = requestType
                    }

                    data class FilterCriteria(
                        val deviceType: String?,
                        val requestType: String?
                    )

                    val filteredChiTietYeuCauList: StateFlow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>> = combine(
                        chiTietYeuCauList,
                        combine(selectedDeviceType, selectedRequestType) { deviceType, requestType ->
                            FilterCriteria(deviceType, requestType)
                        }
                    ) { list, filterCriteria ->
                        list.filter {
                            (filterCriteria.deviceType == null || filterCriteria.deviceType == "Tất cả" || it.tenLoaiThietBi == filterCriteria.deviceType) &&
                            (filterCriteria.requestType == null || filterCriteria.requestType == "Tất cả" || it.loaiYeuCau == filterCriteria.requestType)
                        }
                    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
                }