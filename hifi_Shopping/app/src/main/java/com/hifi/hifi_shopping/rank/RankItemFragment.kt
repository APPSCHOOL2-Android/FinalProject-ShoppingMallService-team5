package com.hifi.hifi_shopping.rank

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.FragmentRankItemBinding
import com.hifi.hifi_shopping.rank.vm.RankItemViewModel

class RankItemFragment : Fragment() {

    lateinit var fragmentRankItemBinding: FragmentRankItemBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentRankItemBinding = FragmentRankItemBinding.inflate(inflater, container, false)
        recyclerView = fragmentRankItemBinding.recyclerViewRankItem

        // 랜덤한 제품 정보 가져오기
        val rankItemViewModel = ViewModelProvider(this).get(RankItemViewModel::class.java)
        rankItemViewModel.getRandomProducts()

        // 리사이클러뷰 설정
        itemAdapter = ItemAdapter(emptyList()) // 빈 리스트로 어댑터를 생성
        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        return fragmentRankItemBinding.root
    }

    // 화면이 재구성될 때 호출되는 함수
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 새로운 아이템 리스트 생성 (예시로 빈 리스트를 생성)
        val newItems: List<RankItemFragment.RankItem> = emptyList()

        // 아이템 어댑터의 updateItemList 함수를 호출하여 아이템 리스트를 업데이트
        itemAdapter.updateItemList(newItems)
    }

    data class RankItem(val imageResId: Int, val name: String)
}

// 아이템 랭킹 관련 리사이클러뷰 어댑터
class ItemAdapter(private var itemList: List<RankItemFragment.RankItem>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_rank_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemImage.setImageResource(currentItem.imageResId)
        holder.itemName.text = currentItem.name

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CategoryActivity::class.java)

            // 선택한 제품의 ID를 Intent에 추가
            // intent.putExtra("product_id", currentItem.productId)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.ImageItem)
        val itemName: TextView = itemView.findViewById(R.id.TextItem)
    }

    fun updateItemList(newItemList: List<RankItemFragment.RankItem>) {
        itemList = newItemList
        notifyDataSetChanged()
    }
}