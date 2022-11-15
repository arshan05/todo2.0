package com.example.projecttodo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
    val tagsViewModel: TagsViewModel by activityViewModels {
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

            var tagDialog = Dialog(view.context)
            tagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            tagDialog.setCancelable(false)
            tagDialog.setContentView(R.layout.add_tag)
            tagDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val tagCloseTag = tagDialog.findViewById<ImageButton>(R.id.closeTagIn)
            val tagInTag = tagDialog.findViewById<EditText>(R.id.tagInEt)
            val addTag = tagDialog.findViewById<ImageButton>(R.id.addTagBtnIn)

            tagCloseTag.setOnClickListener {
                tagDialog.dismiss()
            }

            addTag.setOnClickListener{
                if (tagInTag.text.toString().isBlank()){
                    Snackbar.make(view, "Enter Valid Tag", Snackbar.LENGTH_SHORT).show()
                }
                else{
                    val inputTagText = tagInTag.text.toString()
                    tagsViewModel.insert(Tags(inputTagText))
                    tagDialog.dismiss()
                }
            }

//            val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(view.context)
//            builder.setContentView(R.layout.add_tag)
//            builder.setTitle("Add Tag")

//            val linearLayout = LinearLayout(view.context)
//            linearLayout.setOrientation(VERTICAL)
//
//
//            val inputTag = EditText(view.context)
//            inputTag.inputType = InputType.TYPE_CLASS_TEXT
//            inputTag.hint = "work"
//
//            linearLayout.addView(inputTag)
//            builder.setView(linearLayout)
//
//            builder.setPositiveButton("Submit", DialogInterface.OnClickListener{
//                    dialog, which ->
//                val inputTagText = inputTag.text.toString()
//                tagsViewModel.insert(Tags(inputTagText))
//            })
//            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            tagDialog.show()



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