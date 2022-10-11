package com.example.projecttodo

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), OnTagLongPressed {
    lateinit var adapter: ListAdapter<Tags, TagsViewHolder>
    private val tagsViewModel: TagsViewModel by activityViewModels {
        TagsViewModelFactory((activity?.application as TaskApplication).database.tagsDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addTagBtn = view.findViewById<Button>(R.id.addTagBtn)

        val recyclerView = view.findViewById<RecyclerView>(R.id.tagsRecyclerView)
        adapter = TagsAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)
        recyclerView.adapter = adapter

        lifecycle.coroutineScope.launch {
            tagsViewModel.getTags().collect() {
                adapter.submitList(it)
            }
        }

        addTagBtn.setOnClickListener{
            val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(view.context)
            builder.setTitle("Add Tag")

            val linearLayout = LinearLayout(view.context)
            linearLayout.setOrientation(VERTICAL)


            val inputTag = EditText(view.context)
            inputTag.inputType = InputType.TYPE_CLASS_TEXT
            inputTag.hint = "work"

            linearLayout.addView(inputTag)
            builder.setView(linearLayout)

            builder.setPositiveButton("Submit", DialogInterface.OnClickListener{
                    dialog, which ->
                val inputTagText = inputTag.text.toString()
                tagsViewModel.insert(Tags(inputTagText))
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()



        }

    }

    companion object {
        @JvmStatic
        fun newInstance() : ProfileFragment{
            return ProfileFragment()
        }
    }

    override fun onTagLongPressed(viewModel: Tags, position: Int) {
        val tagToDelete = viewModel
        tagsViewModel.delete(tagToDelete)
        var curView = view
        if (curView != null) {
            Snackbar.make(curView, "Deleted", Snackbar.LENGTH_LONG)
                .setAction("undo"){
                    tagsViewModel.insert(viewModel)
                }
                .show()
        }
        adapter.notifyItemRemoved(position)
    }
}