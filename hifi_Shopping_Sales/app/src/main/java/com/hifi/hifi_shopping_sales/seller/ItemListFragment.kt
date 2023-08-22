package com.hifi.hifi_shopping_sales.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.hifi.hifi_shopping_sales.R
import com.hifi.hifi_shopping_sales.databinding.FragmentItemListBinding
import com.hifi.hifi_shopping_sales.databinding.RowItemBinding


class ItemListFragment : Fragment() {

    lateinit var fragmentItemListBinding: FragmentItemListBinding
    lateinit var sellerActivity: SellerActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentItemListBinding = FragmentItemListBinding.inflate(inflater)
        sellerActivity = activity as SellerActivity
        fragmentItemListBinding.run{
            itemListToolbar.run{
                title = "   상품 확인"
                inflateMenu(R.menu.menu_item_list)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.item_add -> {
                            sellerActivity.replaceFragment(SellerActivity.ADD_ITEM_FRAGMENT, true, null)
                        }
                    }
                    true
                }
            }
            itemRecyclerView.run{
                adapter = ItemRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
                // addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
            }
        }
        return fragmentItemListBinding.root
    }

    inner class ItemRecyclerViewAdapter : RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder>() {
        inner class ItemViewHolder(rowItemBinding: RowItemBinding) : RecyclerView.ViewHolder(rowItemBinding.root){

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val rowItemBinding = RowItemBinding.inflate(layoutInflater)
            val itemViewHolder = ItemViewHolder(rowItemBinding)

            rowItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return itemViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        }
    }
}