package com.example.travelappbir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// CommentAdapter: RecyclerView için bir adaptör. Yorumları listelemek için kullanılır.
class CommentAdapter(private val comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    // ViewHolder sınıfı: Bir liste öğesinin görünümünü ve bileşenlerini temsil eder.
    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Liste öğesindeki TextView bileşenlerini temsil eden değişkenler
        val tvName: TextView = itemView.findViewById(R.id.tvName) // Yorum yapan kişinin adı
        val tvRating: TextView = itemView.findViewById(R.id.tvRating) // Yorumun puanı
        val tvComment: TextView = itemView.findViewById(R.id.tvComment) // Yorum metni
    }

    // Yeni bir ViewHolder oluşturulur. Görünüm şişirilir ve ViewHolder döndürülür.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        // "item_comment" layout dosyasını şişirerek bir View oluşturuyoruz
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view) // Oluşturulan View ile ViewHolder döndürülür
    }

    // Belirtilen pozisyondaki liste öğesini bağlar. Yorum verileri burada ViewHolder bileşenlerine atanır.
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position] // Mevcut pozisyondaki yorum alınır

        // Yorum verilerini TextView bileşenlerine atama
        holder.tvName.text = comment.name // Yorum yapan kişinin adı atanır
        holder.tvRating.text = "Puan: ${comment.rating}/5" // Puan bilgisi atanır
        holder.tvComment.text = comment.comment // Yorum metni atanır
    }

    // RecyclerView içindeki toplam yorum sayısını döndürür
    override fun getItemCount(): Int = comments.size
}
