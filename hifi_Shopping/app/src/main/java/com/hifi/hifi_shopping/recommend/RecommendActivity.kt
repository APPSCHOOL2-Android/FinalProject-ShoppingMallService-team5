package com.hifi.hifi_shopping.recommend

import android.icu.text.IDNA.Info
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityRecommendBinding
import com.hifi.hifi_shopping.databinding.RecommendCategoryRecycItemBinding
import com.hifi.hifi_shopping.databinding.RecommendReviewItemBinding


class RecommendActivity : AppCompatActivity() {

    lateinit var activityRecommendBinding: ActivityRecommendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val test = 100

        activityRecommendBinding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(activityRecommendBinding.root)

        activityRecommendBinding.run{
//            recommendActivityCategoryToolbar.run{
//                title = "추천 리스트"
//                setNavigationIcon(R.drawable.chevron_left_24px)
//                inflateMenu(R.menu.toolbar_menu_basic)
//                isTitleCentered = true
//            }
//
//            recommendCategoryRecyclerView.run{
//                adapter = CategoryListAdapter()
//                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            }
//
//            recommendInfoRecyclerView.run{
//                adapter = InfoListAdapter()
//                layoutManager = LinearLayoutManager(context)
//            }

        }
    }

    inner class InfoListAdapter: RecyclerView.Adapter<InfoListAdapter.InfoListViewHolder>(){
        inner class InfoListViewHolder(recommendReviewItemBinding: RecommendReviewItemBinding): ViewHolder(recommendReviewItemBinding.root){

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

        }

    }

    inner class CategoryListAdapter: RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {
        inner class CategoryListViewHolder(recommendCategoryRecycItemBinding: RecommendCategoryRecycItemBinding): ViewHolder(recommendCategoryRecycItemBinding.root){
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
            holder.categoryTextView.text = "${position}카테고리"
        }
    }

}