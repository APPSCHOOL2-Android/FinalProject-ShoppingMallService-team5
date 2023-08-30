package com.hifi.hifi_shopping.parcel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityParcelBinding
import com.hifi.hifi_shopping.databinding.ParcelRecycItemBinding
import com.hifi.hifi_shopping.parcel.repository.ParcelRepository
import com.hifi.hifi_shopping.parcel.vm.ParcelViewModel
import com.hifi.hifi_shopping.review.ReviewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class ParcelActivity : AppCompatActivity() {

    lateinit var activityParcelBinding: ActivityParcelBinding
    lateinit var parcelViewModel : ParcelViewModel
    var packingParcelDataList = mutableListOf<RowParcelItemClass>()
    var shippingParcelDataList = mutableListOf<RowParcelItemClass>()
    var arrivingParcelDataList = mutableListOf<RowParcelItemClass>()
    var rowParcelList = mutableListOf<RowParcelItemClass>()
    var userIdx = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityParcelBinding = ActivityParcelBinding.inflate(layoutInflater)
        setContentView(activityParcelBinding.root)

        val receivedIntent = intent
        if (receivedIntent != null && receivedIntent.hasExtra("userIdx")) {
            userIdx = receivedIntent.getStringExtra("userIdx")!!
        }

        parcelViewModel = ViewModelProvider(this)[ParcelViewModel::class.java]
        parcelViewModel.run{
            packingParcelList.observe(this@ParcelActivity){
                packingParcelDataList = it
                rowParcelList = packingParcelDataList
                activityParcelBinding.parcelItemRecycView.adapter?.notifyDataSetChanged()
            }
            shippingParcelList.observe(this@ParcelActivity){
                shippingParcelDataList = it
                activityParcelBinding.parcelItemRecycView.adapter?.notifyDataSetChanged()
            }
            arrivingParcelList.observe(this@ParcelActivity){
                arrivingParcelDataList = it
                activityParcelBinding.parcelItemRecycView.adapter?.notifyDataSetChanged()
            }
        }
        activityParcelBinding.run{
            parcelViewModel.getParcelByUserIdx(userIdx)
            parcelMaterialToolbar.run{
                title = "배송 관리"
                setNavigationIcon(R.drawable.chevron_left_24px)
                setNavigationOnClickListener {
                    finish()
                }
                inflateMenu(R.menu.toolbar_menu_basic)
                isTitleCentered = true
            }
            packingCardView.setOnClickListener{
                rowParcelList = packingParcelDataList
                parcelItemRecycView.adapter?.notifyDataSetChanged()
            }

            shippingCardView.setOnClickListener{
                rowParcelList = shippingParcelDataList
                parcelItemRecycView.adapter?.notifyDataSetChanged()
            }

            arrivingCardView.setOnClickListener{
                rowParcelList = arrivingParcelDataList
                parcelItemRecycView.adapter?.notifyDataSetChanged()
            }

            recallRuleVisibleButton.run{
                setOnClickListener {
                    recallRuleTextView.text = """환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책
                        | 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책
                        |  환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책
                        |   환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책 환불 정책
                    """.trimMargin()
                    recallRuleInvisibleButton.visibility = View.VISIBLE
                    recallRuleTextView.visibility = View.VISIBLE
                    visibility = View.GONE
                }
            }
            recallRuleInvisibleButton.run{
                setOnClickListener {
                    recallRuleTextView.text = ""
                    visibility = View.GONE
                    recallRuleTextView.visibility = View.GONE
                    recallRuleVisibleButton.visibility = View.VISIBLE
                }
            }

            parcelItemRecycView.run{
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    inner class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
        inner class ViewHolder(parcelRecycItemBinding: ParcelRecycItemBinding) : RecyclerView.ViewHolder(parcelRecycItemBinding.root) {
            var parcelItemImageView = parcelRecycItemBinding.parcelItemImageView
            var parcelItemStatusTextView = parcelRecycItemBinding.parcelItemStatustextView
            var parcelItemNameTextView = parcelRecycItemBinding.parcelItemNametextView
            var parcelItemPriceTextView = parcelRecycItemBinding.parcelItemPriceTextView
            var parcelOderChangeButton = parcelRecycItemBinding.parcelOderChangeButton
            var parcelOderCancelButton = parcelRecycItemBinding.parcelOderCancelButton

            init {
                parcelRecycItemBinding.root.setOnClickListener {
                    val intent = Intent(this@ParcelActivity, ReviewActivity::class.java)
                    intent.putExtra("userIdx", userIdx)
                    intent.putExtra("productIdx", rowParcelList[adapterPosition].productIdx)
                    startActivity(intent)
                }
            }

            fun bindItem(item: RowParcelItemClass) {
                // UI 업데이트 등 필요한 작업 수행
                parcelItemNameTextView.text = rowParcelList[adapterPosition].productName
                parcelItemStatusTextView.text = rowParcelList[adapterPosition].parcelStatus
                parcelItemPriceTextView.text = rowParcelList[adapterPosition].productPrice
                parcelItemImageView.setImageResource(R.drawable.empty_photo)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val productInfo = ParcelRepository.getProductInfoByIdx(rowParcelList[adapterPosition].productIdx)
                        parcelItemNameTextView.text = productInfo!!.child("name").value as String
                        parcelItemPriceTextView.text = productInfo!!.child("price").value as String
                    }catch (e:Exception){
                        // todo : 예외처리
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val productImgInfo = ParcelRepository.getProductImgByProductIdx(rowParcelList[adapterPosition].productIdx)
                        for (imgInfo in productImgInfo!!.children) {
                            val def = imgInfo.child("default").value as String
                            val omgOrder = imgInfo.child("omgOrder").value as String
                            if (def == "true" && omgOrder == "1") {
                                val productImgFilename = imgInfo.child("imgSrc").value as String
                                val productImgUri = ParcelRepository.getProductImgByFilename(productImgFilename)
                                val url = URL(productImgUri.toString())
                                val httpURLConnection =
                                    url.openConnection() as HttpURLConnection
                                val productImgBitmap =
                                    BitmapFactory.decodeStream(httpURLConnection.inputStream)
                                parcelItemImageView.setImageBitmap(productImgBitmap)
                                break
                            }
                        }
                    } catch(e:Exception){
                        // todo : 예외 처리
                    }
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val parcelRecycItemBinding = ParcelRecycItemBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(parcelRecycItemBinding)

            parcelRecycItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return viewHolder
        }

        override fun getItemCount(): Int {
            return rowParcelList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = rowParcelList[position]
            holder.bindItem(item)
        }
    }

}
data class RowParcelItemClass(val productIdx:String, val productName:String, val productPrice:String, var productImg:Bitmap?,
                              val parcelStatus:String, val parcelDate:String)