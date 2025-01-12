package com.example.travelappbir

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

// VisaCheckActivity: İki ülke arasında seyahat için vize gerekip gerekmediğini kontrol eden bir aktivite.
class VisaCheckActivity : AppCompatActivity() {

    // Ülke isimleri ve bayrak görsellerini içeren liste
    private val countries = listOf(
        Country("Türkiye", R.drawable.flag_turkey),
        Country("Almanya", R.drawable.flag_germany),
        Country("Fransa", R.drawable.flag_france),
        Country("İtalya", R.drawable.flag_italy),
        Country("ABD", R.drawable.flag_usa),
        Country("Rusya", R.drawable.flag_russia),
        Country("Japonya", R.drawable.flag_japan),
        Country("Hindistan", R.drawable.flag_india),
        Country("Brezilya", R.drawable.flag_brazil),
        Country("Çin", R.drawable.flag_china)
    )

    // Vize matrisi: İki ülke arasındaki vize gerekliliklerini temsil eder. (1: Vize gerekli, 0: Vize gerekli değil)
    private val visaMatrix = arrayOf(
        intArrayOf(0, 1, 1, 1, 1, 1, 1, 1, 1, 1), // Türkiye
        intArrayOf(1, 0, 0, 0, 1, 1, 1, 1, 1, 1), // Almanya
        intArrayOf(1, 0, 0, 0, 1, 1, 1, 1, 1, 1), // Fransa
        intArrayOf(1, 0, 0, 0, 1, 1, 1, 1, 1, 1), // İtalya
        intArrayOf(1, 1, 1, 1, 0, 1, 1, 1, 1, 1), // ABD
        intArrayOf(1, 1, 1, 1, 1, 0, 1, 1, 1, 1), // Rusya
        intArrayOf(1, 1, 1, 1, 1, 1, 0, 1, 1, 1), // Japonya
        intArrayOf(1, 1, 1, 1, 1, 1, 1, 0, 1, 1), // Hindistan
        intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 0, 1), // Brezilya
        intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 0)  // Çin
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visa_check)

        // Spinner bileşenlerini tanımla
        val spinnerPassportCountry: Spinner = findViewById(R.id.spinnerPassportCountry)
        val spinnerDestinationCountry: Spinner = findViewById(R.id.spinnerDestinationCountry)
        val checkVisaButton: Button = findViewById(R.id.checkVisaButton)
        val tvVisaResult: TextView = findViewById(R.id.tvVisaResult)

        // Geri butonunu etkinleştir ve başlığı ayarla
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Vize Kontrol"

        // Spinner adaptörünü oluştur ve bağla
        val countryAdapter = CountryAdapter(this, countries)
        spinnerPassportCountry.adapter = countryAdapter
        spinnerDestinationCountry.adapter = countryAdapter

        // Vize kontrol butonuna tıklama olayı tanımla
        checkVisaButton.setOnClickListener {
            val passportCountryIndex = spinnerPassportCountry.selectedItemPosition // Seçilen pasaport ülkesinin indeksi
            val destinationCountryIndex = spinnerDestinationCountry.selectedItemPosition // Seçilen hedef ülkenin indeksi

            // Eğer seçilen ülkeler aynıysa vize gerekmez
            if (passportCountryIndex == destinationCountryIndex) {
                tvVisaResult.text = "Aynı ülkeye seyahat için vize gerekmez."
                return@setOnClickListener
            }

            // Vize gerekip gerekmediğini matrise göre kontrol et
            val requiresVisa = visaMatrix[passportCountryIndex][destinationCountryIndex] == 1
            tvVisaResult.text = if (requiresVisa) {
                "Bu seyahat için vize gereklidir."
            } else {
                "Bu seyahat için vize gerekmiyor."
            }
        }
    }

    // Menü öğesi seçildiğinde çalışır (geri butonu için)
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // Geri butonuna tıklanınca
                onBackPressed() // Aktiviteyi kapat ve önceki sayfaya dön
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
