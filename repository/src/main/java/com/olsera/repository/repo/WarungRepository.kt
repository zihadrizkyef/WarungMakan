package com.olsera.repository.repo

import com.olsera.repository.AppDatabase
import com.olsera.repository.dao.WarungDao
import com.olsera.repository.model.Warung
import com.olsera.repository.repo.result.GetWarungListResult
import kotlin.math.ceil

class WarungRepository(private val db: AppDatabase) {
    suspend fun saveWarung(warung: Warung): Result<Long> {
        val id = db.warungDao().insertAll(warung).first()
        return Result.success(id)
    }

    suspend fun getWarungList(
        page: Int,
        perPage: Int,
    ): Result<GetWarungListResult> {
        val itemCount = db.warungDao().getWarungCount()
        val pageCount = ceil(itemCount.toDouble() / perPage).toInt()
        val listWarungLocal = db.warungDao().getList(page * perPage, perPage)
        return if (listWarungLocal.isNotEmpty()) {
            Result.success(GetWarungListResult(page, pageCount, perPage, listWarungLocal))
        } else {
            Result.failure(Throwable("Gagal mengambil data"))
        }
    }

    suspend fun getWarungDetail(id: Long): Result<Warung> {
        return try {
            Result.success(db.warungDao().getDetail(id))
        } catch (e: Exception) {
            Result.failure(Throwable("Gagal mengambil data"))
        }
    }

    suspend fun updateWarung(warung: Warung) {
        db.warungDao().update(warung)
    }

    suspend fun deleteWarung(id: Long) {
        db.warungDao().deleteById(id)
    }
}