package com.example.travelappbir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// GalleryAdapter: RecyclerView için bir adaptör. Fotoğraf galerisi görüntülerini listelemek için kullanılır.
class GalleryAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    // ViewHolder sınıfı: Her bir liste öğesinin görünümünü ve bileşenlerini temsil eder.
    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.galleryImageView) // Görüntüleri göstermek için ImageView bileşeni.
    }

    // Yeni bir ViewHolder oluşturulur. Görünüm şişirilir ve ViewHolder döndürülür.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery_image, parent, false) // "item_gallery_image" layout dosyası kullanılır.
        return GalleryViewHolder(view)
    }

    // Belirtilen pozisyondaki liste öğesini bağlar. Görsel yükleme işlemi burada yapılır.
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val imageUrl = imageUrls[position] // Mevcut pozisyondaki görsel URL'si alınır.

        // Glide kütüphanesi kullanılarak görsel yüklenir ve ImageView'e atanır.
        Glide.with(holder.itemView.context)
            .load(imageUrl) // Yüklenmesi gereken görselin URL'si.
            .placeholder(R.drawable.ic_placeholder) // Görsel yüklenirken gösterilecek yer tutucu (placeholder).
            .into(holder.imageView) // Yüklenen görsel, ImageView'e aktarılır.
    }

    // Liste öğelerinin toplam sayısını döndürür.
    override fun getItemCount(): Int = imageUrls.size
}
