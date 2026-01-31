package com.example.remedialucp2.repositori

import androidx.room.withTransaction
import com.example.remedialucp2.db.DatabaseBuku
import com.example.remedialucp2.model.AuditLog

class Repositori(private val db: DatabaseBuku) {

    private val bukuDao = db.bukuDao()
    private val kategoriDao = db.kategoriDao()
    private val auditLogDao = db.auditLogDao()

    suspend fun hapusKategoriAman(kategoriId: Int, hapusBuku: Boolean) {
        db.withTransaction {

            val semuaKategori = kategoriDao.getSemuaSubKategori(kategoriId)
            val kategoriIds = semuaKategori.map { it.id }

            val dipinjam = bukuDao.countBukuDipinjamDiKategori(kategoriIds)
            if (dipinjam > 0) {
                throw Exception("Rollback: masih ada buku dipinjam")
            }

            if (hapusBuku) {
                bukuDao.softDeleteByKategoriIds(kategoriIds)
            } else {
                bukuDao.lepasKategori(kategoriIds)
            }

            kategoriIds.forEach { id ->
                kategoriDao.softDelete(id)
            }

            auditLogDao.insert(
                AuditLog(
                    tableName = "Kategori",
                    beforeData = "Kategori id=$kategoriId dan semua sub-kategorinya telah diproses.",
                    afterData = "Semua kategori dan sub-kategori yang relevan ditandai sebagai dihapus.",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
