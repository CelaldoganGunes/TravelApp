package com.example.travelappbir
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LocationAdapter(
    private val locations: List<Location>
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCityDistrict: TextView = itemView.findViewById(R.id.tvCityDistrict)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.tvName.text = location.name
        holder.tvCityDistrict.text = "${location.city}, ${location.district}"

        Glide.with(holder.itemView.context)
            .load(location.imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imageView)

        // Tıklama olayı
        /*
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, LocationDetailActivity::class.java)
            intent.putExtra("name", location.name)
            intent.putExtra("city", location.city)
            intent.putExtra("district", location.district)
            intent.putExtra("description", location.description)
            intent.putStringArrayListExtra("imageUrls", ArrayList(location.imageUrls))
            intent.putExtra("latitude", location.latitude) // Enlem
            intent.putExtra("longitude", location.longitude) // Boylam
            holder.itemView.context.startActivity(intent)
        }*/
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, LocationDetailActivity::class.java)
            intent.putExtra("name", location.name) // Sadece ismi gönder
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = locations.size
}
