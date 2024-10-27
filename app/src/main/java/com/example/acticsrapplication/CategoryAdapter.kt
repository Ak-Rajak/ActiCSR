package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

data class Category(
    val imageResId: Int,  // Resource ID for the image
    val description: String // Optional description or name of the category
)

class CategoryAdapter(private val categoryList: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage: ImageView = itemView.findViewById(R.id.category_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_home, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryImage.setImageResource(category.imageResId)

        // Optional: Handle click events
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, category.description, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = categoryList.size
}
