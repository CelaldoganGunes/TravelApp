package com.example.travelappbir

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// ExpenseTrackerActivity: Harcama takibini yöneten bir aktivite.
class ExpenseTrackerActivity : AppCompatActivity() {

    // Kullanıcı arayüzü bileşenleri
    private lateinit var categorySpinner: Spinner // Harcama kategorilerini seçmek için spinner
    private lateinit var descriptionEditText: EditText // Harcama açıklaması girişi
    private lateinit var amountEditText: EditText // Harcama tutarı girişi
    private lateinit var saveButton: Button // Harcamayı kaydetmek için buton
    private lateinit var expenseRecyclerView: RecyclerView // Harcama listesini göstermek için RecyclerView
    private lateinit var adapter: ExpenseAdapter // RecyclerView için adaptör

    // Harcama listesini tutan değişken
    private var expenseList: MutableList<Expense> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_tracker)

        // Geri butonunu etkinleştir ve başlık ayarla
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Harcama Takip"

        // Kullanıcı arayüzü bileşenlerini başlat
        categorySpinner = findViewById(R.id.spinnerCategory)
        descriptionEditText = findViewById(R.id.editTextDescription)
        amountEditText = findViewById(R.id.editTextAmount)
        saveButton = findViewById(R.id.buttonSave)
        expenseRecyclerView = findViewById(R.id.recyclerViewExpenses)

        // Spinner için kategoriler tanımlanıyor
        val categories = listOf("Yemek", "Eğlence", "Ulaşım", "Kira", "Alışveriş", "Diğer")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        // RecyclerView için adaptör ve düzenleyici
        adapter = ExpenseAdapter(expenseList) { expense ->
            deleteExpense(expense) // Silme işlemini tetiklemek için tıklama olayını ayarla
        }
        expenseRecyclerView.layoutManager = LinearLayoutManager(this)
        expenseRecyclerView.adapter = adapter

        // Kaydedilmiş harcamaları yükle
        loadExpenses()

        // Kaydet butonuna tıklama olayı tanımla
        saveButton.setOnClickListener {
            saveExpense()
        }
    }

    // Yeni bir harcama kaydeder
    private fun saveExpense() {
        val category = categorySpinner.selectedItem.toString() // Seçilen kategori
        val description = descriptionEditText.text.toString() // Açıklama
        val amount = amountEditText.text.toString().toDoubleOrNull() // Tutar (Double'a dönüştürülür)

        // Geçersiz girişleri kontrol et
        if (description.isBlank() || amount == null || amount <= 0) {
            Toast.makeText(this, "Lütfen geçerli bir açıklama ve miktar giriniz.", Toast.LENGTH_SHORT).show()
            return
        }

        // Yeni harcama oluştur ve listeye ekle
        val newExpense = Expense(category, description, amount)
        expenseList.add(newExpense)
        saveExpenses() // Harcamayı kaydet
        adapter.notifyDataSetChanged() // RecyclerView'i güncelle

        // Form alanlarını temizle
        descriptionEditText.text.clear()
        amountEditText.text.clear()
        Toast.makeText(this, "Harcama kaydedildi.", Toast.LENGTH_SHORT).show()
        updateCategoryTotals() // Kategori toplamlarını güncelle
    }

    // Harcamayı listeden ve kayıttan siler
    private fun deleteExpense(expense: Expense) {
        expenseList.remove(expense)
        saveExpenses() // Harcama listesini güncelle
        adapter.updateExpenses(expenseList)
        Toast.makeText(this, "Harcama silindi.", Toast.LENGTH_SHORT).show()
        updateCategoryTotals() // Kategori toplamlarını güncelle
    }

    // Harcamaları SharedPreferences içine JSON olarak kaydeder
    private fun saveExpenses() {
        val sharedPreferences = getSharedPreferences("expense_tracker", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(expenseList) // Harcama listesini JSON formatına dönüştür
        editor.putString("expenses", json)
        editor.apply() // Değişiklikleri uygula
    }

    // SharedPreferences'ten harcamaları yükler
    private fun loadExpenses() {
        val sharedPreferences = getSharedPreferences("expense_tracker", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("expenses", null) ?: return
        val type = object : TypeToken<MutableList<Expense>>() {}.type
        expenseList = Gson().fromJson(json, type) // JSON'u harcama listesine dönüştür
        adapter.updateExpenses(expenseList)
        updateCategoryTotals() // Kategori toplamlarını güncelle
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Geri butonuna basıldığında aktiviteyi kapat
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Her kategori için toplam harcamaları hesaplar
    private fun calculateCategoryTotals(): Map<String, Double> {
        val totals = mutableMapOf<String, Double>()
        for (expense in expenseList) {
            val category = expense.category
            totals[category] = (totals[category] ?: 0.0) + expense.amount // Kategoriye göre toplamı güncelle
        }
        return totals
    }

    // Kategori toplamlarını kullanıcı arayüzüne günceller
    private fun updateCategoryTotals() {
        val totals = calculateCategoryTotals()
        val layoutCategoryTotals: LinearLayout = findViewById(R.id.layoutCategoryTotals)
        layoutCategoryTotals.removeAllViews() // Önceki verileri temizle

        // Her bir kategori için toplam tutarı ekrana yazdır
        for ((category, total) in totals) {
            val textView = TextView(this)
            textView.text = "$category: ${"%.2f".format(total)} TL"
            textView.textSize = 14f
            layoutCategoryTotals.addView(textView)
        }
    }
}
