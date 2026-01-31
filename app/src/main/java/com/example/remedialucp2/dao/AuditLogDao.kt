package com.example.remedialucp2.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.remedialucp2.model.AuditLog

@Dao
interface AuditLogDao {

    @Insert
    suspend fun insert(log: AuditLog)
}
