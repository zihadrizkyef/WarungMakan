package com.olsera.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Warung(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var imageUrl: String = "",
    var address: String = "",
    var city: String = "",
    var zipCode: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var desc: String = "",
    var isActive: Boolean = true,
)
