package com.example.projecttodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TagsViewModel(private val tagsDao: TagsDao):ViewModel() {
    fun getTags() = tagsDao.getTags()

    fun insert(tag: Tags) = viewModelScope.launch{
        tagsDao.insert(tag)
    }
    fun delete(tag: Tags) = viewModelScope.launch{
        tagsDao.delete(tag)
    }
}

class TagsViewModelFactory(private val tagsDao: TagsDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagsViewModel(tagsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}