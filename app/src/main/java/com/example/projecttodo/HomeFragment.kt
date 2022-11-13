package com.example.projecttodo

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import java.util.*

class HomeFragment : Fragment(), OnItemLongPressed,OnItemChecked {
    lateinit var datePickerDialog:DatePickerDialog
    lateinit var adapter:ListAdapter<Task,TaskViewHolder>

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
        var view = inflater.inflate(R.layout.fragment_home,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val floatingActionButton = view.findViewById<FloatingActionButton>(R.id.fab)
        floatingActionButton.setOnClickListener{
//            Toast.makeText(view.context, "Hello", Toast.LENGTH_SHORT).show()

            val dialog = Dialog(view.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.add_task)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val closeBtn = dialog.findViewById(R.id.closeIn) as ImageButton
            val addBtnIn = dialog.findViewById<ImageButton>(R.id.addBtnIn)
            var priorityText = dialog.findViewById<TextView>(R.id.priorityText)
            val datePick = dialog.findViewById<Chip>(R.id.dateIn)

            val radioIn = dialog.findViewById<RadioGroup>(R.id.radioBtnGroup)

            radioIn.setOnCheckedChangeListener{ RadioGroup, isChecked ->
                when(radioIn.checkedRadioButtonId) {
                    R.id.highRadioBtn -> {
                        priorityText.text = "high"
                    }
                    R.id.mediumRadioBtn -> {
                        priorityText.text = "medium"
                    }
                    R.id.lowRadioBtn -> {
                        priorityText.text = "low"
                    }
                    R.id.noRadioBtn -> {
                        priorityText.text = "none"
                    }
                }
            }


            var monthName = arrayListOf<String>(
                "Jan", "Feb",
                "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sept", "Oct", "Nov",
                "Dec"
            )

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
//                        dateString = String.format("%s %d, %d",monthName[month], dayOfMonth, year)
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
                        priorityText.text = "high"
                    }
                    R.id.mediumRadioBtn -> {
                        priorityIn = 2
                        priorityText.text = "medium"
                    }
                    R.id.lowRadioBtn -> {
                        priorityIn = 3
                        priorityText.text = "low"
                    }
                    R.id.noRadioBtn -> {
                        priorityIn = 4
                        priorityText.text = "none"
                    }
                }

                var dateIn = Date(year,month,day)

                if (taskIn.toString().isBlank()){
                    Snackbar.make(view,"Add Valid Task",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    val tagInput = tagSpinner.selectedView.findViewById<TextView>(R.id.spinnerItem).text.toString()
                    val input = Task(taskIn.toString(),priorityIn,dateIn,false,tagInput)
                    taskViewModel.insert(input)
//                    dialog.findViewById<TextView>(R.id.inTaskText).text = ""
                    dialog.dismiss()
                }

            }
            dialog.show()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = MyAdapter(this,this)
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)
        recyclerView.adapter = adapter

        lifecycle.coroutineScope.launch {
            taskViewModel.allTasks().collect() {
                adapter.submitList(it)
            }
        }

//        taskViewModel.allTasks().collect() { tasks ->
//            tasks?.let { adapter.submitList(it) }
//        }

    }

    override fun onItemLongPressed(viewModel: Task, position: Int) {
        val taskToDelete = viewModel
        taskViewModel.delete(taskToDelete)
        var curView = view
        if (curView != null) {
            Snackbar.make(curView, "Deleted", Snackbar.LENGTH_LONG)
                .setAction("undo"){
                    taskViewModel.insert(viewModel)
                }
                .show()
        }
        adapter.notifyItemRemoved(position)
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

//        Toast.makeText(view?.context, "${viewModel.priority}", Toast.LENGTH_SHORT).show()
        adapter.notifyItemChanged(position)
    }
//    companion object {
//        @JvmStatic
//        fun newInstance():Home{
//            return Home()
//        }
//    }
}