package com.example.travelappbir

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
        val imageUrl = intent.getStringExtra("imageUrl")

        // TextView ve ImageView'leri bul ve verilerle güncelle
        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvCityDistrict: TextView = findViewById(R.id.tvDetailCityDistrict)
        val tvDescription: TextView = findViewById(R.id.tvDetailDescription)
        val imgDetailImage: ImageView = findViewById(R.id.imgDetailImage)

        tvTitle.text = name
        tvCityDistrict.text = "$city, $district"
        tvDescription.text = description

        // Görseli yükle
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(imgDetailImage)
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
