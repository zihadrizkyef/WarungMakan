package com.olsera.warungmakan.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olsera.repository.model.Warung
import com.olsera.repository.repo.WarungRepository
import com.olsera.repository.repo.result.GetWarungCountResult
import com.thedeanda.lorem.LoremIpsum
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeViewModel(private val repository: WarungRepository) : ViewModel() {
    val initialList = MutableLiveData<List<Warung>>()
    val loadMoreList = MutableLiveData<List<Warung>>()
    val warungCount = MutableLiveData<GetWarungCountResult>()

    var currentPage = 0
    var perPageParam = 20
    var pageParam = 0
    var isActiveParam: Boolean? = null

    var isReachBottom = false
    val isLoadingData = MutableLiveData<Boolean>()

    /**
     * [isActive] is for filtering the [Warung.isActive]. Pass null if you don't want to do any filter
     */
    fun getList() = viewModelScope.launch {
        isLoadingData.value = true
        val result = repository.getWarungList(pageParam, perPageParam, isActiveParam)
        if (result.isSuccess) {
            val data = result.getOrThrow()
            currentPage = data.page

            if (data.page == 0) {
                initialList.value = data.items
            } else {
                loadMoreList.value = data.items
            }

            isReachBottom = data.page >= data.totalPage-1 || data.items.count() < perPageParam
        }
        isLoadingData.value = false
    }

    fun getWarungCount() = viewModelScope.launch {
        warungCount.value = repository.getWarungCount()
    }

    //init{generateRandom()}
    private fun generateRandom() = viewModelScope.launch {
        val count = Random.nextInt(70, 150)
        val lorem = LoremIpsum.getInstance()
        repeat(count) {
            repository.saveWarung(
                Warung(
                    0,
                    lorem.name,
                    "https://picsum.photos/id/${Random.nextInt(500)}/200/300",
                    lorem.city,
                    lorem.country,
                    lorem.zipCode,
                    (-90+Random.nextInt(180)).toDouble(),
                    (-180+Random.nextInt(360)).toDouble(),
                    lorem.getParagraphs(1, 2),
                    Random.nextInt(5) != 1
                )
            )
        }
    }
}