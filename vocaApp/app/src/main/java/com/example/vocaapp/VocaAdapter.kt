package com.example.vocaapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocaapp.databinding.ItemVocaBinding

class VocaAdapter(
    val list: MutableList<Voca>,
    private val itemClickListener: ItemClickListener? = null,
) : RecyclerView.Adapter<VocaAdapter.VocaViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocaViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemVocaBinding.inflate(inflater, parent, false)
        return VocaViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VocaViewHolder, position: Int) {
        val voca = list[position]
        holder.bind(voca)
        holder.itemView.setOnClickListener { itemClickListener?.onClick(voca) }
    }


    class VocaViewHolder(val binding: ItemVocaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(voca: Voca) {
            binding.apply {
                textTextView.text = voca.text
                meanTextView.text = voca.mean
                typeChip.text = voca.type
            }
        }
    }

    interface ItemClickListener {
        fun onClick(voca: Voca)
    }
}