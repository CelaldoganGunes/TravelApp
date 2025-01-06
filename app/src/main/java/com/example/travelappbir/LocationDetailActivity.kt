package com.example.travelappbir

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LocationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        // ActionBar'da geri butonunu etkinleştir
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Intent ile gelen verileri al
        val name = intent.getStringExtra("name")
        val city = intent.getStringExtra("city")
        val district = intent.getStringExtra("district")
        val description = intent.getStringExtra("description")

        // TextView'leri bul ve güncelle
        val tvName: TextView = findViewById(R.id.tvDetailName)
        val tvCityDistrict: TextView = findViewById(R.id.tvDetailCityDistrict)
        val tvDescription: TextView = findViewById(R.id.tvDetailDescription)

        tvName.text = name
        tvCityDistrict.text = "$city, $district"
        tvDescription.text = description
    }

    // Geri butonuna basıldığında MainActivity'ye dön
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Geri tuşu davranışı
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
