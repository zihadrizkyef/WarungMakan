package com.olsera.repository

import androidx.room.Room
import com.olsera.repository.repo.WarungRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "warung-makan-db"
        ).build()
    }

    single {
        WarungRepository(get())
    }
}