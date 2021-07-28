package com.youssefelsa3ed.almatarchallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youssefelsa3ed.almatarchallenge.data.SearchRepository

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val repository: SearchRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DocSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DocSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}