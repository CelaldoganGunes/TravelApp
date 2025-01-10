package com.example.travelappbir

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Geri butonunu etkinleştir
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favoriler"

        // Favoriler listesini al
        val favoriteLocations = PreferenceHelper.getFavorites(this)

        // RecyclerView'u ayarla
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LocationAdapter(favoriteLocations)
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

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
