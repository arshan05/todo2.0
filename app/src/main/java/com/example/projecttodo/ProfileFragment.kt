package com.example.projecttodo

import android.animation.LayoutTransition
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projecttodo.tagTable.*
import com.example.projecttodo.taskTable.TaskApplication
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), OnTagLongPressed {
    lateinit var adapter: ListAdapter<Tags, TagsViewHolder>
    val tagsViewModel: TagsViewModel by activityViewModels {
        TagsViewModelFactory((activity?.application as TaskApplication).tagRepository)
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
        val addTagBtn = view.findViewById<ImageButton>(R.id.addTagBtn)

        val recyclerView = view.findViewById<RecyclerView>(R.id.tagsRecyclerView)
        adapter = TagsAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)
        recyclerView.adapter = adapter

        val card = view.findViewById<CardView>(R.id.cv)
        val layoutTransition: LayoutTransition = card.layoutTransition
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        val tagDisplay = view.findViewById<Chip>(R.id.tagsDisplayTitle)
        val textCaution = view.findViewById<TextView>(R.id.caution)
        tagDisplay.setOnClickListener{
            recyclerView.visibility = if (recyclerView.visibility == View.VISIBLE) View
                .GONE else View.VISIBLE

            textCaution.visibility = if (textCaution.visibility == View.VISIBLE) View
                .GONE else View.VISIBLE
        }


        lifecycle.coroutineScope.launch {
            tagsViewModel.allTags.collect {
                adapter.submitList(it)
            }
        }

        addTagBtn.setOnClickListener{

            val tagDialog = Dialog(view.context)
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
                    tagDialog.dismiss()
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
        tagsViewModel.delete(viewModel)
        val curView = view
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