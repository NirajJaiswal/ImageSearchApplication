package com.example.imagesearchapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.imagesearchapplication.repository.MainRepository
import com.example.imagesearchapplication.utils.Constant
import com.example.imagesearchapplication.utils.Resource
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val key: String = Constant.key
    var id: String? = null
    private var result = MutableLiveData<String>()


    fun getValue(id: String) {
        result.postValue(id)
    }

    val data = result.switchMap {
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = mainRepository.getImages(key, it)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


}