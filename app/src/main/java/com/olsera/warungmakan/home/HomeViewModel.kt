package com.olsera.warungmakan.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olsera.repository.model.Warung
import com.olsera.repository.repo.WarungRepository
import com.thedeanda.lorem.LoremIpsum
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeViewModel(private val repository: WarungRepository) : ViewModel() {
    init {
        generateRandom()
    }
    fun generateRandom() = viewModelScope.launch {
        val lorem = LoremIpsum.getInstance()
        repeat(15) {
            val warung = Warung(
                0,
                lorem.name,
                "https://i.picsum.photos/id/${Random.nextInt(500)}/500/500",
                lorem.city,
                lorem.city,
                lorem.zipCode,
                Random.nextDouble(5.0),
                Random.nextDouble(5.0),
                lorem.getWords(5, 15),
                Random.nextInt(5) == 1
            )
            repository.saveWarung(warung)
        }
    }

    fun getList() = viewModelScope.launch {
        val result = repository.getWarungList(0, 12)
        val data = result.getOrNull()
        Log.i("AOEU", "data null = ${data == null}")
        data?.let { it ->
            it.items.forEach {
                Log.i("AOEU", "$it")
            }
        }
    }
}