package com.example.urbanquest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanquest.R
import com.example.urbanquest.data.models.ProductEntity

class ProductAdapter(private var products: List<ProductEntity>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView=itemView.findViewById(R.id.number)
        val titleTextView: TextView = itemView.findViewById(R.id.nameElement)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descElement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.elementfor4task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.idTextView.text= product.id.toString()
        holder.titleTextView.text = product.title
        holder.descriptionTextView.text = product.description
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setData(newProducts: List<ProductEntity>) {
        products = newProducts
        notifyDataSetChanged()
    }
}