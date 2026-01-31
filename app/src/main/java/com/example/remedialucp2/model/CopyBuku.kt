package com.example.remedialucp2.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Buku::class,
            parentColumns = ["id"],
            childColumns = ["bukuId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CopyBuku(
    @PrimaryKey
    val copyId: String,
    val bukuId: Int,
    val status: String = "Tersedia"
)
