package com.example.travelappbir

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var locationName: String
    private lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        // Geri butonunu etkinleştir
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Intent ile gelen verileri al
        locationName = intent.getStringExtra("name") ?: "Unknown Location"
        val city = intent.getStringExtra("city") ?: "Unknown City"
        val district = intent.getStringExtra("district") ?: "Unknown District"
        val description = intent.getStringExtra("description") ?: "No Description Available"
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        val imageUrls = intent.getStringArrayListExtra("imageUrls") ?: arrayListOf()

        // Location nesnesini oluştur
        location = Location(locationName, city, district, "", description, imageUrls, latitude, longitude)

        // UI bileşenlerini bul ve güncelle
        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvCityDistrict: TextView = findViewById(R.id.tvDetailCityDistrict)
        val tvDescription: TextView = findViewById(R.id.tvDetailDescription)
        val galleryViewPager: ViewPager2 = findViewById(R.id.galleryViewPager)
        val btnFavorite: Button = findViewById(R.id.btnFavorite)

        tvTitle.text = locationName
        tvCityDistrict.text = "$city, $district"
        tvDescription.text = description

        // Fotoğraf galerisi için adaptörü bağla
        val galleryAdapter = GalleryAdapter(imageUrls)
        galleryViewPager.adapter = galleryAdapter

        // Favori durumu kontrolü
        updateFavoriteButton(btnFavorite)

        // Favori butonu dinleyicisi
        btnFavorite.setOnClickListener {
            toggleFavorite(location, btnFavorite)
        }

        // Google Maps Fragment'i başlat
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mapFragment, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Haritada konumu işaretle ve kamerayı yakınlaştır
        val location = LatLng(latitude, longitude)
        googleMap.addMarker(MarkerOptions().position(location).title(locationName))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
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

    private fun toggleFavorite(location: Location, button: Button) {
        val favorites = PreferenceHelper.getFavorites(this)
        if (favorites.any { it.name == location.name }) {
            // Favorilerden kaldır
            favorites.removeIf { it.name == location.name }
            PreferenceHelper.saveFavorites(this, favorites)
            Toast.makeText(this, "Favorilerden kaldırıldı", Toast.LENGTH_SHORT).show()
        } else {
            // Favorilere ekle
            favorites.add(location)
            PreferenceHelper.saveFavorites(this, favorites)
            Toast.makeText(this, "Favorilere eklendi", Toast.LENGTH_SHORT).show()
        }
        updateFavoriteButton(button)
    }

    private fun updateFavoriteButton(button: Button) {
        val favorites = PreferenceHelper.getFavorites(this)
        if (favorites.any { it.name == location.name }) {
            button.text = "Favorilerden Kaldır"
        } else {
            button.text = "Favorilere Ekle"
        }
    }
}
