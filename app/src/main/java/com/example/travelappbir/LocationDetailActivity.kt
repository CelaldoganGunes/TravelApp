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

// LocationDetailActivity: Bir lokasyonun detaylarını gösteren ve yönetim sağlayan bir aktivite.
class LocationDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    // Lokasyon adı ve detaylarını tutan değişkenler
    private lateinit var locationName: String // Lokasyon adı
    private lateinit var location: Location // Lokasyon detayları
    private var isFavorite: Boolean = false // Favori durumu

    // Yorum adaptörü ve yorum listesi
    private lateinit var commentAdapter: CommentAdapter
    private var comments: MutableList<Comment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        // Geri butonunu etkinleştir
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Intent ile gelen lokasyon adını al
        locationName = intent.getStringExtra("name") ?: "Unknown Location"
        supportActionBar?.title = locationName

        // Lokasyon detaylarını JSON'dan yükle
        location = loadLocationFromJson(locationName) ?: run {
            Toast.makeText(this, "Lokasyon bilgileri bulunamadı!", Toast.LENGTH_SHORT).show()
            finish() // Eğer lokasyon bulunamazsa aktiviteyi kapat
            return
        }

        // Lokasyonun favori olup olmadığını kontrol et
        isFavorite = isLocationFavorite(location)

        // Lokasyon bilgilerini UI'ye yükle
        bindLocationToUI()

        // Yorumları yükle ve listele
        setupComments()

        // Harita bileşenini yükle
        setupMap()
    }

    // Lokasyon bilgilerini kullanıcı arayüzüne bağlar
    private fun bindLocationToUI() {
        val tvTitle: TextView = findViewById(R.id.tvDetailTitle) // Başlık metni
        val tvCityDistrict: TextView = findViewById(R.id.tvDetailCityDistrict) // Şehir ve ilçe bilgisi
        val tvDescription: TextView = findViewById(R.id.tvDetailDescription) // Açıklama
        val galleryViewPager: ViewPager2 = findViewById(R.id.galleryViewPager) // Fotoğraf galerisi
        val fabDirections: FloatingActionButton = findViewById(R.id.fabDirections) // Yol tarifi butonu

        // Verileri UI'ye atama
        tvTitle.text = location.name
        tvCityDistrict.text = "${location.city}, ${location.district}"
        tvDescription.text = location.description

        // Fotoğraf galerisi adaptörünü bağla
        val galleryAdapter = GalleryAdapter(location.imageUrls)
        galleryViewPager.adapter = galleryAdapter

        // Yol tarifi butonuna tıklama olayı tanımla
        fabDirections.setOnClickListener {
            openGoogleMapsForDirections(location.latitude, location.longitude)
        }
    }

    // Yorumları yükler ve listeye bağlar
    private fun setupComments() {
        val recyclerViewComments: RecyclerView = findViewById(R.id.recyclerViewComments)
        recyclerViewComments.layoutManager = LinearLayoutManager(this)
        comments = PreferenceHelper.getComments(this, locationName).toMutableList()
        commentAdapter = CommentAdapter(comments)
        recyclerViewComments.adapter = commentAdapter

        // Yorum ekleme için form bileşenleri
        val etName: EditText = findViewById(R.id.etName)
        val etRating: EditText = findViewById(R.id.etRating)
        val etComment: EditText = findViewById(R.id.etComment)
        val btnSubmitComment: Button = findViewById(R.id.btnSubmitComment)

        // Yorum ekleme işlemi
        btnSubmitComment.setOnClickListener {
            val name = etName.text.toString()
            val rating = etRating.text.toString().toIntOrNull()
            val commentText = etComment.text.toString()

            if (name.isNotEmpty() && rating != null && rating in 1..5 && commentText.isNotEmpty()) {
                val newComment = Comment(name, rating, commentText)
                comments.add(newComment) // Yorum listesine ekle
                PreferenceHelper.saveComments(this, locationName, comments) // Yorumları kaydet
                commentAdapter.notifyDataSetChanged() // RecyclerView'i güncelle
                Toast.makeText(this, "Yorum kaydedildi!", Toast.LENGTH_SHORT).show()

                // Formu temizle
                etName.text.clear()
                etRating.text.clear()
                etComment.text.clear()
            } else {
                Toast.makeText(this, "Lütfen tüm alanları doğru doldurun!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Harita bileşenini başlatır
    private fun setupMap() {
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mapFragment, mapFragment)
            .commit()

        mapFragment.getMapAsync(this) // Harita hazır olduğunda bu sınıfa callback gönderir
    }

    // Google Maps'te rota açmak için bir URI oluşturur ve başlatır
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

    // JSON dosyasından lokasyon bilgilerini yükler
    private fun loadLocationFromJson(name: String): Location? {
        return try {
            val inputStream = assets.open("locations.json") // Dosyayı aç
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<Location>>() {}.type // JSON'u çözmek için tür
            val locations: List<Location> = Gson().fromJson(reader, type)
            reader.close()
            locations.find { it.name == name } // İsme göre lokasyonu bul
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Harita hazır olduğunda çağrılır
    override fun onMapReady(googleMap: GoogleMap) {
        val locationLatLng = LatLng(location.latitude, location.longitude)
        googleMap.addMarker(MarkerOptions().position(locationLatLng).title(location.name))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15f))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_location_detail, menu)
        updateFavoriteIcon(menu?.findItem(R.id.action_favorite)) // Favori ikonu güncelle
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Geri butonuna basıldığında aktiviteyi kapat
                true
            }
            R.id.action_favorite -> {
                toggleFavoriteStatus(item) // Favori durumunu değiştir
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Favori durumunu değiştirir
    private fun toggleFavoriteStatus(item: MenuItem) {
        val favorites = PreferenceHelper.getFavorites(this)
        if (isFavorite) {
            favorites.removeIf { it.name == location.name }
            Toast.makeText(this, "Favorilerden kaldırıldı", Toast.LENGTH_SHORT).show()
        } else {
            favorites.add(location)
            Toast.makeText(this, "Favorilere eklendi", Toast.LENGTH_SHORT).show()
        }
        PreferenceHelper.saveFavorites(this, favorites) // Favori listesini kaydet
        isFavorite = !isFavorite
        updateFavoriteIcon(item) // İkonu güncelle
    }

    // Favori ikonunu günceller
    private fun updateFavoriteIcon(item: MenuItem?) {
        item?.icon = if (isFavorite) {
            getDrawable(R.drawable.ic_star_filled)
        } else {
            getDrawable(R.drawable.ic_star_border)
        }
    }

    // Lokasyonun favori olup olmadığını kontrol eder
    private fun isLocationFavorite(location: Location): Boolean {
        val favorites = PreferenceHelper.getFavorites(this)
        return favorites.any { it.name == location.name }
    }
}
