package com.youssefelsa3ed.almatarchallenge.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.youssefelsa3ed.almatarchallenge.R
import com.youssefelsa3ed.almatarchallenge.databinding.DocLoadStateFooterViewItemBinding

class DocsLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<DocsLoadStateAdapter.DocsLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: DocsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): DocsLoadStateViewHolder {
        return DocsLoadStateViewHolder.create(parent, retry)
    }

    class DocsLoadStateViewHolder(
        private val binding: DocLoadStateFooterViewItemBinding,
        retry: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): DocsLoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.doc_load_state_footer_view_item, parent, false)
                val binding = DocLoadStateFooterViewItemBinding.bind(view)
                return DocsLoadStateViewHolder(binding, retry)
            }
        }
    }
}