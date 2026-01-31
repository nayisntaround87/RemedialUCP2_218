package com.example.remedialucp2.repositori

import androidx.room.withTransaction
import com.example.remedialucp2.dao.AuditLogDao
import com.example.remedialucp2.dao.BukuDao
import com.example.remedialucp2.dao.KategoriDao
import com.example.remedialucp2.db.DatabaseBuku
import com.example.remedialucp2.model.AuditLog
import com.example.remedialucp2.model.Buku
import com.example.remedialucp2.model.Kategori
import kotlinx.coroutines.flow.Flow
import java.util.Date

enum class OpsiHapusBuku {
    HAPUS_BUKU,
    LEPAS_KATEGORI
}

class KategoriRepository(private val db: DatabaseBuku) {

    private val kategoriDao: KategoriDao = db.kategoriDao()
    private val bukuDao: BukuDao = db.bukuDao()
    private val auditLogDao: AuditLogDao = db.auditLogDao()

    fun getSemuaKategori(): Flow<List<Kategori>> {
        return kategoriDao.getSemuaKategori()
    }

    suspend fun hapusKategoriSecaraAman(kategoriId: Int, opsiHapusBuku: OpsiHapusBuku): Boolean {
        return db.withTransaction {

            val semuaKategoriTerdampak = kategoriDao.getSemuaSubKategori(kategoriId)
            val semuaIdKategori = semuaKategoriTerdampak.map { it.id }

            val jumlahBukuDipinjam = bukuDao.countBukuDipinjamDiKategori(semuaIdKategori)
            if (jumlahBukuDipinjam > 0) {

                return@withTransaction false
            }

            val bukuTerdampak = bukuDao.getBukuByKategoriIds(semuaIdKategori)
            val dataSebelum = "Kategori: ${semuaKategoriTerdampak.joinToString { it.nama }}. Buku: ${bukuTerdampak.joinToString { it.judul }}"

            val dataSesudahBuku = when (opsiHapusBuku) {
                OpsiHapusBuku.HAPUS_BUKU -> {
                    bukuDao.softDeleteByKategoriIds(semuaIdKategori)
                    "Buku-buku terkait ditandai sebagai dihapus."
                }
                OpsiHapusBuku.LEPAS_KATEGORI -> {
                    bukuDao.lepasKategori(semuaIdKategori)
                    "Buku-buku terkait dilepas menjadi 'Tanpa Kategori'."
                }
            }

            semuaIdKategori.forEach { id ->
                kategoriDao.softDelete(id)
            }

            val dataSesudahFinal = "Kategori terkait ditandai sebagai dihapus. $dataSesudahBuku"

            catatLogAudit("Penghapusan Kategori", dataSebelum, dataSesudahFinal)

            return@withTransaction true
        }
    }

    private suspend fun catatLogAudit(aksi: String, dataSebelum: String, dataSesudah: String) {
        val log = AuditLog(
            tableName = aksi,
            timestamp = Date().time,
            beforeData = dataSebelum,
            afterData = dataSesudah
        )
        auditLogDao.insert(log)
    }
}
