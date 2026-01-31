package com.example.remedialucp2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.remedialucp2.model.Kategori
import com.example.remedialucp2.repositori.OpsiHapusBuku
import com.example.remedialucp2.ui.theme.RemedialUCP2Theme
import com.example.remedialucp2.viewmodel.RepositoriViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: RepositoriViewModel by viewModels { 
        RepositoriViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RemedialUCP2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KategoriScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun KategoriScreen(viewModel: RepositoriViewModel) {
    val kategoriList by viewModel.semuaKategori.collectAsState()
    val status by viewModel.statusOperasi.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf<Kategori?>(null) }

    // Tampilkan Toast saat status berubah
    LaunchedEffect(status) {
        status?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.resetStatus()
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (kategoriList.isEmpty()) {
            Text("Tidak ada kategori.", modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(kategoriList) { kategori ->
                    KategoriItem(kategori = kategori, onDeleteClick = { showDialog = it })
                }
            }
        }
    }

    // Dialog Konfirmasi Hapus
    showDialog?.let { kategori ->
        DialogKonfirmasiHapus(
            kategori = kategori,
            onDismiss = { showDialog = null },
            onConfirm = { opsi ->
                viewModel.hapusKategori(kategori.id, opsi)
                showDialog = null
            }
        )
    }
}

@Composable
fun KategoriItem(kategori: Kategori, onDeleteClick: (Kategori) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = kategori.nama, modifier = Modifier.weight(1f))
            Button(onClick = { onDeleteClick(kategori) }) {
                Text("Hapus")
            }
        }
    }
}

@Composable
fun DialogKonfirmasiHapus(
    kategori: Kategori,
    onDismiss: () -> Unit,
    onConfirm: (OpsiHapusBuku) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Hapus '${kategori.nama}'?", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Pilih opsi penghapusan untuk buku di dalamnya:")
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = { onConfirm(OpsiHapusBuku.HAPUS_BUKU) }) {
                        Text("Hapus Buku")
                    }
                    Button(onClick = { onConfirm(OpsiHapusBuku.LEPAS_KATEGORI) }) {
                        Text("Lepas Buku")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text("Batal")
                }
            }
        }
    }
}
