package com.example.projecttodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projecttodo.taskTable.Task
import com.example.projecttodo.taskTable.TaskRepository
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val allTask = repository.allTasks

    fun getParticularTag(tagIn:String) = repository.getParticularTag(tagIn)
    fun getTodayTask(dateIn:String) = repository.getTodayTask(dateIn)


//    fun allTasks() = viewModelScope.launch {
//        taskDao.getTask()
//    }
//
    fun insert(task: Task) = viewModelScope.launch{
        repository.insert(task)
    }
    fun delete(task: Task) = viewModelScope.launch{
        repository.delete(task)
    }
    fun update(task: Task) = viewModelScope.launch{
        repository.update(task)
    }
//
//    fun getParticularTag(tagIn:String) = taskDao.getParticularTag(tagIn)
//
//    fun getTodayTask(dateIn:String) = taskDao.getTodayTask(dateIn)

//    fun updateItem(
//        task: String?,
//        priority: Int?,
//        date: String?,
//        isCompleted:Boolean?,
//        tag: String?,
//        id: Int?
//    ) {
//        val updatedItem = getUpdatedItemEntry(task, priority, date, isCompleted,tag,id)
//        updateItem(updatedItem)
//    }
//
//    private fun getUpdatedItemEntry(
//        task: String?, priority: Int?, date: String?, iscompleted: Boolean?,
//        tag: String?, id: Int?): Task {
//        return Task(task, priority,date, iscompleted,tag,id)
//    }
//
//    private fun updateItem(task: Task) {
//        viewModelScope.launch {
//            taskDao.update(task)
//        }
//    }
//    fun update(task: Task) = viewModelScope.launch{
//        taskDao.update(task)
//    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}