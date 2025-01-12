package com.example.travelappbir

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import android.widget.ArrayAdapter

// Country veri sınıfı: Ülke adı ve bayrak görsel kaynağı referansı içerir
data class Country(val name: String, @DrawableRes val flagResId: Int)

// CountryAdapter: Spinner ve diğer listelerde ülke bilgilerini göstermek için ArrayAdapter sınıfını genişletir.
class CountryAdapter(context: Context, private val countries: List<Country>) :
    ArrayAdapter<Country>(context, 0, countries) {

    // Spinner içindeki ana görünümü oluşturur
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    // Spinner açıldığında, her öğe için açılır liste görünümünü oluşturur
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    // Her bir öğe için görünümü oluşturur ve verileri bağlar
    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val country = getItem(position) // Mevcut pozisyondaki ülke verisini alır

        // Eğer mevcut bir görünüm yoksa yeni bir görünüm şişirilir
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_country, parent, false) // "item_country" layout dosyası şişirilir

        // Görünüm bileşenlerini bul
        val flagImageView = view.findViewById<ImageView>(R.id.imageViewFlag) // Ülke bayrağı için ImageView
        val nameTextView = view.findViewById<TextView>(R.id.textViewCountryName) // Ülke adı için TextView

        // Ülke bayrağını ve adını bileşenlere bağla
        flagImageView.setImageResource(country?.flagResId ?: 0) // Bayrak görseli atanır
        nameTextView.text = country?.name ?: "" // Ülke adı atanır

        return view // Hazırlanan görünüm döndürülür
    }
}
