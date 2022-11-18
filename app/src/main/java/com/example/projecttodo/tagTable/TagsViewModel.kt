package com.example.projecttodo.tagTable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projecttodo.taskTable.TaskRepository
import kotlinx.coroutines.launch

class TagsViewModel(private val repository: TagsRepository):ViewModel() {
    val allTags = repository.allTags

    fun insert(tag: Tags) = viewModelScope.launch{
        repository.insert(tag)
    }
    fun delete(tag: Tags) = viewModelScope.launch{
        repository.delete(tag)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

}

class TagsViewModelFactory(private val repository: TagsRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}