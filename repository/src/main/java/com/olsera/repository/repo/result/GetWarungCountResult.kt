package com.olsera.repository.repo.result

data class GetWarungCountResult(
    val total: Int,
    val active: Int,
    val inActive: Int,
)