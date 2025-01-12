package com.example.travelappbir

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExpenseTrackerActivity : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter

    private var expenseList: MutableList<Expense> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_tracker)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Harcama Takip"

        // UI bileşenlerini bul
        categorySpinner = findViewById(R.id.spinnerCategory)
        descriptionEditText = findViewById(R.id.editTextDescription)
        amountEditText = findViewById(R.id.editTextAmount)
        saveButton = findViewById(R.id.buttonSave)
        expenseRecyclerView = findViewById(R.id.recyclerViewExpenses)

        // Kategoriler Spinner'a ekleniyor
        val categories = listOf("Yemek", "Eğlence", "Ulaşım", "Kira", "Alışveriş", "Diğer")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        // RecyclerView için adapter ve layout manager
        adapter = ExpenseAdapter(expenseList)
        expenseRecyclerView.layoutManager = LinearLayoutManager(this)
        expenseRecyclerView.adapter = adapter

        // Harcamaları yükle
        loadExpenses()

        // Kaydet butonu işlemi
        saveButton.setOnClickListener {
            saveExpense()
        }
    }

    private fun saveExpense() {
        val category = categorySpinner.selectedItem.toString()
        val description = descriptionEditText.text.toString()
        val amount = amountEditText.text.toString().toDoubleOrNull()

        if (description.isBlank() || amount == null || amount <= 0) {
            Toast.makeText(this, "Lütfen geçerli bir açıklama ve miktar giriniz.", Toast.LENGTH_SHORT).show()
            return
        }

        val newExpense = Expense(category, description, amount)
        expenseList.add(newExpense)
        saveExpenses()
        adapter.notifyDataSetChanged()

        descriptionEditText.text.clear()
        amountEditText.text.clear()
        Toast.makeText(this, "Harcama kaydedildi.", Toast.LENGTH_SHORT).show()
    }

    private fun saveExpenses() {
        val sharedPreferences = getSharedPreferences("expense_tracker", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(expenseList)
        editor.putString("expenses", json)
        editor.apply()
    }

    private fun loadExpenses() {
        val sharedPreferences = getSharedPreferences("expense_tracker", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("expenses", null) ?: return
        val type = object : TypeToken<MutableList<Expense>>() {}.type
        expenseList = Gson().fromJson(json, type)
        adapter.updateExpenses(expenseList)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Geri butonuna basıldığında bir önceki sayfaya dön
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
