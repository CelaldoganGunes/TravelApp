package com.example.travelappbir

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LocationAdapter
    private lateinit var locationList: MutableList<Location> // Orijinal veri listesi
    private lateinit var filteredList: MutableList<Location> // Filtrelenen liste

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Veri listesi
        val locationList = mutableListOf(
            Location(
                "Eiffel Tower",
                "Paris",
                "Île-de-France",
                "https://example.com/eiffel1.jpg",
                "Eiffel Kulesi hakkında detaylı bilgi.",
                listOf("https://example.com/eiffel1.jpg", "https://example.com/eiffel2.jpg", "https://example.com/eiffel3.jpg")
            ),
            Location(
                "Statue of Liberty",
                "New York",
                "New York County",
                "https://example.com/liberty1.jpg",
                "Özgürlük Anıtı hakkında detaylı bilgi.",
                listOf("https://example.com/liberty1.jpg", "https://example.com/liberty2.jpg", "https://example.com/liberty3.jpg")
            )
        )

        // Başlangıçta tüm verileri filtrelenmiş listeye kopyala
        filteredList = locationList.toMutableList()

        // RecyclerView ve adaptör kurulum
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LocationAdapter(filteredList)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        // Arama çubuğu dinleyicisi
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
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
                adapter.notifyDataSetChanged() // Listeyi güncelle
                return true
            }
        })

        return true
    }
}
