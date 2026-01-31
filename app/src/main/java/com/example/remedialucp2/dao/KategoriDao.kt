package com.example.remedialucp2.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.remedialucp2.model.Kategori

@Dao
interface KategoriDao {

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
