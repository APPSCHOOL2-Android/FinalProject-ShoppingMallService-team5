package com.hifi.hifi_shopping_sales.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping_sales.R
import com.hifi.hifi_shopping_sales.databinding.FragmentItemListBinding
import com.hifi.hifi_shopping_sales.databinding.RowItemBinding
import com.hifi.hifi_shopping_sales.utils.formatPrice
import com.hifi.hifi_shopping_sales.vm.ProductViewModel
import com.hifi.hifi_shopping_sales.vm.SellerViewModel


class ItemListFragment : Fragment() {

    lateinit var fragmentItemListBinding: FragmentItemListBinding
    lateinit var sellerActivity: SellerActivity
    lateinit var productViewModel: ProductViewModel
    lateinit var sellerViewModel: SellerViewModel
    val productItemList = mutableListOf<ProductRowItemClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentItemListBinding = FragmentItemListBinding.inflate(inflater)
        sellerActivity = activity as SellerActivity

        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        sellerViewModel = ViewModelProvider(sellerActivity)[SellerViewModel::class.java]
        productViewModel.run{
            productList.observe(viewLifecycleOwner){ itemList ->
                itemList.forEach {
                    val idx = it.value.idx
                    val name = it.value.name
                    val price = it.value.price
                    val newItem = ProductRowItemClass(idx, null, name, price,"1", "4")
                    productItemList.add(newItem)
                    productViewModel.getProductImgListByIdx(idx)
                    fragmentItemListBinding.itemRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
            loadImgBitmap.observe(sellerActivity){data ->
                productItemList.map{
                    if(it.idx == data.first){
                        it.img = data.second
                    }
                }
                fragmentItemListBinding.itemRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
        fragmentItemListBinding.run{
            itemListToolbar.run{
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

            productViewModel.getProductAll()
        }
        return fragmentItemListBinding.root
    }

    override fun onResume() {
        super.onResume()
        productItemList.clear()
    }

    inner class ItemRecyclerViewAdapter : RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder>() {
        inner class ItemViewHolder(rowItemBinding: RowItemBinding) : RecyclerView.ViewHolder(rowItemBinding.root){
            val productNameTextView:TextView
            val productPriceTextView:TextView
            val productImageView:ImageView

            init{
                productNameTextView = rowItemBinding.productNameTextView
                productPriceTextView = rowItemBinding.productPriceTextView
                productImageView = rowItemBinding.productImageView
            }
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
            return productItemList.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.productNameTextView.text = productItemList[position].name
            holder.productPriceTextView.text = formatPrice(productItemList[position].price)
            holder.productImageView.setImageBitmap(productItemList[position].img)
        }
    }
}