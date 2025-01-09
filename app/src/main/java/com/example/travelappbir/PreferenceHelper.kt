package com.example.travelappbir

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferenceHelper {

    private const val PREFS_NAME = "favorites_prefs"
    private const val FAVORITES_KEY = "favorites"

    fun saveFavorites(context: Context, favorites: List<Location>) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(favorites)
        editor.putString(FAVORITES_KEY, json)
        editor.apply()
    }

    fun getFavorites(context: Context): MutableList<Location> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(FAVORITES_KEY, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Location>>() {}.type
        return Gson().fromJson(json, type)
    }
}
