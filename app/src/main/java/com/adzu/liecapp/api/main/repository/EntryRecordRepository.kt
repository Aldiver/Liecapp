package com.adzu.liecapp.api.main.repository

import com.adzu.liecapp.api.main.service.EntryRecordApiService
import com.adzu.liecapp.utils.apiRequestFlow
import javax.inject.Inject

class EntryRecordRepository @Inject constructor(
    private val entryRecordApiService: EntryRecordApiService,
) {
    fun getAllEntryRecords() = apiRequestFlow {
        entryRecordApiService.getAllEntryRecords()
    }

    fun getTotalEntryRecords() = apiRequestFlow {
        entryRecordApiService.getTotalEntryRecords()
    }
}

