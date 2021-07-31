package com.youssefelsa3ed.almatarchallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.youssefelsa3ed.almatarchallenge.R
import com.youssefelsa3ed.almatarchallenge.databinding.FragmentDetailsBinding
import com.youssefelsa3ed.almatarchallenge.model.Doc
import com.youssefelsa3ed.almatarchallenge.model.Isbn
import com.youssefelsa3ed.almatarchallenge.ui.adapters.IsbnAdapter
import com.youssefelsa3ed.almatarchallenge.utils.URLS
import kotlin.math.min

private const val DOC_ITEM = "doc_item"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private var doc: Doc? = null

    private lateinit var adapter: IsbnAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doc = it.getParcelable(DOC_ITEM)
            adapter = IsbnAdapter(
                if (doc?.isbn.isNullOrEmpty())
                    emptyList()
                else doc?.isbn!!
                    .filter { url ->
                        !url.isNullOrEmpty()
                    }.map { url ->
                        Isbn(getIsbnImageUrl(url ?: ""), null)
                    }.let { list ->
                        list.slice(0 until min(5, list.size))
                    }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        doc?.let {
            binding.author.text = it.authorName?.get(0)
            binding.title.text = it.title
        }

        binding.author.setOnClickListener {
            it.findNavController().navigate(
                R.id.docListFragment,
                DocListFragmentArgs.Builder()
                    .setSearchKey("author")
                    .setSearchVal(doc?.authorName?.get(0) ?: "")
                    .build().toBundle()
            )
        }

        binding.title.setOnClickListener {
            it.findNavController().navigate(
                R.id.docListFragment,
                DocListFragmentArgs.Builder()
                    .setSearchKey("title")
                    .setSearchVal(doc?.title ?: "")
                    .build().toBundle()
            )
        }

        binding.rvIsbns.adapter = adapter
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param doc The selected document item.
         * @return A new instance of fragment DetailsFragment.
         */
        @JvmStatic
        fun newInstance(doc: Doc) =
            DetailsFragment().apply {
                arguments = bundleOf(DOC_ITEM to doc)
            }
    }

    /***
     *
     * Get the full url of an isbn
     *
     * @param isbn The isbn of a document.
     * @param imageQuality The required quality of the image.
     *
     * @return true if the object is null, false otherwise.
     */
    private fun getIsbnImageUrl(
        isbn: String,
        imageQuality: String = "M"
    ): String {
        return "${URLS.ImageUrl.value}$isbn-$imageQuality.jpg?default=false"
    }
}