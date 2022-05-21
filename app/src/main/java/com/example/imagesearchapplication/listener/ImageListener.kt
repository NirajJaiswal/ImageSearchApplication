package com.example.imagesearchapplication.listener

import com.example.imagesearchapplication.model.Hit

interface ImageListener {
    fun onImageClick(image:Hit)
}