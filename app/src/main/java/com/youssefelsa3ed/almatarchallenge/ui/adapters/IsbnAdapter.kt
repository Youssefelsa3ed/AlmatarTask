package com.youssefelsa3ed.almatarchallenge.ui.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.youssefelsa3ed.almatarchallenge.Utils
import com.youssefelsa3ed.almatarchallenge.databinding.IsbnViewItemBinding
import com.youssefelsa3ed.almatarchallenge.model.Isbn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class IsbnAdapter(
    private val values: List<Isbn>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            IsbnViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(values[position]) {
            if (errorMsg.isNullOrEmpty() && Utils.isObjectNull(bitmap)) {
                CoroutineScope(Dispatchers.Main).launch {
                    run {
                        withContext(Dispatchers.IO) {
                            val response: Pair<String?, InputStream?> =
                                Utils.getImageStreamFromUrl(url)
                            errorMsg = response.first
                            bitmap = BitmapFactory.decodeStream(response.second)
                        }
                        notifyItemChanged(position)
                    }
                }
            } else {
                val viewHolder: ViewHolder = holder as ViewHolder
                viewHolder.imageView.setImageBitmap(bitmap)
                viewHolder.progressBar.visibility = View.GONE
                viewHolder.errorMsg.visibility =
                    if (Utils.isObjectNull(bitmap)) View.VISIBLE else View.GONE
                viewHolder.errorMsg.text = errorMsg
            }
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(binding: IsbnViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.image
        val errorMsg: TextView = binding.error
        val progressBar: ProgressBar = binding.progressBar
    }
}