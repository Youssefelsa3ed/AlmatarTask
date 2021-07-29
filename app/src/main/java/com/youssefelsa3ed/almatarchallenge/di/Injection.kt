package com.youssefelsa3ed.almatarchallenge.di

import androidx.lifecycle.ViewModelProvider
import com.youssefelsa3ed.almatarchallenge.api.SearchHttpProvider
import com.youssefelsa3ed.almatarchallenge.api.SearchService
import com.youssefelsa3ed.almatarchallenge.data.SearchRepository
import com.youssefelsa3ed.almatarchallenge.ui.ViewModelFactory

object Injection {

    private fun provideSearchService(): SearchService {
        return SearchService()
    }

    private fun provideSearchHttpProvider(): SearchHttpProvider {
        return SearchHttpProvider()
    }

    private fun provideSearchRepository(): SearchRepository {
        return SearchRepository(provideSearchService(), provideSearchHttpProvider())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideSearchRepository())
    }
}