package com.vikas.mobile.mynotes.ui

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.vikas.mobile.mynotes.R
import dagger.hilt.android.AndroidEntryPoint
import observeOnce
import java.util.*

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 */
@AndroidEntryPoint
class AddCategoryDialogFragment : BottomSheetDialogFragment() {

    private val addCategoryViewModel: AddCategoryViewModel by viewModels()
    private val allCategoriesName = mutableListOf<String>()

    lateinit var buttonAdd: Button
    lateinit var editTextCategory: EditText
    lateinit var textViewError: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val inflatedView = inflater.inflate(R.layout.add_cateogry_layout, container, false)
        buttonAdd = inflatedView.findViewById(R.id.buttonAddCategoryAdd)
        editTextCategory = inflatedView.findViewById(R.id.edtTextAddCategory)
        textViewError = inflatedView.findViewById(R.id.textAddCategoryError)

        return inflatedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addCategoryViewModel.fetchAllCategories().observeOnce(this, { categoryList ->
            categoryList.forEach { category ->
                allCategoriesName.add(category.name.content.toLowerCase(Locale.ROOT))
            }
        })

        view.findViewById<Button>(R.id.buttonAddCategoryCancel).setOnClickListener {
            dismiss()
        }

        buttonAdd.setOnClickListener {
            addCategoryViewModel.addCategory(getKeyedCategoryName())
            dismiss()
        }

        editTextCategory.addTextChangedListener {
            if (allCategoriesName.contains(it.toString().trim().toLowerCase(Locale.ROOT))) {
                buttonAdd.isEnabled = false
                textViewError.visibility = View.VISIBLE
                textViewError.text = getString(R.string.error_category_already_exist)
            } else {
                textViewError.visibility = View.GONE
                buttonAdd.isEnabled = true
            }
        }
    }

    private fun getKeyedCategoryName() : String {
        return editTextCategory.text.toString().trim()
    }

    companion object {

        fun newInstance(): AddCategoryDialogFragment =
                AddCategoryDialogFragment()

    }
}