package com.example.unidex.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unidex.databinding.ItemUniversityBinding
import com.example.unidex.model.University

//adapter for the university list
class UniversityAdapter(
    private var items: List<University>,
    private val onItemClick: (University) -> Unit
) : RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val binding = ItemUniversityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UniversityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<University>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class UniversityViewHolder(
        private val binding: ItemUniversityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(university: University) {
            binding.tvUniversityName.text = university.name
            binding.tvUniversityCountry.text = university.country
            binding.root.setOnClickListener { onItemClick(university) }
        }
    }
}