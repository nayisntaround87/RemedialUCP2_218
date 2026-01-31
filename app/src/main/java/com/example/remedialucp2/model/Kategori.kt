package com.example.remedialucp2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Kategori(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val parentId: Int?,
    val deleted: Boolean = false
)
