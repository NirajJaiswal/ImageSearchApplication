package com.example.imagesearchapplication.model

data class SearchResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)