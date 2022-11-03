package com.lugares.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lugares.data.LugarDao
import com.lugares.model.Lugar
import com.lugares.repository.LugarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {

    val getAlldata: MutableLiveData<List<Lugar>>

    private val repository: LugarRepository = LugarRepository(LugarDao())

    init {
        getAlldata = repository.getAllData
    }

    fun addLugar (lugar: Lugar) {
        repository.addLugar(lugar)
    }

    fun updateLugar (lugar: Lugar) {
        repository.updateLugar(lugar)
    }

    fun deleteLugar (lugar: Lugar) {
        repository.deleteLugar(lugar)
    }
}