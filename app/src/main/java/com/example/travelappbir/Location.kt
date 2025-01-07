package com.example.travelappbir

data class Location(
    val name: String,
    val city: String,
    val district: String,
    val imageUrl: String,
    val description: String,
    val imageUrls: List<String> // Galeri için görseller
)
