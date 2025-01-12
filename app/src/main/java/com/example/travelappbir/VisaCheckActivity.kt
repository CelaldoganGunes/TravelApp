package com.example.travelappbir

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class VisaCheckActivity : AppCompatActivity() {

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

    // Vize matrisi (1: Vize gerekli, 0: Vize gerekli değil)
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

        val spinnerPassportCountry: Spinner = findViewById(R.id.spinnerPassportCountry)
        val spinnerDestinationCountry: Spinner = findViewById(R.id.spinnerDestinationCountry)
        val checkVisaButton: Button = findViewById(R.id.checkVisaButton)
        val tvVisaResult: TextView = findViewById(R.id.tvVisaResult)

        // Geri butonunu etkinleştir
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Vize Kontrol"

        // Spinner için adapter
        val countryAdapter = CountryAdapter(this, countries)
        spinnerPassportCountry.adapter = countryAdapter
        spinnerDestinationCountry.adapter = countryAdapter

        // Vize kontrol butonu
        checkVisaButton.setOnClickListener {
            val passportCountryIndex = spinnerPassportCountry.selectedItemPosition
            val destinationCountryIndex = spinnerDestinationCountry.selectedItemPosition

            if (passportCountryIndex == destinationCountryIndex) {
                tvVisaResult.text = "Aynı ülkeye seyahat için vize gerekmez."
                return@setOnClickListener
            }

            val requiresVisa = visaMatrix[passportCountryIndex][destinationCountryIndex] == 1
            tvVisaResult.text = if (requiresVisa) {
                "Bu seyahat için vize gereklidir."
            } else {
                "Bu seyahat için vize gerekmiyor."
            }
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // Geri butonuna tıklanınca
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
