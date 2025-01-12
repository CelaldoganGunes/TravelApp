package com.example.travelappbir

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// FavoritesActivity: Favori lokasyonları listeleyen bir aktivite.
class FavoritesActivity : AppCompatActivity() {

    // RecyclerView bileşeni ve adaptör tanımları
    private lateinit var recyclerView: RecyclerView // Favori lokasyonları göstermek için RecyclerView
    private lateinit var adapter: LocationAdapter // Lokasyonları bağlamak için adaptör

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Geri butonunu etkinleştir
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Geri gitme butonunu etkinleştir
        supportActionBar?.title = "Favoriler" // Başlık olarak "Favoriler" belirle

        // RecyclerView'u başlatıyoruz
        recyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = LinearLayoutManager(this) // RecyclerView düzenini dikey olarak ayarla

        // İlk favoriler listesini yükle
        updateFavoritesList()
    }

    override fun onResume() {
        super.onResume()
        // Aktivite geri geldiğinde favori listesini günceller
        updateFavoritesList()
    }

    // Favori lokasyon listesini günceller ve RecyclerView'e bağlar
    private fun updateFavoritesList() {
        val favoriteLocations = PreferenceHelper.getFavorites(this) // Kaydedilmiş favorileri al
        adapter = LocationAdapter(favoriteLocations) // Yeni adaptör oluştur
        recyclerView.adapter = adapter // RecyclerView'e adaptörü bağla
    }

    // Lokasyon detay sayfasını açar
    private fun openLocationDetail(location: Location) {
        val intent = Intent(this, LocationDetailActivity::class.java)

        // Lokasyon bilgilerini intent ile aktar
        intent.putExtra("name", location.name)
        intent.putExtra("city", location.city)
        intent.putExtra("district", location.district)
        intent.putExtra("description", location.description)
        intent.putExtra("latitude", location.latitude)
        intent.putExtra("longitude", location.longitude)
        intent.putStringArrayListExtra("imageUrls", ArrayList(location.imageUrls))

        // Lokasyon detay sayfasını başlat
        startActivity(intent)
    }

    // Menü öğesi seçildiğinde çalışır
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // Geri butonuna basıldığında
                onBackPressed() // Önceki sayfaya geri dön
                true
            }
            else -> super.onOptionsItemSelected(item) // Diğer öğeler için varsayılan işlem
        }
    }
}
