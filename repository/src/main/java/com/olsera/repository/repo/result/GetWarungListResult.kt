package com.olsera.repository.repo.result

import com.olsera.repository.model.Warung

data class GetWarungListResult(
    val page: Int,
    val totalPage: Int,
    val itemPerPage: Int,
    val items: List<Warung>,
)