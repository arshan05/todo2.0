package com.example.projecttodo

interface TaskCheckListener {
    fun onTaskCheckChange(model: Model, position:Int)
}