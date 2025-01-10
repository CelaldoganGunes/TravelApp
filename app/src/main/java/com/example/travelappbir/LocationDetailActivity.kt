package com.example.travelappbir

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LocationDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var locationName: String
    private lateinit var location: Location
    private var isFavorite: Boolean = false

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

        // Favori durumu kontrolü
        isFavorite = isLocationFavorite(location)

        // UI bileşenlerini bul ve güncelle
        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvCityDistrict: TextView = findViewById(R.id.tvDetailCityDistrict)
        val tvDescription: TextView = findViewById(R.id.tvDetailDescription)
        val galleryViewPager: ViewPager2 = findViewById(R.id.galleryViewPager)
        val fabDirections: FloatingActionButton = findViewById(R.id.fabDirections)

        tvTitle.text = locationName
        tvCityDistrict.text = "$city, $district"
        tvDescription.text = description

        // Fotoğraf galerisi için adaptörü bağla
        val galleryAdapter = GalleryAdapter(imageUrls)
        galleryViewPager.adapter = galleryAdapter

        // Floating Action Button'a tıklama işlemi
        fabDirections.setOnClickListener {
            openGoogleMapsForDirections(latitude, longitude)
        }

        // Google Maps Fragment'i başlat
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mapFragment, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    private fun openGoogleMapsForDirections(latitude: Double, longitude: Double) {
        // Google Maps yol tarifi URI'si
        val uri = Uri.parse("google.navigation:q=$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Google Maps uygulaması yüklü değilse
            Toast.makeText(this, "Google Maps uygulaması yüklü değil", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Haritada konumu işaretle ve kamerayı yakınlaştır
        val location = LatLng(latitude, longitude)
        googleMap.addMarker(MarkerOptions().position(location).title(locationName))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_location_detail, menu)
        updateFavoriteIcon(menu?.findItem(R.id.action_favorite))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_favorite -> {
                toggleFavoriteStatus(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleFavoriteStatus(item: MenuItem) {
        val favorites = PreferenceHelper.getFavorites(this)
        if (isFavorite) {
            // Favorilerden kaldır
            favorites.removeIf { it.name == location.name }
            Toast.makeText(this, "Favorilerden kaldırıldı", Toast.LENGTH_SHORT).show()
        } else {
            // Favorilere ekle
            favorites.add(location)
            Toast.makeText(this, "Favorilere eklendi", Toast.LENGTH_SHORT).show()
        }
        PreferenceHelper.saveFavorites(this, favorites)
        isFavorite = !isFavorite
        updateFavoriteIcon(item)
    }

    private fun updateFavoriteIcon(item: MenuItem?) {
        item?.icon = if (isFavorite) {
            getDrawable(R.drawable.ic_star_filled)
        } else {
            getDrawable(R.drawable.ic_star_border)
        }
    }

    private fun isLocationFavorite(location: Location): Boolean {
        val favorites = PreferenceHelper.getFavorites(this)
        return favorites.any { it.name == location.name }
    }
}
