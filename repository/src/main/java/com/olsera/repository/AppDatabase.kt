package com.olsera.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.olsera.repository.dao.WarungDao
import com.olsera.repository.model.Warung

@Database(entities = [Warung::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun warungDao(): WarungDao
}