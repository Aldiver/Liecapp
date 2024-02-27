package com.adzu.liecapp.api.main.service

import com.adzu.liecapp.api.main.models.EntryInfo
import com.adzu.liecapp.api.main.models.EntryInfoResponse
import retrofit2.Response
import retrofit2.http.GET


interface EntryRecordApiService {
    @GET("entry-records")
    suspend fun getAllEntryRecords(): Response<List<EntryInfo>>
}
