package com.example.remedialucp2.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.db.DatabaseBuku
import com.example.remedialucp2.model.Kategori
import com.example.remedialucp2.repositori.KategoriRepository
import com.example.remedialucp2.repositori.OpsiHapusBuku
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RepositoriViewModel(application: Application) : ViewModel() {

    private val kategoriRepository: KategoriRepository

    val semuaKategori: StateFlow<List<Kategori>>

    private val _statusOperasi = MutableStateFlow<String?>(null)
    val statusOperasi: StateFlow<String?> = _statusOperasi

    init {
        val database = DatabaseBuku.getInstance(application)
        kategoriRepository = KategoriRepository(database)
        semuaKategori = kategoriRepository.getSemuaKategori()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun hapusKategori(kategoriId: Int, opsi: OpsiHapusBuku) {
        viewModelScope.launch {
            val berhasil = kategoriRepository.hapusKategoriSecaraAman(kategoriId, opsi)
            if (berhasil) {
                _statusOperasi.value = "Operasi berhasil!"
            } else {
                _statusOperasi.value = "Gagal: Terdapat buku yang masih dipinjam dalam kategori ini."
            }
        }
    }

    fun resetStatus() {
        _statusOperasi.value = null
    }

    companion object {
        fun Factory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(RepositoriViewModel::class.java)) {
                        return RepositoriViewModel(application) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
