package com.youssefelsa3ed.almatarchallenge.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import com.youssefelsa3ed.almatarchallenge.R
import com.youssefelsa3ed.almatarchallenge.databinding.FragmentDocListBinding
import com.youssefelsa3ed.almatarchallenge.di.Injection
import com.youssefelsa3ed.almatarchallenge.model.Doc
import com.youssefelsa3ed.almatarchallenge.ui.adapters.DocsAdapter
import com.youssefelsa3ed.almatarchallenge.ui.adapters.DocsLoadStateAdapter
import com.youssefelsa3ed.almatarchallenge.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [DocListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocListFragment : Fragment() {
    private lateinit var viewModel: DocSearchViewModel
    private lateinit var binding: FragmentDocListBinding
    private lateinit var adapter: DocsAdapter
    private var searchJob: Job? = null
    private var queryKey: String? = null
    private var queryVal: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDocListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(DocSearchViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onClickListener = View.OnClickListener { itemView ->
            itemView.findNavController()
                .navigate(
                    R.id.detailsFragment,
                    DetailsFragmentArgs
                        .Builder(itemView.tag as Doc)
                        .build()
                        .toBundle()
                )
        }

        initAdapter(onClickListener)
        queryKey = viewModel.currentQueryKey
        queryVal = viewModel.currentQueryValue
        initSearch()
        search()
    }

    private fun initAdapter(
        onClickListener: View.OnClickListener
    ) {
        adapter = DocsAdapter(
            onClickListener,
            diffCallback = object : DiffUtil.ItemCallback<Doc>() {
                override fun areItemsTheSame(oldItem: Doc, newItem: Doc): Boolean =
                    // Doc key serves as unique ID
                    oldItem.key == newItem.key

                override fun areContentsTheSame(oldItem: Doc, newItem: Doc): Boolean =
                    oldItem == newItem
            }
        )
        binding.itemList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = DocsLoadStateAdapter { adapter.retry() },
            footer = DocsLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.itemList.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error
            // Show the retry state if initial load or refresh fails.
            binding.errorMsg.isVisible = loadState.source.refresh is LoadState.Error

            // Display any error, regardless of whether it came from SearchRepository or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
            errorState?.let {
                binding.errorMsg.text = getString(R.string.woops_error, it.error.localizedMessage)
            }

            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)
        }
        binding.retryButton.setOnClickListener { adapter.retry() }
    }


    private fun showEmptyList(
        show: Boolean
    ) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.itemList.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.itemList.visibility = View.VISIBLE
        }
    }

    private fun initSearch() {
        if (Utils.isObjectNull(queryVal)) {
            arguments?.let {
                queryKey = DocListFragmentArgs.fromBundle(it).searchKey
                queryVal = DocListFragmentArgs.fromBundle(it).searchVal
            }
        }
        binding.searchDocs.queryHint = when (queryKey) {
            "q" -> ""
            else -> queryKey
        }

        binding.searchDocs.setQuery(queryVal, true)

        binding.searchDocs.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                updateRepoListFromInput()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.searchDocs.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for SearchRepository changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.itemList.scrollToPosition(0) }
        }
    }

    private fun updateRepoListFromInput() {
        binding.searchDocs.query.trim().let {
            if (it.isNotEmpty()) {
                queryVal = it.toString()
                search()
            }
        }
    }

    private fun search() {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        if (queryVal.isNullOrEmpty()) return
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(queryKey ?: "q", queryVal ?: "").collectLatest {
                adapter.submitData(it)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param queryKey The search type {'q','title','author'}.
         * @param queryVal The search query.
         * @return A new instance of fragment [DocListFragment].
         */
        @JvmStatic
        fun newInstance(
            queryKey: String,
            queryVal: String
        ) =
            DocListFragment().apply {
                arguments = DocListFragmentArgs.Builder()
                    .setSearchKey(queryKey)
                    .setSearchVal(queryVal)
                    .build().toBundle()
            }
    }
}