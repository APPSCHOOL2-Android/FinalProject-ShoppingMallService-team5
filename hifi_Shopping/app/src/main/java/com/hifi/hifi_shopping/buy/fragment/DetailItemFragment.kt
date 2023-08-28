package com.hifi.hifi_shopping.buy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.buy.buy_repository.OrderItemRepository.Companion.getProductNormalReview
import com.hifi.hifi_shopping.buy.buy_vm.OrderItemViewModel
import com.hifi.hifi_shopping.buy.buy_vm.OrderUserViewModel
import com.hifi.hifi_shopping.buy.datamodel.ProductFAQData
import com.hifi.hifi_shopping.buy.datamodel.ProductNormalReview
import com.hifi.hifi_shopping.buy.datamodel.SubscribeUserInfo
import com.hifi.hifi_shopping.databinding.FragmentDetailItemBinding
import com.hifi.hifi_shopping.databinding.RowDetailReviewBinding
import com.hifi.hifi_shopping.databinding.SubscribeUserListItemBinding

private lateinit var orderUserViewModel: OrderUserViewModel
private lateinit var orderItemViewModel: OrderItemViewModel
class DetailItemFragment : Fragment() {

    lateinit var fragmenDetailItemtBinding: FragmentDetailItemBinding
    lateinit var buyActivity: BuyActivity
    private var orderUserIdx = ""
    private var productIdx = ""
    var subscribeUserMap = LinkedHashMap<String, SubscribeUserInfo>()
    var subReviewUserIdxList = listOf<String>()

    var normalReviewKey = listOf<String>()
    var productNormalReviewMap = HashMap<String, ProductNormalReview>()

    var productFAQKey = listOf<String>()
    var productFAQMap = HashMap<String, ProductFAQData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        dataSetting()
        viewSetting()
        viewModelSetting()

        return fragmenDetailItemtBinding.root
    }

    private fun dataSetting(){
        fragmenDetailItemtBinding = FragmentDetailItemBinding.inflate(layoutInflater)
        buyActivity = activity as BuyActivity
        productIdx = arguments?.getString("selProduct")!!
        orderUserIdx = arguments?.getString("userIdx")!!
    }

    private fun viewSetting(){
        fragmenDetailItemtBinding.run{
            subscribeUserListRecyclerView.run{
                adapter = SubscribeUserListAdapter()
                layoutManager = LinearLayoutManager(buyActivity, LinearLayoutManager.HORIZONTAL, false)
            }

            nomalReviewRecyclerView.run{
                adapter = NomalReviewAdapter()
                layoutManager = LinearLayoutManager(buyActivity)
            }

            faqListRecyclerView.run{
                adapter = FAQListAdapter()
                layoutManager = LinearLayoutManager(buyActivity)
            }

            buyRightBtn.run{
                setOnClickListener {
                    val bundle = Bundle()
                    var productList = ArrayList<String>()
                    productList.add(productIdx)
                    bundle.putStringArrayList("selProduct", productList)
                    bundle.putString("userIdx", orderUserIdx) // 유저 인덱스
                    buyActivity.replaceFragment(BuyActivity.ORDER_FRAGMENT, false, bundle)
                }
            }

            tab1TextView.run{
                setOnClickListener {
                    itemInfoLine.setBackgroundColor(buyActivity.getColor(R.color.brown))
                    reviewInfoLine.setBackgroundColor(buyActivity.getColor(R.color.white))
                    faqInfoLine.setBackgroundColor(buyActivity.getColor(R.color.white))

                    productInfoContainer.visibility = View.VISIBLE
                    reviewContainer.visibility = View.GONE
                    FAQContainer.visibility = View.GONE
                }
            }

            tab2TextView.run{
                setOnClickListener {
                    itemInfoLine.setBackgroundColor(buyActivity.getColor(R.color.white))
                    reviewInfoLine.setBackgroundColor(buyActivity.getColor(R.color.brown))
                    faqInfoLine.setBackgroundColor(buyActivity.getColor(R.color.white))

                    productInfoContainer.visibility = View.GONE
                    reviewContainer.visibility = View.VISIBLE
                    FAQContainer.visibility = View.GONE
                }
            }

            tab3TextView.run{
                setOnClickListener {
                    itemInfoLine.setBackgroundColor(buyActivity.getColor(R.color.white))
                    reviewInfoLine.setBackgroundColor(buyActivity.getColor(R.color.white))
                    faqInfoLine.setBackgroundColor(buyActivity.getColor(R.color.brown))

                    productInfoContainer.visibility = View.GONE
                    reviewContainer.visibility = View.GONE
                    FAQContainer.visibility = View.VISIBLE
                }
            }

        }
    }
    private fun viewModelSetting(){
        orderItemViewModel = ViewModelProvider(buyActivity)[OrderItemViewModel::class.java]
        orderUserViewModel = ViewModelProvider(buyActivity)[OrderUserViewModel::class.java]

        orderItemViewModel.run{
            productMap.observe(buyActivity){
                if(it[productIdx] != null){
                    if(it[productIdx]?.img != null){
                        fragmenDetailItemtBinding.productTitleImg.setImageBitmap(it[productIdx]?.img)
                    }
                    fragmenDetailItemtBinding.productTitleName.text = it[productIdx]!!.name
                    fragmenDetailItemtBinding.productDetailTextView.text = it[productIdx]!!.context
                }
            }

            productNoTitleImgMap.observe(buyActivity){
                for(i in 1 .. 3){
                    if(it.containsKey("$i")){
                        val imgView = ImageView(buyActivity)
                        imgView.setImageBitmap(it["$i"])
                        var layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        imgView.layoutParams = layoutParams
                        imgView.scaleType = ImageView.ScaleType.FIT_XY
                        fragmenDetailItemtBinding.productInfoImageLayout.addView(imgView)
                    }
                }
            }

            normalReviewMap.observe(buyActivity){
                normalReviewKey = it.keys.toList()
                productNormalReviewMap = it
                if(normalReviewKey.isEmpty()){
                    fragmenDetailItemtBinding.normalReviewCount.text = "처음으로 리뷰를 달아보세요."
                } else {
                    fragmenDetailItemtBinding.normalReviewCount.text = "${normalReviewKey.size}개의 리뷰"
                }
                fragmenDetailItemtBinding.nomalReviewRecyclerView.adapter?.notifyDataSetChanged()
            }

            productFAQ.observe(buyActivity){
                productFAQKey = it.keys.toList()
                productFAQMap = it
                fragmenDetailItemtBinding.faqCountTextView.text = "${productFAQKey.size}개의 문의"
                fragmenDetailItemtBinding.faqListRecyclerView.adapter?.notifyDataSetChanged()
            }

        }

        orderUserViewModel.run{
            subscribeUserInfoMap.observe(buyActivity){
                subscribeUserMap = it
                subReviewUserIdxList = it.keys.toList()

                if(subscribeUserMap[subReviewUserIdxList[0]]!!.review != null){
                    fragmenDetailItemtBinding.constraint2.visibility = View.VISIBLE
                    fragmenDetailItemtBinding.subscrobeReviewUserNickname.text = subscribeUserMap[subReviewUserIdxList[0]]!!.nickname
                    fragmenDetailItemtBinding.subscrobeUserReviewContext.text = subscribeUserMap[subReviewUserIdxList[0]]!!.review
                    fragmenDetailItemtBinding.subscribeUserListRecyclerView.adapter?.notifyDataSetChanged()
                } else {
                    fragmenDetailItemtBinding.constraint2.visibility = View.GONE
                }
            }

            subUserIdxList.observe(buyActivity){
                it.forEach {
                    getOrderUserSubUserInfo(it, productIdx)
                }
            }
        }

        orderItemViewModel.getOrderProductData(productIdx)
        orderUserViewModel.getOrderUserSubUser(orderUserIdx)
        orderItemViewModel.getProductNormalReview(productIdx)
        orderItemViewModel.getProductFAQ(productIdx)

    }

    inner class FAQListAdapter(): RecyclerView.Adapter<FAQListAdapter.FAQListViewHolder>(){
        inner class FAQListViewHolder(rowDetailReviewBinding: RowDetailReviewBinding): ViewHolder(rowDetailReviewBinding.root){
            val rowDetailReviewTextViewName = rowDetailReviewBinding.rowDetailReviewTextViewName
            val rowDetailReviewTextViewContext = rowDetailReviewBinding.rowDetailReviewTextViewContext
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQListViewHolder {
            val rowDetailReviewBinding = RowDetailReviewBinding.inflate(layoutInflater)
            val nomalReviewViewHolder = FAQListViewHolder(rowDetailReviewBinding)

            rowDetailReviewBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return nomalReviewViewHolder
        }

        override fun getItemCount(): Int {
            return productFAQKey.size
        }

        override fun onBindViewHolder(holder: FAQListViewHolder, position: Int) {
            if(productFAQMap[productFAQKey[position]]?.nickname != null ){
                holder.rowDetailReviewTextViewName.text = productFAQMap[productFAQKey[position]]?.nickname
            }
            holder.rowDetailReviewTextViewContext.text = productFAQMap[productFAQKey[position]]?.context
        }

    }

    inner class NomalReviewAdapter(): RecyclerView.Adapter<NomalReviewAdapter.NomalReviewViewHolder>(){
        inner class NomalReviewViewHolder(rowDetailReviewBinding: RowDetailReviewBinding): ViewHolder(rowDetailReviewBinding.root){
            val rowDetailReviewTextViewName = rowDetailReviewBinding.rowDetailReviewTextViewName
            val rowDetailReviewTextViewContext = rowDetailReviewBinding.rowDetailReviewTextViewContext
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NomalReviewViewHolder {
            val rowDetailReviewBinding = RowDetailReviewBinding.inflate(layoutInflater)
            val nomalReviewViewHolder = NomalReviewViewHolder(rowDetailReviewBinding)

            rowDetailReviewBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return nomalReviewViewHolder
        }

        override fun getItemCount(): Int {
            return normalReviewKey.size
        }

        override fun onBindViewHolder(holder: NomalReviewViewHolder, position: Int) {
            if(productNormalReviewMap[normalReviewKey[position]]?.nickname != null ){
                holder.rowDetailReviewTextViewName.text = productNormalReviewMap[normalReviewKey[position]]?.nickname
            }
            holder.rowDetailReviewTextViewContext.text = productNormalReviewMap[normalReviewKey[position]]?.review
        }
    }

    inner class SubscribeUserListAdapter(): RecyclerView.Adapter<SubscribeUserListAdapter.SubscribeUserListViewHolder>(){
        inner class SubscribeUserListViewHolder(subscribeUserListItemBinding: SubscribeUserListItemBinding): ViewHolder(subscribeUserListItemBinding.root){
            var subscrobeUserProfileImageView = subscribeUserListItemBinding.subscrobeUserProfileImageView
            var subscrobeUserProfileNickNametextView = subscribeUserListItemBinding.subscrobeUserProfileNickNametextView

            init{
                subscribeUserListItemBinding.run{
                    subscrobeUserProfileImageView.setOnClickListener {
                        val key = subReviewUserIdxList[adapterPosition]
                        fragmenDetailItemtBinding.subscrobeUserReviewContext.text = subscribeUserMap[key]!!.review
                        fragmenDetailItemtBinding.subscrobeReviewUserNickname.text = subscribeUserMap[key]!!.nickname
                    }
                    subscrobeUserProfileNickNametextView.setOnClickListener {
                        val key = subReviewUserIdxList[adapterPosition]
                        fragmenDetailItemtBinding.subscrobeUserReviewContext.text = subscribeUserMap[key]!!.review
                        fragmenDetailItemtBinding.subscrobeReviewUserNickname.text = subscribeUserMap[key]!!.nickname
                    }
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SubscribeUserListViewHolder {
            val subscribeUserListItemBinding = SubscribeUserListItemBinding.inflate(layoutInflater)
            val viewHolder = SubscribeUserListViewHolder(subscribeUserListItemBinding)

            subscribeUserListItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return viewHolder
        }

        override fun getItemCount(): Int {
            return subReviewUserIdxList.size
        }

        override fun onBindViewHolder(holder: SubscribeUserListViewHolder, position: Int) {
            val key = subReviewUserIdxList[position]

            holder.subscrobeUserProfileNickNametextView.text = subscribeUserMap[key]!!.nickname
            if(subscribeUserMap[key]!!.profileImg != null){
                holder.subscrobeUserProfileImageView.setImageBitmap(subscribeUserMap[key]!!.profileImg)
            }

        }
    }

}