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

            val dipinjam = bukuDao.countBukuDipinjam(kategoriIds)
            if (dipinjam > 0) {
                throw Exception("Rollback: masih ada buku dipinjam")
            }

            if (hapusBuku) {
                bukuDao.softDeleteByKategori(kategoriId)
            } else {
                bukuDao.lepasKategori(kategoriId)
            }

            kategoriDao.softDelete(kategoriId)

            auditLogDao.insert(
                AuditLog(
                    tableName = "Kategori",
                    beforeData = "Kategori id=$kategoriId",
                    afterData = "deleted",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
