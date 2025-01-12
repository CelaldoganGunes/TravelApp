package com.example.travelappbir

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class LocationDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var locationName: String
    private lateinit var location: Location
    private var isFavorite: Boolean = false
    private lateinit var commentAdapter: CommentAdapter
    private var comments: MutableList<Comment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        // Geri butonunu etkinleştir
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Intent ile gelen "name" değerini al
        locationName = intent.getStringExtra("name") ?: "Unknown Location"

        // JSON dosyasından lokasyon bilgilerini yükle
        location = loadLocationFromJson(locationName) ?: run {
            Toast.makeText(this, "Lokasyon bilgileri bulunamadı!", Toast.LENGTH_SHORT).show()
            finish() // Lokasyon bulunamazsa sayfayı kapat
            return
        }

        // Favori durumu kontrolü
        isFavorite = isLocationFavorite(location)

        // Lokasyon bilgilerini UI'ye yükle
        bindLocationToUI()

        // Yorumları yükle
        setupComments()

        // Haritayı yükle
        setupMap()
    }

    private fun bindLocationToUI() {
        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvCityDistrict: TextView = findViewById(R.id.tvDetailCityDistrict)
        val tvDescription: TextView = findViewById(R.id.tvDetailDescription)
        val galleryViewPager: ViewPager2 = findViewById(R.id.galleryViewPager)
        val fabDirections: FloatingActionButton = findViewById(R.id.fabDirections)

        tvTitle.text = location.name
        tvCityDistrict.text = "${location.city}, ${location.district}"
        tvDescription.text = location.description

        // Fotoğraf galerisi için adaptörü bağla
        val galleryAdapter = GalleryAdapter(location.imageUrls)
        galleryViewPager.adapter = galleryAdapter

        // Yol tarifi butonu
        fabDirections.setOnClickListener {
            openGoogleMapsForDirections(location.latitude, location.longitude)
        }
    }

    private fun setupComments() {
        val recyclerViewComments: RecyclerView = findViewById(R.id.recyclerViewComments)
        recyclerViewComments.layoutManager = LinearLayoutManager(this)
        comments = PreferenceHelper.getComments(this, locationName).toMutableList()
        commentAdapter = CommentAdapter(comments)
        recyclerViewComments.adapter = commentAdapter

        val etName: EditText = findViewById(R.id.etName)
        val etRating: EditText = findViewById(R.id.etRating)
        val etComment: EditText = findViewById(R.id.etComment)
        val btnSubmitComment: Button = findViewById(R.id.btnSubmitComment)

        btnSubmitComment.setOnClickListener {
            val name = etName.text.toString()
            val rating = etRating.text.toString().toIntOrNull()
            val commentText = etComment.text.toString()

            if (name.isNotEmpty() && rating != null && rating in 1..5 && commentText.isNotEmpty()) {
                val newComment = Comment(name, rating, commentText)
                comments.add(newComment)
                PreferenceHelper.saveComments(this, locationName, comments)
                commentAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Yorum kaydedildi!", Toast.LENGTH_SHORT).show()
                etName.text.clear()
                etRating.text.clear()
                etComment.text.clear()
            } else {
                Toast.makeText(this, "Lütfen tüm alanları doğru doldurun!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupMap() {
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mapFragment, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    private fun openGoogleMapsForDirections(latitude: Double, longitude: Double) {
        val uri = Uri.parse("google.navigation:q=$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Google Maps uygulaması yüklü değil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadLocationFromJson(name: String): Location? {
        return try {
            val inputStream = assets.open("locations.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<Location>>() {}.type
            val locations: List<Location> = Gson().fromJson(reader, type)
            reader.close()
            locations.find { it.name == name } // Lokasyonu isme göre bul
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val locationLatLng = LatLng(location.latitude, location.longitude)
        googleMap.addMarker(MarkerOptions().position(locationLatLng).title(location.name))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15f))
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
            favorites.removeIf { it.name == location.name }
            Toast.makeText(this, "Favorilerden kaldırıldı", Toast.LENGTH_SHORT).show()
        } else {
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
