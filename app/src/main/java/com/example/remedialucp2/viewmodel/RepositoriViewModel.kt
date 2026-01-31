package com.example.remedialucp2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.repositori.Repositori
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepositoriViewModel(
    private val repositori: Repositori
) : ViewModel() {

    fun hapusKategori(id: Int, hapusBuku: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repositori.hapusKategoriAman(id, hapusBuku)
        }
    }
}
