package com.hifi.hifi_shopping.recommend

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.ActivityRecommendBinding
import com.hifi.hifi_shopping.databinding.FragmentRecommendBinding
import com.hifi.hifi_shopping.databinding.RecommendCategoryRecycItemBinding
import com.hifi.hifi_shopping.databinding.RecommendReviewItemBinding
import com.hifi.hifi_shopping.user.UserActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecommendFragment : Fragment() {

    lateinit var fragmentRecommendBinding: FragmentRecommendBinding
    lateinit var categoryActivity: CategoryActivity

    var fromMyPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentRecommendBinding = FragmentRecommendBinding.inflate(layoutInflater)
        categoryActivity = activity as CategoryActivity

        fromMyPage = arguments?.getBoolean("fromMyPage") ?: false

        if (fromMyPage) {
            categoryActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        categoryActivity.navController.popBackStack()
                        categoryActivity.categoryViewModel.setNavControllerDestination(categoryActivity.navController.currentDestination?.id ?: R.id.categoryMainFragment)
                    }

                    if (fromMyPage) {
                        val intent = Intent(categoryActivity, UserActivity::class.java)
                        intent.putExtra("userEmail", categoryActivity.userDataClass.email)
                        intent.putExtra("userIdx", categoryActivity.userDataClass.idx)
                        intent.putExtra("userNickname", categoryActivity.userDataClass.nickname)
                        intent.putExtra("userPw", categoryActivity.userDataClass.pw)
                        intent.putExtra("userVerify", categoryActivity.userDataClass.verify)
                        intent.putExtra("userPhoneNum", categoryActivity.userDataClass.phoneNum)
                        intent.putExtra("userProfileImg", categoryActivity.userDataClass.profileImg)
                        intent.putExtra("whereFrom", "category")
                        startActivity(intent)
                    }
                }
            })
        }

        fragmentRecommendBinding.run {
            recommendActivityCategoryToolbar.run {
                setNavigationOnClickListener {
                    categoryActivity.onBackPressedDispatcher.onBackPressed()
                }

                title = "추천 리스트"
                setNavigationIcon(R.drawable.chevron_left_24px)
                inflateMenu(R.menu.toolbar_menu_basic)
                isTitleCentered = true
            }

            recommendCategoryRecyclerView.run{
                adapter = CategoryListAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

            recommendInfoRecyclerView.run{
                adapter = InfoListAdapter()
                layoutManager = LinearLayoutManager(context)
            }
        }

        return fragmentRecommendBinding.root
    }

    inner class InfoListAdapter: RecyclerView.Adapter<InfoListAdapter.InfoListViewHolder>(){
        inner class InfoListViewHolder(val recommendReviewItemBinding: RecommendReviewItemBinding): RecyclerView.ViewHolder(recommendReviewItemBinding.root){
            fun bind() {
                recommendReviewItemBinding.recommendUserProfileNickNametextView.text = "유저 닉네임$adapterPosition"
                recommendReviewItemBinding.recommendItemNametextView.text = "제품$adapterPosition"
                recommendReviewItemBinding.recommendUserProfileReviewContentsTextView.text = "최신리뷰$adapterPosition"
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoListViewHolder {
            var recommendReviewItemBinding = RecommendReviewItemBinding.inflate(layoutInflater)
            var infoListViewHolder = InfoListViewHolder(recommendReviewItemBinding)

            recommendReviewItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return infoListViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: InfoListViewHolder, position: Int) {
            holder.bind()
        }

    }

    inner class CategoryListAdapter: RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {
        inner class CategoryListViewHolder(recommendCategoryRecycItemBinding: RecommendCategoryRecycItemBinding): RecyclerView.ViewHolder(recommendCategoryRecycItemBinding.root){
            var categoryTextView = recommendCategoryRecycItemBinding.categoryTextView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
            var recommendRecycItemBinding = RecommendCategoryRecycItemBinding.inflate(layoutInflater)
            var categoryListViewHolder = CategoryListViewHolder(recommendRecycItemBinding)

            recommendRecycItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return categoryListViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
            holder.categoryTextView.text = "카테고리${position}"
        }
    }
}