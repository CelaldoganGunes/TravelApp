package com.example.travelappbir

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import android.widget.ArrayAdapter

data class Country(val name: String, @DrawableRes val flagResId: Int)

class CountryAdapter(context: Context, private val countries: List<Country>) :
    ArrayAdapter<Country>(context, 0, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val country = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_country, parent, false)
        val flagImageView = view.findViewById<ImageView>(R.id.imageViewFlag)
        val nameTextView = view.findViewById<TextView>(R.id.textViewCountryName)

        flagImageView.setImageResource(country?.flagResId ?: 0)
        nameTextView.text = country?.name ?: ""
        return view
    }
}
