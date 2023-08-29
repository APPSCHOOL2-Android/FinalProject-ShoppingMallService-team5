package com.hifi.hifi_shopping.category.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.auth.vm.AuthViewModel
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.category.CategoryViewModel
import com.hifi.hifi_shopping.category.ContentType
import com.hifi.hifi_shopping.databinding.FragmentCategoryMainBinding
import com.hifi.hifi_shopping.databinding.ItemCategoryCategoryDetailBinding
import com.hifi.hifi_shopping.databinding.ItemReviewCategoryDetailBinding
import com.hifi.hifi_shopping.notice.NoticeActivity
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.UserActivity

class CategoryMainFragment : Fragment() {

    lateinit var categoryActivity: CategoryActivity
    lateinit var binding: FragmentCategoryMainBinding

    lateinit var categoryMainViewModel: CategoryMainViewModel
    lateinit var categoryViewModel: CategoryViewModel
//    lateinit var authViewModel: AuthViewModel

    var categoryList: Array<String>? = null
    var categoryNum = 0

    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryActivity = activity as CategoryActivity
        binding = FragmentCategoryMainBinding.inflate(layoutInflater)

        categoryMainViewModel = ViewModelProvider(this)[CategoryMainViewModel::class.java]
        categoryViewModel = ViewModelProvider(categoryActivity)[CategoryViewModel::class.java]
//        authViewModel = ViewModelProvider(categoryActivity)[AuthViewModel::class.java]

//        authViewModel.run {
//            userData.observe(viewLifecycleOwner) {
//                Log.d("brudenell", "뷰모델 테스트")
//                categoryMainViewModel.currentUserIdx = it.idx
//            }
//        }

        val profileClickCallback: () -> Unit = {
            val intent = Intent(categoryActivity, UserActivity::class.java)
            startActivity(intent)
        }

        val productClickCallback: (String) -> Unit = { idx ->
            val intent = Intent(categoryActivity, BuyActivity::class.java)

            val buyProductList = ArrayList<String>()
            buyProductList.add(idx)

            intent.putStringArrayListExtra("buyProduct", buyProductList)

            startActivity(intent)
        }

        val productListAdapter = ProductListAdapter(categoryViewModel, categoryMainViewModel, productClickCallback)

        val reviewListAdapter = ReviewListAdapter(categoryViewModel, this, categoryMainViewModel, profileClickCallback, productClickCallback)

        categoryNum = arguments?.getInt("categoryNum") ?: 0

        var submitListFlag = false

        categoryMainViewModel.run {
            if (categoryNum == 0) {
                getProduct("")
                getReview("")
            } else {
                getProduct(categoryNum.toString())
                getReview(categoryNum.toString())
            }

            allProductList.observe(viewLifecycleOwner) {
//                getProductWithWorth()
                getProductWithWorthJustInfo()
            }

            productList.observe(viewLifecycleOwner) {
                if (!submitListFlag) {
                    Log.d("brudenell", "다시 그려")
                    productListAdapter.submitList(it)
                    submitListFlag = true
                }
            }

            reviewList.observe(viewLifecycleOwner) {
                reviewListAdapter.submitList(it)
            }

            updateSubscribeData.observe(viewLifecycleOwner) {
                reviewListAdapter.notifyDataSetChanged()
            }
        }

        if (categoryNum == 0) {
            categoryList = arrayOf(
                "준비", "관리", "취미", "연애"
            )
            categoryViewModel.setSearchSubCategory(false)
        } else if (categoryData.containsKey(categoryNum)) {
            categoryList = categoryData[categoryNum]
            categoryViewModel.setSearchSubCategory(true)
        } else {
            categoryList = emptyArray()
            categoryViewModel.setSearchSubCategory(true)
        }

        binding.run {
            materialToolbarCategoryMain.run {
                if (categoryNum == 0) {
                    setNavigationOnClickListener {
                        val intent = Intent(categoryActivity, NoticeActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    setNavigationIcon(R.drawable.chevron_left_24px)

                    title = arguments?.getString("categoryName")

                    setTitleTextAppearance(context, R.style.CategoryDetialToolbarTitleTextAppearance)

                    setTitleTextColor(categoryActivity.getColor(R.color.brown))

                    setNavigationOnClickListener {
                        findNavController().popBackStack()
                    }
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

            // 카테고리 리스트
            recyclerViewCategoryMainCategory.run {
                adapter = CategoryListAdapter()
                layoutManager = LinearLayoutManager(categoryActivity, LinearLayoutManager.HORIZONTAL, false)
            }

            // 제품 리스트
            recyclerViewCategoryMainProduct.run {
                adapter = productListAdapter
                layoutManager = LinearLayoutManager(categoryActivity)
            }

            fabCategoryMainWorthUp.setOnClickListener {
                categoryMainViewModel.run {
                    if (productCount > productWorth + 6) {
                        productWorth += 6
                    }
                    submitListFlag = false
//                    getProductWithWorth()
                    getProductWithWorthJustInfo()
                }
            }

            fabCategoryMainWorthDown.setOnClickListener {
                categoryMainViewModel.run {
                    if (productWorth > 0) {
                        productWorth -= 6
                    }
                    submitListFlag = false
//                    getProductWithWorth()
                    getProductWithWorthJustInfo()
                }
            }

            // 리뷰 리스트
            recyclerViewCategoryMainReview.run {
                adapter = reviewListAdapter
                layoutManager = LinearLayoutManager(categoryActivity)
                addItemDecoration(DividerItemDecoration(categoryActivity, DividerItemDecoration.VERTICAL))
            }

            // 스위치 처리
            categoryViewModel.run {
                showProductOrReview.observe(viewLifecycleOwner) { type ->
                    if (type == ContentType.PRODUCT) {
                        recyclerViewCategoryMainProduct.visibility = View.VISIBLE
                        recyclerViewCategoryMainReview.visibility = View.GONE
                        fabCategoryMainWorthUp.visibility = View.VISIBLE
                        fabCategoryMainWorthDown.visibility = View.VISIBLE
                        switchCategoryMainProductOrReview.text = "제품"
                        switchCategoryMainProductOrReview.isChecked = false
                    } else {
                        recyclerViewCategoryMainProduct.visibility = View.GONE
                        recyclerViewCategoryMainReview.visibility = View.VISIBLE
                        fabCategoryMainWorthUp.visibility = View.GONE
                        fabCategoryMainWorthDown.visibility = View.GONE
                        switchCategoryMainProductOrReview.text = "리뷰"
                        switchCategoryMainProductOrReview.isChecked = true
                    }
                }
            }

            switchCategoryMainProductOrReview.run {
                setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        categoryViewModel.setShowProductOrReview(ContentType.REVIEW)
                    } else {
                        categoryViewModel.setShowProductOrReview(ContentType.PRODUCT)
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
                        arg.putString("categoryName", categoryName)

                        findNavController().navigate(R.id.categoryMainFragment, arg, navOptions)
                    }
                }
            }
        }
    }
}