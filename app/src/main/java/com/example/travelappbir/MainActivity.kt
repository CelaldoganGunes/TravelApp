package com.example.travelappbir

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val locations = listOf(
            Location(
                "Eiffel Tower", "Paris", "Île-de-France",
                "https://example.com/eiffel.jpg",
                "Eiffel Kulesi, 1889 Dünya Fuarı için inşa edilmiş ikonik bir yapıdır."
            ),
            Location(
                "Statue of Liberty", "New York", "New York County",
                "https://example.com/liberty.jpg",
                "Özgürlük Anıtı, ABD'yi temsil eden dünyaca ünlü bir simgedir."
            ),
            Location(
                "Great Wall", "Beijing", "China",
                "https://example.com/wall.jpg",
                "Çin Seddi, Çin'in kuzey sınırlarını korumak için inşa edilen tarihi bir yapıdır."
            )
        )


        recyclerView.adapter = LocationAdapter(locations)
    }
}
