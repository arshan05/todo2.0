package com.example.projecttodo

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks : Flow<List<Task>> = taskDao.getTask()
    fun getParticularTag(tagIn:String): Flow<List<Task>>{
        return taskDao.getParticularTag(tagIn)
    }
    fun getTodayTask(dateIn:String):Flow<List<Task>>{
        return taskDao.getTodayTask(dateIn)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(task: Task){
        taskDao.insert(task)
    }
    suspend fun delete(task:Task){
        taskDao.delete(task)
    }
    suspend fun update(task: Task){
        taskDao.update(task)
    }

}