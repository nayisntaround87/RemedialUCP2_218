package com.example.remedialucp2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.remedialucp2.dao.*
import com.example.remedialucp2.model.*

@Database(
    entities = [
        Buku::class,
        CopyBuku::class,
        Author::class,
        BookAuthorCrossRef::class,
        Kategori::class,
        AuditLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DatabaseBuku : RoomDatabase() {

    abstract fun bukuDao(): BukuDao
    abstract fun kategoriDao(): KategoriDao
    abstract fun auditLogDao(): AuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseBuku? = null

        fun getInstance(context: Context): DatabaseBuku {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseBuku::class.java,
                    "database_buku"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
