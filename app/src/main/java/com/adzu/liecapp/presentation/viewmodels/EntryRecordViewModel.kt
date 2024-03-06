package com.adzu.liecapp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.adzu.liecapp.api.main.models.EntryCount
import com.adzu.liecapp.api.main.models.EntryInfo
import com.adzu.liecapp.api.main.models.EntryInfoResponse
import com.adzu.liecapp.api.main.models.VehicleInfo
import com.adzu.liecapp.api.main.repository.EntryRecordRepository
import com.adzu.liecapp.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EntryRecordViewModel @Inject constructor(
    private val entryRecordRepository: EntryRecordRepository,
) : BaseViewModel() {

    private val _allEntryRecordsResponse = MutableLiveData<ApiResponse<List<EntryInfo>>>()
    val allEntryRecordsResponse = _allEntryRecordsResponse

    private val _totalEntries = MutableLiveData<ApiResponse<EntryCount>>()
    val totalEntries = _totalEntries

    fun getAllEntryRecords(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _allEntryRecordsResponse,
        coroutinesErrorHandler
    ) {
        entryRecordRepository.getAllEntryRecords()
    }

    fun getTotalEntryRecords(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _totalEntries,
        coroutinesErrorHandler
    ) {
        entryRecordRepository.getTotalEntryRecords()
    }
}
