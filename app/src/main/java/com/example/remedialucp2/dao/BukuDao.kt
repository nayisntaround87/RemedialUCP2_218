package com.example.remedialucp2.dao

import androidx.room.*
import com.example.remedialucp2.model.Buku

@Dao
interface BukuDao {

    @Insert
    suspend fun insert(buku: Buku)

    @Query("""
        SELECT COUNT(*) FROM CopyBuku 
        WHERE status = 'dipinjam' 
        AND bukuId IN (:bukuIds)
    """)
    suspend fun countBukuDipinjam(bukuIds: List<Int>): Int

    @Query("UPDATE Buku SET deleted = 1 WHERE kategoriId = :kategoriId")
    suspend fun softDeleteByKategori(kategoriId: Int)

    @Query("UPDATE Buku SET kategoriId = NULL WHERE kategoriId = :kategoriId")
    suspend fun lepasKategori(kategoriId: Int)
}
