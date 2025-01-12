package com.example.travelappbir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// ExpenseAdapter: RecyclerView için bir adaptör. Harcamaları listelemek ve yönetmek için kullanılır.
class ExpenseAdapter(
    private var expenses: MutableList<Expense>, // Harcamaları içeren liste
    private val onDeleteClick: (Expense) -> Unit // Silme işlemi için tıklama olayı dinleyicisi
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    // ViewHolder sınıfı: Her bir liste öğesinin görünümünü ve bileşenlerini temsil eder.
    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory: TextView = itemView.findViewById(R.id.textCategory) // Harcama kategorisi
        val textDescription: TextView = itemView.findViewById(R.id.textDescription) // Harcama açıklaması
        val textAmount: TextView = itemView.findViewById(R.id.textAmount) // Harcama miktarı
        val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete) // Harcamayı silmek için buton
    }

    // RecyclerView'de yeni bir öğe oluşturulduğunda çağrılır. Görünüm şişirilir ve bir ViewHolder döndürülür.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false) // "item_expense" layout dosyası kullanılır
        return ExpenseViewHolder(view)
    }

    // RecyclerView'deki her öğe bağlandığında çağrılır. Veriler ViewHolder bileşenlerine atanır.
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position] // Mevcut pozisyondaki harcamayı al

        // Harcama bilgilerini TextView bileşenlerine ata
        holder.textCategory.text = expense.category // Harcama kategorisi atanır
        holder.textDescription.text = expense.description // Harcama açıklaması atanır
        holder.textAmount.text = "%.2f TL".format(expense.amount) // Harcama tutarı formatlanıp atanır

        // Silme butonuna tıklama olayı tanımlanır
        holder.buttonDelete.setOnClickListener {
            onDeleteClick(expense) // Silme işlemini çağır
        }
    }

    // RecyclerView'deki toplam öğe sayısını döndürür
    override fun getItemCount(): Int = expenses.size

    // Harcama listesini günceller ve RecyclerView'i yeniden oluşturur
    fun updateExpenses(newExpenses: MutableList<Expense>) {
        expenses = newExpenses // Yeni harcama listesini ata
        notifyDataSetChanged() // RecyclerView'i güncelle
    }
}
