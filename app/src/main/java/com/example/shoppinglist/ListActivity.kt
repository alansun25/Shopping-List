package com.example.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.shoppinglist.adapter.ItemAdapter
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.Item
import com.example.shoppinglist.databinding.ActivityListBinding
import com.example.shoppinglist.dialog.ItemDialog
import com.example.shoppinglist.touch.ItemRecyclerTouchCallback
import com.google.android.material.snackbar.Snackbar
import kotlin.concurrent.thread

class ListActivity : AppCompatActivity(), ItemDialog.ItemHandler {

    companion object {
        const val KEY_ITEM_EDIT = "KEY_ITEM_EDIT"
    }

    private lateinit var binding: ActivityListBinding
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        binding.fab.setOnClickListener { view ->
            ItemDialog().show(supportFragmentManager, "Item_DIALOG")
        }

        binding.btnDelAll.setOnClickListener {
            thread {
                AppDatabase.getInstance(this).itemDao().deleteAll()
            }
        }

        binding.btnDelBought.setOnClickListener {
            thread {
                AppDatabase.getInstance(this).itemDao().deleteBought()
            }
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ItemAdapter(this)
        binding.recyclerItems.adapter = adapter

        val touchCallbackList = ItemRecyclerTouchCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbackList)
        itemTouchHelper.attachToRecyclerView(binding.recyclerItems)

        val todoItems = AppDatabase.getInstance(this).itemDao().getAll()
        todoItems.observe(this, Observer { items ->
            adapter.submitList(items)
        })
    }


    fun showEditDialog(todoToEdit: Item) {
        val dialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_EDIT, todoToEdit)
        dialog.arguments = bundle

        dialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }

    override fun itemCreated(todo: Item) {
        thread {
            AppDatabase.getInstance(this).itemDao().insertItem(todo)

            runOnUiThread {
                Snackbar.make(binding.root, "${todo.name} added", Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) {
                        adapter.deleteLastItem()
                    }
                    .show()
            }
        }
    }

    override fun itemUpdated(todo: Item) {
        thread {
            AppDatabase.getInstance(this).itemDao().updateItem(todo)
        }
    }
}