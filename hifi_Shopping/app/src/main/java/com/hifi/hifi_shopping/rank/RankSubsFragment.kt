package com.hifi.hifi_shopping.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentRankSubsBinding

class RankSubsFragment : Fragment() {

    lateinit var fragmentRankSubsBinding: FragmentRankSubsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var subsAdapter: SubsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentRankSubsBinding = FragmentRankSubsBinding.inflate(inflater, container, false)
        recyclerView = fragmentRankSubsBinding.recyclerViewRankSubs

        // 더미 (시험용)(제품)
        val subsList = createDummySubsList()
        subsAdapter = SubsAdapter(subsList)

        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3) // 3 items per row
            adapter = subsAdapter
        }

        return fragmentRankSubsBinding.root
    }

    private fun createDummySubsList(): List<RankSubs> {
        val dummyList = mutableListOf<RankSubs>()

        dummyList.add(RankSubs(R.drawable.add_box_24px, "Subs 1"))
        dummyList.add(RankSubs(R.drawable.add_box_24px, "Subs 2"))
        dummyList.add(RankSubs(R.drawable.add_box_24px, "Subs 3"))
        dummyList.add(RankSubs(R.drawable.arrow_drop_down_24px, "Subs 4"))
        dummyList.add(RankSubs(R.drawable.arrow_drop_down_24px, "Subs 5"))
        dummyList.add(RankSubs(R.drawable.arrow_drop_down_24px, "Subs 6"))
        dummyList.add(RankSubs(R.drawable.call_24px, "Subs 7"))
        dummyList.add(RankSubs(R.drawable.call_24px, "Subs 8"))
        dummyList.add(RankSubs(R.drawable.call_24px, "Subs 9"))
        dummyList.add(RankSubs(R.drawable.favorite2_24px, "Subs 10"))
        dummyList.add(RankSubs(R.drawable.favorite2_24px, "Subs 11"))
        dummyList.add(RankSubs(R.drawable.favorite2_24px, "Subs 12"))
        dummyList.add(RankSubs(R.drawable.edit_square_24px, "Subs 13"))
        dummyList.add(RankSubs(R.drawable.edit_square_24px, "Subs 14"))
        dummyList.add(RankSubs(R.drawable.edit_square_24px, "Subs 15"))
        return dummyList
    }

    data class RankSubs(val imageResId: Int, val name: String)
}

// 구독 랭킹 관련 리사이클러뷰 어댑터
class SubsAdapter(private val subsList: List<RankSubsFragment.RankSubs>) : RecyclerView.Adapter<SubsAdapter.SubsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_rank_item, parent, false)
        return SubsViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: SubsViewHolder, position: Int) {
        val currentSubsItem = subsList[position]
        holder.subsImage.setImageResource(currentSubsItem.imageResId)
        holder.subsName.text = currentSubsItem.name

        holder.itemView.setOnClickListener {
            // Handle subs item click
        }
    }
    override fun getItemCount(): Int {
        return subsList.size
    }
    inner class SubsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subsImage: ImageView = itemView.findViewById(R.id.ImageItem)
        val subsName: TextView = itemView.findViewById(R.id.TextItem)
    }
}