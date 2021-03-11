package com.vikas.mobile.mysafenotes.ui

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.entity.Category
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_cateogry_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.buttonAddCategoryCancel).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.buttonAddCategoryAdd).setOnClickListener {
            Category(getKeyedCategoryName()).run {
                addCategoryViewModel.addCategory(this)
            }
            dismiss()
        }
    }

    private fun getKeyedCategoryName() : String {
        val categoryName = view?.findViewById<EditText>(R.id.edtTextAddCategory)?.text
        return categoryName.toString().trim()
    }

    companion object {

        fun newInstance(): AddCategoryDialogFragment =
                AddCategoryDialogFragment()

    }
}