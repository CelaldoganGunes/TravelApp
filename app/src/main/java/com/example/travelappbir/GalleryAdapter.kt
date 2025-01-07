package com.example.travelappbir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.galleryImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_image, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = imageUrls.size
}
