package com.example.travelappbir

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide

class LocationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        // Geri butonu etkinleştir
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Intent ile gelen verileri al
        val name = intent.getStringExtra("name")
        val city = intent.getStringExtra("city")
        val district = intent.getStringExtra("district")
        val description = intent.getStringExtra("description")
        val imageUrls = intent.getStringArrayListExtra("imageUrls") ?: arrayListOf()

        // TextView'leri bul ve verilerle güncelle
        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvCityDistrict: TextView = findViewById(R.id.tvDetailCityDistrict)
        val tvDescription: TextView = findViewById(R.id.tvDetailDescription)
        val galleryViewPager: ViewPager2 = findViewById(R.id.galleryViewPager)

        tvTitle.text = name
        tvCityDistrict.text = "$city, $district"
        tvDescription.text = description

        // Fotoğraf galerisi adaptörünü bağla
        val galleryAdapter = GalleryAdapter(imageUrls)
        galleryViewPager.adapter = galleryAdapter
    }

    // Geri butonuna basıldığında MainActivity'ye dön
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
