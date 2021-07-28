package com.youssefelsa3ed.almatarchallenge

import androidx.lifecycle.ViewModelProvider
import com.youssefelsa3ed.almatarchallenge.api.SearchService
import com.youssefelsa3ed.almatarchallenge.data.SearchRepository
import com.youssefelsa3ed.almatarchallenge.ui.ViewModelFactory

object Injection {

    private fun provideSearchService(): SearchService {
        return SearchService()
    }

    private fun provideGithubRepository(): SearchRepository {
        return SearchRepository(provideSearchService())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository())
    }
}