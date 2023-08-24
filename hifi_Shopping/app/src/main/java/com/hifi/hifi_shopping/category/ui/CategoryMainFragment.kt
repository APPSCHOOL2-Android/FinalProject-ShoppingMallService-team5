package com.hifi.hifi_shopping.category.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.category.CategoryViewModel
import com.hifi.hifi_shopping.databinding.FragmentCategoryMainBinding
import com.hifi.hifi_shopping.databinding.ItemCategoryCategoryDetailBinding
import com.hifi.hifi_shopping.databinding.ItemProductCategoryDetailBinding
import com.hifi.hifi_shopping.databinding.ItemReviewCategoryDetailBinding
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.UserActivity

class CategoryMainFragment : Fragment() {

    lateinit var categoryActivity: CategoryActivity
    lateinit var binding: FragmentCategoryMainBinding

    lateinit var categoryViewModel: CategoryViewModel

    var worth = 3

    var categoryList: Array<String>? = null
    var categoryNum = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryActivity = activity as CategoryActivity
        binding = FragmentCategoryMainBinding.inflate(layoutInflater)

        categoryNum = arguments?.getInt("categoryNum") ?: 0

        binding.run {
            materialToolbarCategoryMain.run {
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }

                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menuItemSearch -> {
                            val intent = Intent(categoryActivity, SearchActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.menuItemCart -> {
                            val intent = Intent(categoryActivity, UserActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    true
                }
            }

            if (categoryNum == 0) {
                categoryList = arrayOf(
                    "준비", "관리", "취미", "연애"
                )
            } else if (categoryData.containsKey(categoryNum)) {
                categoryList = categoryData[categoryNum]
            } else {
                categoryList = emptyArray()
            }

            recyclerViewCategoryMainCategory.run {
                adapter = CategoryListAdapter()
                layoutManager = LinearLayoutManager(categoryActivity, LinearLayoutManager.HORIZONTAL, false)
            }

            recyclerViewCategoryMainProduct.run {
                adapter = ProductListAdapter()
                layoutManager = LinearLayoutManager(categoryActivity)
            }

            recyclerViewCategoryMainReview.run {
                adapter = ReviewListAdapter()
                layoutManager = LinearLayoutManager(categoryActivity)
                addItemDecoration(DividerItemDecoration(categoryActivity, DividerItemDecoration.VERTICAL))
            }

            fabCategoryMainWorthUp.setOnClickListener {
                worth += 1
                recyclerViewCategoryMainProduct.adapter?.notifyDataSetChanged()
            }

            fabCategoryMainWorthDown.setOnClickListener {
                worth -= 1
                recyclerViewCategoryMainProduct.adapter?.notifyDataSetChanged()
            }

            switchCategoryMainProductOrReview.run {
                setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        recyclerViewCategoryMainProduct.visibility = View.GONE
                        recyclerViewCategoryMainReview.visibility = View.VISIBLE
                        fabCategoryMainWorthUp.visibility = View.GONE
                        fabCategoryMainWorthDown.visibility = View.GONE
                        text = "리뷰"
                    } else {
                        recyclerViewCategoryMainProduct.visibility = View.VISIBLE
                        recyclerViewCategoryMainReview.visibility = View.GONE
                        fabCategoryMainWorthUp.visibility = View.VISIBLE
                        fabCategoryMainWorthDown.visibility = View.VISIBLE
                        text = "제품"
                    }
                }
            }
        }

        return binding.root
    }

    inner class CategoryListAdapter: RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
            return CategoryListViewHolder(
                ItemCategoryCategoryDetailBinding.inflate(layoutInflater)
            )
        }

        override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
            holder.bind(categoryList?.get(position) ?: "")
        }

        override fun getItemCount(): Int {
            return categoryList?.size ?: 0
        }

        inner class CategoryListViewHolder(
            val itemCategoryCategoryDetailBinding: ItemCategoryCategoryDetailBinding
        ): RecyclerView.ViewHolder(itemCategoryCategoryDetailBinding.root) {
            fun bind(categoryName: String) {
                itemCategoryCategoryDetailBinding.run {
                    textViewItemCategoryCategoryName.text = categoryName

                    root.setOnClickListener {
                        val arg = Bundle()
                        arg.putInt("categoryNum", categoryNum * 10 + adapterPosition + 1)
                        findNavController().navigate(R.id.categoryMainFragment, arg)
                    }
                }
            }
        }
    }

    inner class ReviewListAdapter: RecyclerView.Adapter<ReviewListAdapter.ReviewListViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListViewHolder {
            val itemReviewCategoryDetailBinding = ItemReviewCategoryDetailBinding.inflate(layoutInflater)

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            itemReviewCategoryDetailBinding.root.layoutParams = params

            return ReviewListViewHolder(itemReviewCategoryDetailBinding)
        }

        override fun onBindViewHolder(holder: ReviewListViewHolder, position: Int) {
            holder.bind()
        }

        override fun getItemCount(): Int {
            return 10
        }

        inner class ReviewListViewHolder(
            val itemReviewCategoryDetailBinding: ItemReviewCategoryDetailBinding
        ): RecyclerView.ViewHolder(itemReviewCategoryDetailBinding.root) {
            fun bind() {
                itemReviewCategoryDetailBinding.run {
                    Glide.with(imageViewItemReviewCategoryDetailUserThumb)
                        .load("https://picsum.photos/${60 + adapterPosition}/${60 + adapterPosition}")
                        .into(imageViewItemReviewCategoryDetailUserThumb)

                    textViewItemReviewCategoryDetailUserName.text = "자취 만렙$adapterPosition"

                    Glide.with(imageViewItemReviewCategoryDetailProductThumb)
                        .load("https://picsum.photos/${100 + adapterPosition}/${100 + adapterPosition}")
                        .into(imageViewItemReviewCategoryDetailProductThumb)

                    textViewItemReviewCategoryDetailProductName.text = "제품명$adapterPosition"

                    textViewItemReviewCategoryDetailReviewContent.text = "최신 리뷰$adapterPosition"
                }
            }
        }
    }

    inner class ProductListAdapter: RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
            val itemProductCategoryDetailBinding = ItemProductCategoryDetailBinding.inflate(layoutInflater)

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            itemProductCategoryDetailBinding.root.layoutParams = params

            return ProductListViewHolder(itemProductCategoryDetailBinding)
        }

        override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
            holder.bind()
        }

        override fun getItemCount(): Int {
            return 6
        }

        inner class ProductListViewHolder(
            val itemProductCategoryDetailBinding: ItemProductCategoryDetailBinding
        ): RecyclerView.ViewHolder(itemProductCategoryDetailBinding.root){
            fun bind() {
                itemProductCategoryDetailBinding.run {
                    Glide.with(imageViewItemProductCategoryDetailThumb)
                        .load("https://picsum.photos/${100 + worth * 6 + adapterPosition}/${100 + worth * 6 + adapterPosition}")
                        .into(imageViewItemProductCategoryDetailThumb)

                    textViewItemProductCategoryDetailName.text = "제품명${worth * 6 + adapterPosition}"
                }
            }
        }
    }
}