package com.vikas.mobile.mysafenotes.ui

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.Repository
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.ui.dashboard.AddCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

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
        return categoryName.toString()
    }

    companion object {

        fun newInstance(): AddCategoryDialogFragment =
                AddCategoryDialogFragment()

    }
}