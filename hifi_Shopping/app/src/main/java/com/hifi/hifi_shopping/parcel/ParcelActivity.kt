package com.hifi.hifi_shopping.parcel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityParcelBinding
import com.hifi.hifi_shopping.databinding.PacelRecycItemBinding

class ParcelActivity : AppCompatActivity() {

    lateinit var activityParcelBinding: ActivityParcelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityParcelBinding = ActivityParcelBinding.inflate(layoutInflater)
        setContentView(activityParcelBinding.root)

        activityParcelBinding.run{
            pacelMaterialToolbar.run{
                title = "배송 관리"
                setNavigationIcon(R.drawable.chevron_left_24px)
                inflateMenu(R.menu.toolbar_menu_basic)
                isTitleCentered = true
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

            pacelItemRecycView.run{
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    inner class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
        inner class ViewHolder(pacelRecycItemBinding: PacelRecycItemBinding) : RecyclerView.ViewHolder(pacelRecycItemBinding.root){
            var pacelItemImageView = pacelRecycItemBinding.pacelItemImageView
            var pacelItemStatustextView = pacelRecycItemBinding.pacelItemStatustextView
            var pacelItemNametextView = pacelRecycItemBinding.pacelItemNametextView
            var pacelItemPriceTextView = pacelRecycItemBinding.pacelItemPriceTextView
            var pacelOderChangeButton = pacelRecycItemBinding.pacelOderChangeButton
            var pacelOderCancelButton = pacelRecycItemBinding.pacelOderCancelButton

            init{
                pacelRecycItemBinding.root.setOnClickListener {
                    // todo : review activity로 이동
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val pacelRecycItemBinding = PacelRecycItemBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(pacelRecycItemBinding)

            pacelRecycItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return viewHolder
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // holder.pacelItemImageView.setImageResource(R.drawable.couple)
            holder.pacelItemNametextView.text = "TMA-2 Comfort Wireless"
            holder.pacelItemStatustextView.text = "배송지 도착"
            holder.pacelItemPriceTextView.text = "20,000 원"
        }
    }

}