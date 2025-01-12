package com.example.travelappbir

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    // RecyclerView bileşeni, lokasyon listesini göstermek için kullanılacak
    private lateinit var recyclerView: RecyclerView

    // Lokasyonları göstermek için kullanılacak adaptör
    private lateinit var adapter: LocationAdapter

    // JSON dosyasından yüklenen tüm lokasyonlar
    private var locationList: MutableList<Location> = mutableListOf()

    // Filtrelenen lokasyonları tutan liste (arama için)
    private var filteredList: MutableList<Location> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView bileşenini başlatıyoruz
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this) // Listeleme düzeni dikey olarak ayarlanıyor

        // JSON dosyasından lokasyon verilerini yükleme
        loadLocationsFromJson()

        // Tüm lokasyonları filtrelenmiş listeye ekleme (başlangıç durumu)
        filteredList.addAll(locationList)

        // RecyclerView için adaptörü bağlama
        adapter = LocationAdapter(filteredList)
        recyclerView.adapter = adapter
    }

    // JSON dosyasından lokasyon verilerini okur ve locationList'e ekler
    private fun loadLocationsFromJson() {
        try {
            // "locations.json" adlı dosyayı açıyoruz
            val inputStream = assets.open("locations.json")
            val reader = InputStreamReader(inputStream)

            // JSON verilerini Location türünde bir listeye dönüştürüyoruz
            val type = object : TypeToken<List<Location>>() {}.type
            locationList = Gson().fromJson(reader, type)

            // Reader'ı kapatıyoruz
            reader.close()
        } catch (e: Exception) {
            // Hata durumunda loglama veya hata yönetimi
            e.printStackTrace()
        }
    }

    // Bir lokasyon detayını açan yardımcı yöntem
    private fun openLocationDetail(location: Location) {
        val intent = Intent(this, LocationDetailActivity::class.java)

        // Lokasyon verilerini intent'e ekliyoruz
        intent.putExtra("name", location.name)
        intent.putExtra("city", location.city)
        intent.putExtra("district", location.district)
        intent.putExtra("description", location.description)
        intent.putExtra("latitude", location.latitude)
        intent.putExtra("longitude", location.longitude)
        intent.putStringArrayListExtra("imageUrls", ArrayList(location.imageUrls))

        // Detay sayfasını başlatıyoruz
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // Menü düzenini şişiriyoruz

        // Arama işlevi için SearchView bileşeni
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        // Arama çubuğuna dinleyici ekliyoruz
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // Arama çubuğu gönderme işlemi kullanılmıyor
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Arama metni her değiştiğinde çağrılır
                filteredList.clear() // Filtrelenmiş listeyi temizle

                if (!newText.isNullOrEmpty()) {
                    val searchText = newText.lowercase() // Metni küçük harfe çevir

                    // Lokasyonları ada veya şehre göre filtrele
                    locationList.forEach {
                        if (it.name.lowercase().contains(searchText) || it.city.lowercase().contains(searchText)) {
                            filteredList.add(it)
                        }
                    }
                } else {
                    // Arama çubuğu boşsa tüm lokasyonları ekle
                    filteredList.addAll(locationList)
                }

                adapter.notifyDataSetChanged() // RecyclerView'i güncelle
                return true
            }
        })

        return true // Menü başarılı şekilde oluşturuldu
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorites -> {
                // Favoriler sayfasını aç
                val intent = Intent(this, FavoritesActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_expense_tracker -> {
                // Harcama Takip sayfasını aç
                val intent = Intent(this, ExpenseTrackerActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_visa_check -> {
                // Vize Kontrol sayfasını aç
                val intent = Intent(this, VisaCheckActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item) // Diğer öğeler için varsayılan işlem
        }
    }
}
