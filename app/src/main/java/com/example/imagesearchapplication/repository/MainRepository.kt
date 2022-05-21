package com.example.imagesearchapplication.repository

import com.example.imagesearchapplication.service.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getImages(key: String, id: String) = apiHelper.getImages(key, id)

}