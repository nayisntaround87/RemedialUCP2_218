package com.example.remedialucp2.dao

import androidx.room.*
import com.example.remedialucp2.model.Buku

@Dao
interface BukuDao {

    @Insert
    suspend fun insert(buku: Buku)

    @Query("SELECT * FROM Buku WHERE kategoriId IN (:kategoriIds) AND deleted = 0")
    suspend fun getBukuByKategoriIds(kategoriIds: List<Int>): List<Buku>

    @Query("""
        SELECT COUNT(*) FROM CopyBuku 
        WHERE status = 'dipinjam' 
        AND deleted = 0
        AND bukuId IN (
            SELECT id FROM Buku WHERE kategoriId IN (:kategoriIds) AND deleted = 0
        )
    """)
    suspend fun countBukuDipinjamDiKategori(kategoriIds: List<Int>): Int

    @Query("UPDATE Buku SET deleted = 1 WHERE kategoriId IN (:kategoriIds)")
    suspend fun softDeleteByKategoriIds(kategoriIds: List<Int>)

    @Query("UPDATE Buku SET kategoriId = NULL WHERE kategoriId IN (:kategoriIds)")
    suspend fun lepasKategori(kategoriIds: List<Int>)
}
