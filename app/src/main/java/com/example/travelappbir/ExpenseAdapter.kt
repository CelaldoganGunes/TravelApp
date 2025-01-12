package com.example.travelappbir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private var expenses: MutableList<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTextView: TextView = view.findViewById(R.id.textCategory)
        val descriptionTextView: TextView = view.findViewById(R.id.textDescription)
        val amountTextView: TextView = view.findViewById(R.id.textAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.categoryTextView.text = expense.category
        holder.descriptionTextView.text = expense.description
        holder.amountTextView.text = "${expense.amount} TL"
    }

    override fun getItemCount(): Int = expenses.size

    fun updateExpenses(newExpenses: MutableList<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}
