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

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LocationAdapter
    private var locationList: MutableList<Location> = mutableListOf()
    private var filteredList: MutableList<Location> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView'i başlat
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // JSON'dan verileri yükle
        loadLocationsFromJson()

        // Filtrelenmiş listeyi doldur
        filteredList.addAll(locationList)

        // Adapteri bağla
        adapter = LocationAdapter(filteredList)
        recyclerView.adapter = adapter
    }

    private fun loadLocationsFromJson() {
        try {
            val inputStream = assets.open("locations.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<Location>>() {}.type
            locationList = Gson().fromJson(reader, type)
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openLocationDetail(location: Location) {
        val intent = Intent(this, LocationDetailActivity::class.java)
        intent.putExtra("name", location.name)
        intent.putExtra("city", location.city)
        intent.putExtra("district", location.district)
        intent.putExtra("description", location.description)
        intent.putExtra("latitude", location.latitude)
        intent.putExtra("longitude", location.longitude)
        intent.putStringArrayListExtra("imageUrls", ArrayList(location.imageUrls))
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        // Arama dinleyicisini ayarla
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filtreleme işlemi
                filteredList.clear()
                if (!newText.isNullOrEmpty()) {
                    val searchText = newText.lowercase()
                    locationList.forEach {
                        if (it.name.lowercase().contains(searchText) || it.city.lowercase().contains(searchText)) {
                            filteredList.add(it)
                        }
                    }
                } else {
                    filteredList.addAll(locationList)
                }
                adapter.notifyDataSetChanged() // RecyclerView'i güncelle
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorites -> {
                // Favoriler sayfasına geçiş
                val intent = Intent(this, FavoritesActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_expense_tracker -> {
                // Harcama Takip sayfasına geçiş
                val intent = Intent(this, ExpenseTrackerActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_visa_check -> {
                // Vize Kontrol sayfasına geçiş
                val intent = Intent(this, VisaCheckActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
