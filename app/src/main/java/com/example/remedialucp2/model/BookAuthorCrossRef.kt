package com.example.remedialucp2.model

import androidx.room.Entity

@Entity(primaryKeys = ["bukuId", "authorId"])
data class BookAuthorCrossRef(
    val bukuId: Int,
    val authorId: Int
)
