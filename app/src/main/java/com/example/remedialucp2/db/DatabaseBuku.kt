package com.example.remedialucp2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.remedialucp2.dao.*
import com.example.remedialucp2.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                )
                .addCallback(DatabaseCallback(CoroutineScope(Dispatchers.IO)))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch {
                    val kategoriDao = it.kategoriDao()
                    kategoriDao.insertAll(
                        Kategori(nama = "Fiksi Ilmiah", parentId = null),
                        Kategori(nama = "Pemrograman", parentId = null),
                        Kategori(nama = "Sejarah", parentId = null)
                    )
                }
            }
        }
    }
}
