package com.example.travelappbir

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// LocationAdapter, RecyclerView için adaptör olarak kullanılır ve lokasyon verilerini gösterir.
class LocationAdapter(
    private val locations: List<Location> // Lokasyonları içeren veri listesi
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    // ViewHolder, bir liste öğesinin görünümünü ve bileşenlerini temsil eder.
    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Her bir liste öğesinde yer alan bileşenler
        val imageView: ImageView = itemView.findViewById(R.id.imageView) // Lokasyonun görseli
        val tvName: TextView = itemView.findViewById(R.id.tvName) // Lokasyon adı
        val tvCityDistrict: TextView = itemView.findViewById(R.id.tvCityDistrict) // Şehir ve ilçe bilgisi
    }

    // RecyclerView öğesi oluşturulduğunda çağrılır. Görünüm şişirilir ve bir ViewHolder döndürülür.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false) // XML tasarım dosyası "item_location" şişirilir
        return LocationViewHolder(view)
    }

    // RecyclerView'deki her öğe bağlandığında çağrılır.
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position] // Mevcut pozisyondaki lokasyonu al

        // Lokasyon verilerini ilgili ViewHolder bileşenlerine bağla
        holder.tvName.text = location.name // Lokasyon adı atanır
        holder.tvCityDistrict.text = "${location.city}, ${location.district}" // Şehir ve ilçe bilgisi atanır

        // Glide kütüphanesi kullanılarak lokasyon görseli yüklenir
        Glide.with(holder.itemView.context)
            .load(location.imageUrl) // Görsel URL'si
            .placeholder(R.drawable.ic_placeholder) // Yükleme sırasında gösterilecek yer tutucu görsel
            .into(holder.imageView) // Görsel, ImageView bileşenine yüklenir

        // Tıklama olayı tanımlanır
        holder.itemView.setOnClickListener {
            // Lokasyon detay sayfasını açmak için Intent oluşturulur
            val intent = Intent(holder.itemView.context, LocationDetailActivity::class.java)

            // Sadece lokasyonun adı Intent'e eklenir
            intent.putExtra("name", location.name)

            // Detay sayfası başlatılır
            holder.itemView.context.startActivity(intent)
        }
    }

    // RecyclerView içindeki toplam öğe sayısını döndürür
    override fun getItemCount(): Int = locations.size
}
