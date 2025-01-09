package com.example.travelappbir

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView

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

        // Test verilerini doldur
        locationList = mutableListOf(
            Location(
                "Eiffel Tower",
                "Paris",
                "Île-de-France",
                "https://example.com/eiffel.jpg",
                "Eiffel Kulesi hakkında detaylı bilgi.",
                listOf(
                    "https://example.com/eiffel1.jpg",
                    "https://example.com/eiffel2.jpg",
                    "https://example.com/eiffel3.jpg"
                ),
                48.8588443,
                2.2943506
            ),
            Location(
                "Statue of Liberty",
                "New York",
                "New York County",
                "https://example.com/liberty.jpg",
                "Özgürlük Anıtı hakkında detaylı bilgi.",
                listOf(
                    "https://example.com/liberty1.jpg",
                    "https://example.com/liberty2.jpg",
                    "https://example.com/liberty3.jpg"
                ),
                40.689247,
                -74.044502
            ),
            Location(
                "Great Wall",
                "Beijing",
                "China",
                "https://example.com/greatwall.jpg",
                "Çin Seddi hakkında detaylı bilgi.",
                listOf(
                    "https://example.com/wall1.jpg",
                    "https://example.com/wall2.jpg",
                    "https://example.com/wall3.jpg"
                ),
                40.431908,
                116.570374
            )
        )

        // Filtrelenmiş listeyi doldur
        filteredList.addAll(locationList)

        // Adapteri bağla
        adapter = LocationAdapter(filteredList)
        recyclerView.adapter = adapter
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
}
