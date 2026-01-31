package com.example.remedialucp2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.remedialucp2.model.Kategori
import kotlinx.coroutines.flow.Flow

@Dao
interface KategoriDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg kategori: Kategori)

    @Query("SELECT * FROM Kategori WHERE deleted = 0 ORDER BY nama ASC")
    fun getSemuaKategori(): Flow<List<Kategori>>

    @Query("""
        WITH RECURSIVE sub_kategori(id) AS (
            SELECT id FROM Kategori WHERE id = :parentId
            UNION ALL
            SELECT k.id FROM Kategori k
            JOIN sub_kategori sk ON k.parentId = sk.id
        )
        SELECT * FROM Kategori WHERE id IN (SELECT id FROM sub_kategori)
    """)
    suspend fun getSemuaSubKategori(parentId: Int): List<Kategori>

    @Query("UPDATE Kategori SET deleted = 1 WHERE id = :id")
    suspend fun softDelete(id: Int)
}
