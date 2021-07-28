package com.youssefelsa3ed.almatarchallenge.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.youssefelsa3ed.almatarchallenge.databinding.DocListViewItemBinding
import com.youssefelsa3ed.almatarchallenge.model.Doc

class DocsAdapter(
    private val onClickListener: View.OnClickListener,
    diffCallback: DiffUtil.ItemCallback<Doc>,
) : PagingDataAdapter<Doc, DocsAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DocListViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.idTitle.text = item?.title
        holder.author.text = item?.authorName?.get(0)

        with(holder.itemView) {
            holder.itemView.tag = item
            setOnClickListener(onClickListener)
        }
    }

    inner class ViewHolder(binding: DocListViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idTitle: TextView = binding.idTitle
        val author: TextView = binding.author
    }
}