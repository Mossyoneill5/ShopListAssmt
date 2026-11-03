package com.mmgpt.shoplistgpt

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.mmgpt.shoplistgpt.PlanCategoryFragment
import com.mmgpt.shoplistgpt.PlanCategoriesFragment

class CategoryPagerAdapter(
    activity: FragmentActivity,
    private val viewModel: ShoppingListViewModel,
    private val planMode: Boolean = false
) : FragmentStateAdapter(activity) {


    override fun getItemId(position: Int): Long {
        val categories = viewModel.categories.value.orEmpty()
        val categoryIndex = if (planMode) position - 1 else position

        return if (categoryIndex in categories.indices) {
            categories[categoryIndex].id + 1L
        } else {
            0L
        }
    }

    override fun containsItem(itemId: Long): Boolean {
        if (itemId == 0L) return true

        return viewModel.categories.value.orEmpty().any {
            it.id + 1L == itemId
        }
    }

    override fun getItemCount(): Int =
        (viewModel.categories.value?.size ?: 0) + 1

    override fun createFragment(position: Int): Fragment =
        if (planMode) {
            if (position == 0) {
                PlanCategoriesFragment()
            } else {
                PlanCategoryFragment.newInstance(
                    viewModel.categories.value.orEmpty()[position - 1].id
                )
            }
        } else {
            val size = viewModel.categories.value?.size ?: 0
            if (position == size) {
                ShopCategoriesFragment()
            } else {
                CategoryFragment.newInstance(
                    viewModel.categories.value.orEmpty()[position].id
                )
            }
        }
}
