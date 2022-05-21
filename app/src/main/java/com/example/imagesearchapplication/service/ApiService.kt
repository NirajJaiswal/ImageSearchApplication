package com.example.imagesearchapplication.service

import com.example.imagesearchapplication.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getImages(@Query("key") apiKey:String, @Query("q") id:String
    ): SearchResponse

}