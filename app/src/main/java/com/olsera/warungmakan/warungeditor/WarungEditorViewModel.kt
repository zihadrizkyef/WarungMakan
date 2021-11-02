package com.olsera.warungmakan.warungeditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olsera.repository.model.Warung
import com.olsera.repository.repo.WarungRepository
import kotlinx.coroutines.launch

class WarungEditorViewModel(private val repository: WarungRepository) : ViewModel() {
    val warungDetail = MutableLiveData<Warung>()
    val warungIsSaved = MutableLiveData<Boolean>()
    val warungIsDeleted = MutableLiveData<Boolean>()

    fun getWarungDetail(id: Long) = viewModelScope.launch {
        val result = repository.getWarungDetail(id)
        val warung = result.getOrThrow()
        warungDetail.value = warung
    }

    fun saveNewWarung(warung: Warung) = viewModelScope.launch {
        repository.saveWarung(warung)
        warungIsSaved.value = true
    }

    fun updateWarung(warung: Warung) = viewModelScope.launch {
        repository.updateWarung(warung)
        warungIsSaved.value = true
    }

    fun deleteWarung(id: Long) = viewModelScope.launch {
        repository.deleteWarung(id)
        warungIsDeleted.value = true
    }
}