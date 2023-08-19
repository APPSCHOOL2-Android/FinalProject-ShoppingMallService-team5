package com.hifi.hifi_shopping.recommend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityRecommendBinding
import com.hifi.hifi_shopping.databinding.RecommendRecycItemBinding

class RecommendActivity : AppCompatActivity() {

    lateinit var activityRecommendBinding: ActivityRecommendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityRecommendBinding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(activityRecommendBinding.root)

        activityRecommendBinding.run{
            recommendActivityCategoryToolbar.run{
                title = "추천 리스트"
                setNavigationIcon(R.drawable.chevron_left_24px)
                inflateMenu(R.menu.toolbar_menu_basic)
                isTitleCentered = true
            }

            recommendCategoryRecyclerView.run{
                adapter = CategoryListAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    inner class CategoryListAdapter: RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {
        inner class CategoryListViewHolder(recommendRecycItemBinding: RecommendRecycItemBinding): ViewHolder(recommendRecycItemBinding.root){
            var categoryTextView = recommendRecycItemBinding.categoryTextView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
            var recommendRecycItemBinding = RecommendRecycItemBinding.inflate(layoutInflater)
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