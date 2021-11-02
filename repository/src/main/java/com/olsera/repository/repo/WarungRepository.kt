package com.olsera.repository.repo

import android.os.Handler
import com.olsera.repository.AppDatabase
import com.olsera.repository.dao.WarungDao
import com.olsera.repository.model.Warung
import com.olsera.repository.repo.result.GetWarungCountResult
import com.olsera.repository.repo.result.GetWarungListResult
import kotlinx.coroutines.delay
import kotlin.math.ceil
import kotlin.random.Random

class WarungRepository(private val db: AppDatabase) {
    suspend fun saveWarung(warung: Warung): Result<Long> {
        delay(Random.nextLong(500L))
        val id = db.warungDao().insertAll(warung).first()
        return Result.success(id)
    }

    /**
     * [isActive] is for filtering the [Warung.isActive]. Pass null if you don't want to do any filter
     */
    suspend fun getWarungList(
        page: Int,
        perPage: Int,
        isActive: Boolean?,
    ): Result<GetWarungListResult> {
        delay(Random.nextLong(700))
        val itemCount = db.warungDao().getWarungCount()
        val pageCount = ceil(itemCount.toDouble() / perPage).toInt()
        val listWarungLocal = if (isActive != null) {
            db.warungDao().getList(page * perPage, perPage, isActive)
        } else {
            db.warungDao().getList(page * perPage, perPage)
        }
        return if (listWarungLocal.isNotEmpty()) {
            Result.success(GetWarungListResult(page, pageCount, perPage, listWarungLocal))
        } else {
            Result.failure(Throwable("Gagal mengambil data"))
        }
    }

    suspend fun getWarungDetail(id: Long): Result<Warung> {
        return try {
            delay(Random.nextLong(500L))
            Result.success(db.warungDao().getDetail(id))
        } catch (e: Exception) {
            Result.failure(Throwable("Gagal mengambil data"))
        }
    }

    suspend fun updateWarung(warung: Warung) {
        delay(Random.nextLong(500L))
        db.warungDao().update(warung)
    }

    suspend fun deleteWarung(id: Long) {
        delay(Random.nextLong(500L))
        db.warungDao().deleteById(id)
    }

    suspend fun getWarungCount(): GetWarungCountResult {
        val total = db.warungDao().getWarungCount()
        val active = db.warungDao().getActiveWarungCount()
        val inactive = db.warungDao().getInactiveWarungCount()

        return GetWarungCountResult(total, active, inactive)
    }
}