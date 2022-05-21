package com.example.imagesearchapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.imagesearchapplication.repository.MainRepository
import com.example.imagesearchapplication.utils.Constant
import com.example.imagesearchapplication.utils.Resource
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val key: String = Constant.key
    var id: String? = null


    fun getValue(id: String) {
        this.id = id
    }

    fun getImages() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getImages(key, id!!)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}