package com.adzu.liecapp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.adzu.liecapp.api.main.models.EntryInfo
import com.adzu.liecapp.api.main.models.EntryInfoResponse
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

    fun getAllEntryRecords(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _allEntryRecordsResponse,
        coroutinesErrorHandler
    ) {
        entryRecordRepository.getAllEntryRecords()
    }
}
