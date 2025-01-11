package com.example.travelappbir

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferenceHelper {

    // Favoriler için
    private const val FAVORITES_PREFS_NAME = "favorites_prefs"
    private const val FAVORITES_KEY = "favorites"

    // Yorumlar için
    private const val COMMENTS_PREFS_NAME = "comments_prefs"
    private const val COMMENTS_KEY = "comments"

    // Favori Lokasyonları Kaydetme
    fun saveFavorites(context: Context, favorites: List<Location>) {
        val sharedPreferences = context.getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(favorites)
        editor.putString(FAVORITES_KEY, json)
        editor.apply()
    }

    // Favori Lokasyonları Getirme
    fun getFavorites(context: Context): MutableList<Location> {
        val sharedPreferences = context.getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(FAVORITES_KEY, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Location>>() {}.type
        return Gson().fromJson(json, type)
    }

    // Yorumları Kaydetme (Her lokasyon için ayrı ayrı)
    fun saveComments(context: Context, locationName: String, comments: List<Comment>) {
        val sharedPreferences = context.getSharedPreferences(COMMENTS_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val allComments = getAllComments(context).toMutableMap()
        allComments[locationName] = comments
        val json = Gson().toJson(allComments)
        editor.putString(COMMENTS_KEY, json)
        editor.apply()
    }

    // Belirli bir lokasyon için yorumları alma
    fun getComments(context: Context, locationName: String): List<Comment> {
        return getAllComments(context)[locationName] ?: emptyList()
    }

    // Tüm yorumları alma (Lokasyonlara göre)
    private fun getAllComments(context: Context): Map<String, List<Comment>> {
        val sharedPreferences = context.getSharedPreferences(COMMENTS_PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(COMMENTS_KEY, null)
        val type = object : TypeToken<Map<String, List<Comment>>>() {}.type
        return if (json != null) Gson().fromJson(json, type) else emptyMap()
    }
}
