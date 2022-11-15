package com.example.projecttodo

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class HomeFragment : Fragment(), OnItemLongPressed,OnItemChecked {
    lateinit var datePickerDialog:DatePickerDialog
    lateinit var adapter:ListAdapter<Task,TaskViewHolder>
    val monthName = arrayListOf<String>(
        "Jan", "Feb",
        "Mar", "Apr", "May", "Jun", "Jul",
        "Aug", "Sept", "Oct", "Nov",
        "Dec"
    )

//    var data = arrayListOf<Model>()
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var dateString: String? = ""


    val taskViewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory((activity?.application as TaskApplication).database.taskDao())
    }

    val tagsViewModel: TagsViewModel by activityViewModels {
        TagsViewModelFactory((activity?.application as TaskApplication).database.tagsDao())
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val chipRecyclerView = view.findViewById<RecyclerView>(R.id.chipRecyclerView)
//        chipRecyclerView.layoutManager = LinearLayoutManager(view.context,LinearLayoutManager.HORIZONTAL, false)
        var chipGroup = view.findViewById<ChipGroup>(R.id.chip_group)
//        var chipData:MutableList<Tags> = mutableListOf()
//        chipData.add(Tags("all"))
//        chipData.add(Tags("today"))

//        var chipAdapter = ChipAdapter()
//        chipRecyclerView.adapter = chipAdapter

        lifecycle.coroutineScope.launch {
            tagsViewModel.getTags().collect() {
//                chipData.addAll(it)
                it.forEach(){
                    chipGroup.addChip(view.context,it.tag)
                }
//                chipAdapter.submitList(chipData)
            }
        }

        chipGroup.isSingleSelection = true
        chipGroup.isSelectionRequired = true

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = MyAdapter(this,this)
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)
        recyclerView.adapter = adapter


        chipGroup.check(chipGroup.checkedChipId)

        val checked = chipGroup.findViewById<Chip>(chipGroup.checkedChipId)
        if (checked.text == "all"){
            lifecycle.coroutineScope.launch {
                taskViewModel.allTasks().collect() {
                    adapter.submitList(it)
                }
            }
        }

        chipGroup.setOnCheckedStateChangeListener{ chipGroup, checkedId ->
            var checkedChip = chipGroup.findViewById<Chip>(checkedId.get(0))

            if (checkedChip.text == "all"){
                lifecycle.coroutineScope.launch {
                    taskViewModel.allTasks().collect() {
                        adapter.submitList(it)
                    }
                }
            }
            else if(checkedChip.text == "today"){
                var dateIn = LocalDate.now().dayOfMonth
                var monthIn = LocalDate.now().month.value
                var yearIn = LocalDate.now().year

                var dateTemp = String.format("%s %d, %d",monthName[monthIn-1], dateIn, yearIn)
                Log.d("date today", dateTemp)
                lifecycle.coroutineScope.launch {
                    taskViewModel.getTodayTask(dateTemp).collect() {
                        adapter.submitList(it)
                    }
                }
            }
            else{
                var checkedTagString = checkedChip.text.toString()
                lifecycle.coroutineScope.launch {
                    taskViewModel.getParticularTag(checkedTagString).collect() {
                        adapter.submitList(it)
                    }
                }
            }


        }

//        chipGroup.setOnCheckedChangeListener { chipGroup, checkedId ->
//            var checkedChip = chipGroup.findViewById<Chip>(checkedId)
////            checkedChip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.SEC_500))
//        }


        val floatingActionButton = view.findViewById<FloatingActionButton>(R.id.fab)
        floatingActionButton.setOnClickListener{

            val dialog = Dialog(view.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.add_task)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val closeBtn = dialog.findViewById(R.id.closeIn) as ImageButton
            val addBtnIn = dialog.findViewById<ImageButton>(R.id.addBtnIn)
            val priorityText = dialog.findViewById<TextView>(R.id.priorityText)
            val datePick = dialog.findViewById<Chip>(R.id.dateIn)

            val radioIn = dialog.findViewById<RadioGroup>(R.id.radioBtnGroup)

            radioIn.setOnCheckedChangeListener{ RadioGroup, isChecked ->
                when(radioIn.checkedRadioButtonId) {
                    R.id.highRadioBtn -> {
                        priorityText.text = getString(R.string.high)
                    }
                    R.id.mediumRadioBtn -> {
                        priorityText.text = getString(R.string.medium)
                    }
                    R.id.lowRadioBtn -> {
                        priorityText.text = getString(R.string.low)
                    }
                    R.id.noRadioBtn -> {
                        priorityText.text = getString(R.string.none)
                    }
                }
            }


            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            dateString = String.format("%s %d, %d",monthName[month], day, year)

            datePick.text = "today"

            datePick.setOnClickListener() {
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)


                datePickerDialog =
                    DatePickerDialog(view.context, object : DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(
                            view: DatePicker?,
                            year: Int,
                            month: Int,
                            dayOfMonth: Int
                        ) {
                        dateString = String.format("%s %d, %d",monthName[month], dayOfMonth, year)
                            if (dayOfMonth == day) {
                                datePick.setText("today")
                            } else {
                                dateString = String.format("%s %d, %d",monthName[month], dayOfMonth, year)
                                datePick.setText(
                                    String.format(
                                        "%s %d, %d",
                                        monthName[month],
                                        dayOfMonth,
                                        year
                                    )
                                )
                            }
                        }
                    }, year, month, day)
//                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            }
            val tagSpinner = dialog.findViewById<Spinner>(R.id.tagIn)

            lifecycle.coroutineScope.launch {
                tagsViewModel.getTags().collect() {
                    val spinnerAdapter = SpinnerAdapter(view.context, it)
                    tagSpinner.adapter = spinnerAdapter
                }
            }


            closeBtn.setOnClickListener {
                dialog.dismiss()
            }
            addBtnIn.setOnClickListener{
                var taskIn = dialog.findViewById<EditText>(R.id.taskIn).text
                var priorityIn:Int = -1



                when(radioIn.checkedRadioButtonId){
                    R.id.highRadioBtn -> {
                        priorityIn = 1
                        priorityText.text = getString(R.string.high)
                    }
                    R.id.mediumRadioBtn -> {
                        priorityIn = 2
                        priorityText.text = getString(R.string.medium)
                    }
                    R.id.lowRadioBtn -> {
                        priorityIn = 3
                        priorityText.text = getString(R.string.low)
                    }
                    R.id.noRadioBtn -> {
                        priorityIn = 4
                        priorityText.text = getString(R.string.none)
                    }
                }

//                var dateIn = Date(year,month,day)

                if (taskIn.toString().isBlank()){
                    Snackbar.make(view,"Add Valid Task",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    val tagInput = tagSpinner.selectedView.findViewById<TextView>(R.id.spinnerItem).text.toString()
                    val input = Task(taskIn.toString(),priorityIn,dateString,false,tagInput)
                    taskViewModel.insert(input)
//                    dialog.findViewById<TextView>(R.id.inTaskText).text = ""
                    dialog.dismiss()
                }

            }
            dialog.show()
        }

//        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
//        adapter = MyAdapter(this,this)
//        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)
//        recyclerView.adapter = adapter
//
//        lifecycle.coroutineScope.launch {
//            taskViewModel.allTasks().collect() {
//                adapter.submitList(it)
//            }
//        }

//        taskViewModel.allTasks().collect() { tasks ->
//            tasks?.let { adapter.submitList(it) }
//        }

    }

    override fun onItemLongPressed(viewModel: Task, position: Int) {
        val taskToDelete = viewModel
        taskViewModel.delete(taskToDelete)
        val curView = view
        if (curView != null) {
            Snackbar.make(curView, "Deleted", Snackbar.LENGTH_LONG)
                .setAction("undo"){
                    taskViewModel.insert(viewModel)
                }
                .show()
        }
//        adapter.notifyItemRemoved(position)
    }


    override fun onItemChecked(viewModel: Task, position: Int) {

        if (viewModel.isCompleted == false){
//            itemView.findViewById<CheckBox>(R.id.inCheckbox).isChecked = false
            viewModel.isCompleted = true
            var x = viewModel.priority
            if(x!=null)
                x += 5
            viewModel.priority = x
            taskViewModel.updateItem(viewModel.task, viewModel.priority, viewModel.date, viewModel.isCompleted,viewModel.tag,viewModel.id)
        }
        else{
//            itemView.findViewById<CheckBox>(R.id.inCheckbox).isChecked = true
            var x = viewModel.priority
            if(x!=null)
                x -= 5
            viewModel.isCompleted = false
            viewModel.priority = x
            taskViewModel.updateItem(viewModel.task, viewModel.priority, viewModel.date, viewModel.isCompleted,viewModel.tag,viewModel.id)
        }

        adapter.notifyItemChanged(position)
    }
//    companion object {
//        @JvmStatic
//        fun newInstance():Home{
//            return Home()
//        }
//    }
}

private fun ChipGroup.addChip(ctx:Context, tag: String?) {
    Chip(ctx).apply {

        id = View.generateViewId()
        text = tag
//        setChipIconResource(R.drawable.tag)
        isCheckable = true
        isClickable = true
        textSize = 18F
        setTextColor(Color.BLACK)
//        checkedIconTint = ColorStateList(R.color.state_color,R.color.SEC_700)
//        setCheckedIconTintResource(R.color.SEC_500)
        isCheckedIconVisible = true
        isFocusable = true
        addView(this)
    }
}
