package com.example.shoppinglist.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.shoppinglist.ListActivity
import com.example.shoppinglist.R
import com.example.shoppinglist.data.Item
import com.example.shoppinglist.databinding.ActivityDialogBinding

class ItemDialog : DialogFragment() {
    interface ItemHandler {
        fun itemCreated(item: Item)
        fun itemUpdated(item: Item)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemHandler){
            itemHandler = context
        } else {
            throw RuntimeException(
                "The Activity is not implementing the ItemHandler interface.")
        }
    }

    private lateinit var binding: ActivityDialogBinding

    private var isEditMode = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        if (arguments != null && requireArguments().containsKey(
                ListActivity.KEY_ITEM_EDIT)) {
            isEditMode = true
            dialogBuilder.setTitle("Edit Item")
        } else {
            isEditMode = false
            dialogBuilder.setTitle("New Item")
        }

        binding = ActivityDialogBinding.inflate(requireActivity().layoutInflater)
        dialogBuilder.setView(binding.root)

        // Pre-fill the dialog if we are in edit mode
        if (isEditMode) {
            val itemToEdit =
                requireArguments().getSerializable(
                    ListActivity.KEY_ITEM_EDIT) as Item

            binding.etName.setText(itemToEdit.name)
            binding.etPrice.setText(itemToEdit.price)
            binding.etDesc.setText(itemToEdit.desc)
            binding.spinnerCats.setSelection(itemToEdit.category)
        }

        val catAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            android.R.layout.simple_spinner_item
        )

        catAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerCats.adapter = catAdapter

        dialogBuilder.setPositiveButton("OK") {
                dialog, which ->
        }

        dialogBuilder.setNegativeButton("Cancel") {
                dialog, which ->
        }

        return dialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            if (binding.etName.text.isEmpty()) {
                binding.etName.error = "Please enter an item name."
            } else {
                if (isEditMode) {
                    handleEdit()
                } else {
                    handleCreate()
                }

                dialog.dismiss()
            }
        }
    }

    private fun handleCreate() {
        itemHandler.itemCreated(
            Item(
                null,
                binding.spinnerCats.selectedItemPosition,
                binding.etName.text.toString(),
                binding.etDesc.text.toString(),
                binding.etPrice.text.toString(),
                binding.cbDialogBought.isChecked
            )
        )
    }

    private fun handleEdit() {
        val todoToEdit =
            (requireArguments().getSerializable(
                ListActivity.KEY_ITEM_EDIT
            ) as Item).copy(
                itemId = null,
                category = binding.spinnerCats.selectedItemPosition,
                name = binding.etName.text.toString(),
                desc = binding.etDesc.text.toString(),
                price = binding.etPrice.text.toString(),
                bought = binding.cbDialogBought.isChecked
            )

        itemHandler.itemUpdated(todoToEdit)
    }
}