package com.mmgpt.shoplistgpt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmgpt.shoplistgpt.adapter.ItemAdapter
import com.mmgpt.shoplistgpt.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    private val viewModel: ShoppingListViewModel by activityViewModels()
    private var categoryId: Long = -1L
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = requireArguments().getLong(ARG_CATEGORY_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ItemAdapter(onAction = { item, action ->
            when (action) {
                ItemAdapter.Action.CHECK -> viewModel.checkItem(item)
                ItemAdapter.Action.FLAG -> viewModel.flagItem(item)
                // EDIT and DELETE actions are not applicable in this fragment
                ItemAdapter.Action.EDIT, ItemAdapter.Action.DELETE -> {}
            }
        })
        binding.recyclerView.adapter = adapter

        viewModel.categories.observe(viewLifecycleOwner) { list ->
            val cat = viewModel.getCategoryById(categoryId)
            if (cat != null) {
                android.util.Log.d(
                    "ShopFragment",
                    "Fragment categoryId=$categoryId, " +
                            "Showing=${cat.heading}, " +
                            "Items=${cat.items.map { it.name }}"
                )
                binding.tvHeading.text = cat.heading
                binding.tvCount.text = "${cat.remainingCount}/${cat.totalCount}"
                adapter.submitList(cat.items)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val ARG_CATEGORY_ID = "category_id"

        fun newInstance(categoryId: Long): CategoryFragment {
            return CategoryFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_CATEGORY_ID, categoryId)
                }
            }
        }
    }
}

