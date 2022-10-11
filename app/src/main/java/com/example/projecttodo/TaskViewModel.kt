package com.example.projecttodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {
    fun allTasks() : Flow<List<Task>> = taskDao.getTask()

    fun insert(task: Task) = viewModelScope.launch{
        taskDao.insert(task)
    }
    fun delete(task: Task) = viewModelScope.launch{
        taskDao.delete(task)
    }

    fun updateItem(
        task: String?,
        priority: Int?,
        date: Date?,
        isCompleted:Boolean?,
        tag: String?,
        id: Int?
    ) {
        val updatedItem = getUpdatedItemEntry(task, priority, date, isCompleted,tag,id)
        updateItem(updatedItem)
    }

    private fun getUpdatedItemEntry(
        task: String?, priority: Int?, date: Date?, iscompleted: Boolean?,
        tag: String?, id: Int?): Task {
        return Task(task, priority,date, iscompleted,tag,id)
    }

    private fun updateItem(task: Task) {
        viewModelScope.launch {
            taskDao.update(task)
        }
    }
//    fun update(task: Task) = viewModelScope.launch{
//        taskDao.update(task)
//    }
}

class TaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}