package com.example.remedialucp2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AuditLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tableName: String,
    val beforeData: String,
    val afterData: String,
    val timestamp: Long
)
