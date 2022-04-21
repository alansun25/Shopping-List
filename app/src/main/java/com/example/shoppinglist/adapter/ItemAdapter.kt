package com.example.shoppinglist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.ListActivity
import com.example.shoppinglist.R
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.Item
import com.example.shoppinglist.databinding.ItemBinding
import com.example.shoppinglist.touch.ItemTouchHelperCallback
import kotlin.concurrent.thread

class ItemAdapter(var context: Context) :
    ListAdapter<Item, ItemAdapter.ViewHolder>(TodoDiffCallback()), ItemTouchHelperCallback {
    inner class ViewHolder(var binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.tvItemName.text = item.name
            binding.tvPrice.text = item.price
            binding.tvDesc.text = item.desc
            binding.cbBought.isChecked = item.bought

            when (item.category) {
                0 -> {
                    binding.itemIcon.setImageResource(R.drawable.fooddrink)
                }
                1 -> {
                    binding.itemIcon.setImageResource(R.drawable.supplies)
                }
                2 -> {
                    binding.itemIcon.setImageResource(R.drawable.tech)
                }
                3 -> {
                    binding.itemIcon.setImageResource(R.drawable.sports)
                }
                4 -> {
                    binding.itemIcon.setImageResource(R.drawable.misc)
                }
            }

            binding.btnDel.setOnClickListener {
                deleteItem(this.adapterPosition)
            }

            binding.btnEdit.setOnClickListener {
                (context as ListActivity).showEditDialog(
                    getItem(this.adapterPosition)
                )
            }

            binding.cbBought.setOnClickListener {
                val currentItem = getItem(adapterPosition)
                currentItem.bought = binding.cbBought.isChecked

                thread {
                    AppDatabase.getInstance(context).itemDao().updateItem(currentItem)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemBinding.inflate(
            LayoutInflater.from(context),
            parent, false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun deleteLastItem() {
        deleteItem(itemCount - 1)
    }

    fun deleteItem(idx: Int) {
        thread {
            AppDatabase.getInstance(context).itemDao().deleteItem(getItem(idx))
        }
    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }
}

class TodoDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}