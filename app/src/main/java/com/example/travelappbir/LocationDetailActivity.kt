package com.example.travelappbir

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class LocationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        val tvName: TextView = findViewById(R.id.tvDetailName)
        val tvCityDistrict: TextView = findViewById(R.id.tvDetailCityDistrict)
        val tvDescription: TextView = findViewById(R.id.tvDetailDescription)

        // Intent ile gelen verileri al
        val name = intent.getStringExtra("name")
        val city = intent.getStringExtra("city")
        val district = intent.getStringExtra("district")
        val description = intent.getStringExtra("description")

        // TextView'leri güncelle
        tvName.text = name
        tvCityDistrict.text = "$city, $district"
        tvDescription.text = description
    }
}
