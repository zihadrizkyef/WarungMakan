package com.olsera.repository.dao

import androidx.room.*
import com.olsera.repository.model.Warung

@Dao
interface WarungDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg warung: Warung): List<Long>

    @Query("SELECT * FROM Warung")
    suspend fun getAll(): List<Warung>

    @Query("SELECT * FROM Warung LIMIT :offset, :perPage")
    suspend fun getList(offset: Int, perPage: Int): List<Warung>

    @Query("SELECT * FROM Warung WHERE isActive = :isActive LIMIT :offset, :perPage")
    suspend fun getList(offset: Int, perPage: Int, isActive: Boolean): List<Warung>

    @Query("SELECT * FROM Warung WHERE id = :id")
    suspend fun getDetail(id: Long): Warung

    @Update
    suspend fun update(warung: Warung)

    @Delete
    suspend fun delete(warung: Warung)

    @Query("DELETE FROM Warung WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(id) FROM Warung")
    suspend fun getWarungCount(): Int

    @Query("SELECT COUNT(id) FROM Warung WHERE isActive")
    suspend fun getActiveWarungCount(): Int

    @Query("SELECT COUNT(id) FROM Warung WHERE NOT isActive")
    suspend fun getInactiveWarungCount(): Int
}