package com.example.remedialucp2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Buku(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val judul: String,
    val kategoriId: Int?,
    val deleted: Boolean = false
)