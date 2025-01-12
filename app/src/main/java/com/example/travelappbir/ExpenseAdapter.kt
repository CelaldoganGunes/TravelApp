package com.example.travelappbir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private var expenses: MutableList<Expense>,
    private val onDeleteClick: (Expense) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory: TextView = itemView.findViewById(R.id.textCategory)
        val textDescription: TextView = itemView.findViewById(R.id.textDescription)
        val textAmount: TextView = itemView.findViewById(R.id.textAmount)
        val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.textCategory.text = expense.category
        holder.textDescription.text = expense.description
        holder.textAmount.text = "%.2f TL".format(expense.amount)

        holder.buttonDelete.setOnClickListener {
            onDeleteClick(expense)
        }
    }

    override fun getItemCount(): Int = expenses.size

    fun updateExpenses(newExpenses: MutableList<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}
