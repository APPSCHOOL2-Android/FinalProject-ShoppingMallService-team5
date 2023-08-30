package com.hifi.hifi_shopping.review

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityReviewBinding
import com.hifi.hifi_shopping.databinding.ReviewRycItemBinding
import com.hifi.hifi_shopping.review.vm.ReviewProductViewModel
import com.hifi.hifi_shopping.review.vm.ReviewSubscribeViewModel
import com.hifi.hifi_shopping.user.model.UserDataClass
import java.text.NumberFormat
import java.util.Locale

class ReviewActivity : AppCompatActivity() {
    lateinit var activityReviewBinding: ActivityReviewBinding
    lateinit var reviewProductViewModel:ReviewProductViewModel
    lateinit var reviewSubscribeViewModel: ReviewSubscribeViewModel

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    var uploadUri: Uri? = null
//    val auth = FirebaseAuth.getInstance()
//    val user = auth.currentUser
    // lateinit var productIdx: String
    var productIdx = "1"
    var userIdx = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityReviewBinding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(activityReviewBinding.root)

        val receivedIntent = intent
        if (receivedIntent != null && receivedIntent.hasExtra("productIdx")) {
            productIdx = receivedIntent.getStringExtra("productIdx")!!
            userIdx = receivedIntent.getStringExtra("userIdx")!!
            // Log.d("리뷰 데이터",productIdx.toString())
        }
        reviewProductViewModel = ViewModelProvider(this)[ReviewProductViewModel::class.java]
        reviewSubscribeViewModel = ViewModelProvider(this)[ReviewSubscribeViewModel::class.java]

        albumLauncher = albumSetting(activityReviewBinding.reviewImageView)
        reviewProductViewModel.run{
            productName.observe(this@ReviewActivity){
                activityReviewBinding.reviewWriteItemTitleTextView.text = it
            }
            productPrice.observe(this@ReviewActivity){
                activityReviewBinding.reviewWriteItemPricetextView.text = formatPrice(it)
            }
            productImg.observe(this@ReviewActivity){
                activityReviewBinding.reviewWriteItemImageView.setImageBitmap(it)
            }
        }

        reviewSubscribeViewModel.run{
            subscribeList.observe(this@ReviewActivity){
                activityReviewBinding.reviewWriteItemRecommendHumanRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

        activityReviewBinding.run{
            // todo : 해당 상품 idx 입력 연결
            reviewProductViewModel.getProductByIdx(productIdx)
            reviewSubscribeViewModel.getSubscribeListByUserIdx(userIdx)

            reviewImageView.visibility = View.GONE
            reviewWriteToolbar.run{
                setNavigationOnClickListener {
                    finish()
                }
            }
            reviewWritePictureAddButton.setOnClickListener {
                clickAlbumLaunch(albumLauncher)
            }

            reviewWriteItemRecommendHumanRecyclerView.run{
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }
    inner class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
        inner class ViewHolder(reviewRycItemBinding: ReviewRycItemBinding) : RecyclerView.ViewHolder(reviewRycItemBinding.root){
            var profile : ImageView
            var nickname : TextView
            init{
                profile = reviewRycItemBinding.editUserProfileImg
                nickname = reviewRycItemBinding.textView5
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val reviewRycItemBinding = ReviewRycItemBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(reviewRycItemBinding)

            reviewRycItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return viewHolder
        }

        override fun getItemCount(): Int {
            return reviewSubscribeViewModel.subscribeList.value?.size!!
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if(reviewSubscribeViewModel.subscribeList.value?.get(position)?.profile == null) {
                holder.profile.setImageResource(R.drawable.empty_photo)
            }else{
                holder.profile.setImageBitmap(reviewSubscribeViewModel.subscribeList.value?.get(position)?.profile)
            }
            holder.nickname.text = reviewSubscribeViewModel.subscribeList.value?.get(position)?.nickname
        }
    }

    fun albumSetting(previewImageView: ImageView) : ActivityResultLauncher<Intent>{
        val albumContract = ActivityResultContracts.StartActivityForResult()
        val albumLauncher = registerForActivityResult(albumContract){
            if(it.resultCode == RESULT_OK){
                // 선택한 이미지에 접근할 수 있는 Uri 객체를 추출한다.
                if(it.data?.data != null){
                    uploadUri = it.data?.data
                    // 안드로이드 10 (Q) 이상이라면...
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        // 이미지를 생성할 수 있는 디코더를 생성한다.
                        val source = ImageDecoder.createSource(contentResolver, uploadUri!!)
                        // Bitmap객체를 생성한다.
                        val bitmap = ImageDecoder.decodeBitmap(source)

                        previewImageView.setImageBitmap(bitmap)
                        previewImageView.visibility = View.VISIBLE
                    } else {
                        // 컨텐츠 프로바이더를 통해 이미지 데이터 정보를 가져온다.
                        val cursor = contentResolver.query(uploadUri!!, null, null, null, null)
                        if(cursor != null){
                            cursor.moveToNext()

                            // 이미지의 경로를 가져온다.
                            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                            val source = cursor.getString(idx)

                            // 이미지를 생성하여 보여준다.
                            val bitmap = BitmapFactory.decodeFile(source)
                            previewImageView.setImageBitmap(bitmap)
                            previewImageView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        return albumLauncher
    }

    fun clickAlbumLaunch(albumLauncher: ActivityResultLauncher<Intent>){
        val newIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        newIntent.setType("image/*")
        val mimeType = arrayOf("image/*")
        newIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        albumLauncher.launch(newIntent)
    }

    fun formatPrice(priceStr: String): String {
        val price = priceStr.toIntOrNull() ?: return "Invalid Input"
        val formatter = NumberFormat.getNumberInstance(Locale("ko", "KR"))
        val formattedAmount = formatter.format(price)
        return "$formattedAmount 원"
    }
}