package com.example.imagesearchapplication.service

class ApiHelper(private val apiService: ApiService) {

    suspend fun getImages(key: String, id: String) = apiService.getImages(key, id)

}