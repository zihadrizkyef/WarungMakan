package com.olsera.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Warung(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val imageUrl: String = "",
    val address: String = "",
    val city: String = "",
    val zipCode: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val desc: String = "",
    val isActive: Boolean = true,
)
