package com.hifi.hifi_shopping.rank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentRankItemBinding

class RankItemFragment : Fragment() {

    lateinit var fragmentRankItemBinding: FragmentRankItemBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentRankItemBinding = FragmentRankItemBinding.inflate(inflater, container, false)
        recyclerView = fragmentRankItemBinding.recyclerViewRankItem

        // 더미 (시험용)
        val itemList = createDummyItemList()
        itemAdapter = ItemAdapter(itemList)

        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3) // 3 items per row
            adapter = itemAdapter
        }

        return fragmentRankItemBinding.root
    }

    private fun createDummyItemList(): List<RankItem> {
        val dummyList = mutableListOf<RankItem>()

        dummyList.add(RankItem(R.drawable.add_box_24px, "Item 1"))
        dummyList.add(RankItem(R.drawable.add_box_24px, "Item 2"))
        dummyList.add(RankItem(R.drawable.add_box_24px, "Item 3"))
        dummyList.add(RankItem(R.drawable.arrow_drop_down_24px, "Item 4"))
        dummyList.add(RankItem(R.drawable.arrow_drop_down_24px, "Item 5"))
        dummyList.add(RankItem(R.drawable.arrow_drop_down_24px, "Item 6"))
        dummyList.add(RankItem(R.drawable.call_24px, "Item 7"))
        dummyList.add(RankItem(R.drawable.call_24px, "Item 8"))
        dummyList.add(RankItem(R.drawable.call_24px, "Item 9"))
        dummyList.add(RankItem(R.drawable.favorite2_24px, "Item 10"))
        dummyList.add(RankItem(R.drawable.favorite2_24px, "Item 11"))
        dummyList.add(RankItem(R.drawable.favorite2_24px, "Item 12"))
        dummyList.add(RankItem(R.drawable.edit_square_24px, "Item 13"))
        dummyList.add(RankItem(R.drawable.edit_square_24px, "Item 14"))
        dummyList.add(RankItem(R.drawable.edit_square_24px, "Item 15"))
        return dummyList
    }

    data class RankItem(val imageResId: Int, val name: String)
}

class ItemAdapter(private val itemList: List<RankItemFragment.RankItem>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_rank_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemImage.setImageResource(currentItem.imageResId)
        holder.itemName.text = currentItem.name

        holder.itemView.setOnClickListener {
            // Handle item click
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.ImageItem)
        val itemName: TextView = itemView.findViewById(R.id.TextItem)
    }
}
