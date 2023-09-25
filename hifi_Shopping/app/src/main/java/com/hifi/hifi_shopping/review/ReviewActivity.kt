package com.hifi.hifi_shopping.review

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityReviewBinding
import com.hifi.hifi_shopping.databinding.ReviewRycItemBinding
import com.hifi.hifi_shopping.parcel.repository.ParcelRepository
import com.hifi.hifi_shopping.review.repository.ReviewProductRepository
import com.hifi.hifi_shopping.review.repository.ReviewSubscribeRepository
import com.hifi.hifi_shopping.review.vm.ReviewProductViewModel
import com.hifi.hifi_shopping.review.vm.ReviewSubscribeViewModel
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.UserActivity
import com.hifi.hifi_shopping.user.model.ReviewDataClass
import com.hifi.hifi_shopping.user.model.UserDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.concurrent.thread

class ReviewActivity : AppCompatActivity() {
    lateinit var activityReviewBinding: ActivityReviewBinding
    lateinit var reviewProductViewModel:ReviewProductViewModel
    lateinit var reviewSubscribeViewModel: ReviewSubscribeViewModel

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    private var uploadUri: Uri? = null
    var productIdx = "1"
    lateinit var userData: UserDataClass
    var reviewSubscribeList = mutableListOf<ReviewSubscribeClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityReviewBinding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(activityReviewBinding.root)

        val receivedIntent = intent
        if (receivedIntent != null && receivedIntent.hasExtra("productIdx")) {
            productIdx = receivedIntent.getStringExtra("productIdx")!!
        }
        if(receivedIntent != null && receivedIntent.hasExtra("userIdx")) {
            val userIdx = receivedIntent.getStringExtra("userIdx")!!
            val userEmail = receivedIntent.getStringExtra("userEmail")!!
            val userPw = receivedIntent.getStringExtra("userPw")!!
            val userNickname = receivedIntent.getStringExtra("userNickname")!!
            val userVerify = receivedIntent.getStringExtra("userVerify")!!
            val userPhoneNum = receivedIntent.getStringExtra("userPhoneNum")!!
            val userProfileImg = receivedIntent.getStringExtra("userProfileImg")!!
            val newUserData = UserDataClass(userIdx, userEmail, userPw, userNickname, userVerify,
                userPhoneNum, userProfileImg)
            userData = newUserData
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
                if(it!=null) {
                    activityReviewBinding.reviewWriteItemImageView.setImageBitmap(it)
                }
            }
        }

        reviewSubscribeViewModel.run{
            subscribeList.observe(this@ReviewActivity){
                reviewSubscribeList = it
                activityReviewBinding.reviewWriteItemRecommendHumanRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

        activityReviewBinding.run{
            reviewProductViewModel.getProductByIdx(productIdx)
            reviewSubscribeViewModel.getSubscribeListByUserIdx(userData.idx)

            reviewImageView.visibility = View.GONE
            reviewWriteToolbar.run{
                setNavigationOnClickListener {
                    finish()
                }
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_item_search -> {
                            val intent = Intent(this@ReviewActivity, SearchActivity::class.java)
                            intent.putExtra("userEmail", userData.email)
                            intent.putExtra("userIdx", userData.idx)
                            intent.putExtra("userNickname", userData.nickname)
                            intent.putExtra("userPw", userData.pw)
                            intent.putExtra("userVerify", userData.verify)
                            intent.putExtra("userPhoneNum", userData.phoneNum)
                            intent.putExtra("userProfileImg", userData.profileImg)
                            startActivity(intent)
                        }
                        R.id.menu_item_cart -> {
                            val intent = Intent(this@ReviewActivity, UserActivity::class.java)
                            intent.putExtra("whereFrom", "review")
                            intent.putExtra("userFragmentType", "cart")
                            intent.putExtra("userEmail", userData.email)
                            intent.putExtra("userIdx", userData.idx)
                            intent.putExtra("userNickname", userData.nickname)
                            intent.putExtra("userPw", userData.pw)
                            intent.putExtra("userVerify", userData.verify)
                            intent.putExtra("userPhoneNum", userData.phoneNum)
                            intent.putExtra("userProfileImg", userData.profileImg)
                            startActivity(intent)
                        }
                    }
                    true
                }
            }
            reviewWritePictureAddButton.setOnClickListener {
                clickAlbumLaunch(albumLauncher)
            }

            reviewWriteItemRecommendHumanRecyclerView.run{
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            reviewWriteAddReviewButton.setOnClickListener{
                val reviewContext = reviewWriteReviewContentEditTextText.text.toString()
                val score = reviewWriteQuestionItemRatingBar.rating.toString()
                val idx = UUID.randomUUID().toString()
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val writeDate = sdf.format(Date(System.currentTimeMillis()))

                if(reviewContext.isEmpty()){
                    val builder = MaterialAlertDialogBuilder(this@ReviewActivity)
                    builder.setTitle("리뷰 내용 입력 오류")
                    builder.setMessage("리뷰 내용을 입력해주세요")
                    builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                        showSoftInput(reviewWriteReviewContentEditTextText)
                    }
                    builder.show()
                    return@setOnClickListener
                }

                var fileName = "empty_photo"
                if(uploadUri != null){
                    fileName = "img_review_${System.currentTimeMillis()}.jpg"
                    ReviewProductRepository.uploadImage(uploadUri!!, fileName)
                }

                val newReview = ReviewDataClass(idx, productIdx, "리뷰 제목", reviewContext, score, userData.idx, "0",
                    writeDate, fileName)
                ReviewProductRepository.addReviewInfo(newReview)
                finish()
            }
        }
    }
    inner class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
        inner class ViewHolder(reviewRycItemBinding: ReviewRycItemBinding) : RecyclerView.ViewHolder(reviewRycItemBinding.root){
            var profile = reviewRycItemBinding.editUserProfileImg
            var nickname = reviewRycItemBinding.subscribeUserNickname

            fun bindItem(reviewSubscribeItem:ReviewSubscribeClass){
                nickname.text = reviewSubscribeItem.nickname
                profile.setImageResource(R.drawable.sample_img)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val productImgUri = ReviewSubscribeRepository.getUserProfileImgByFilename(reviewSubscribeItem.filename)
                        val url = URL(productImgUri.toString())
                        val httpURLConnection =
                            url.openConnection() as HttpURLConnection
                        val productImgBitmap =
                            BitmapFactory.decodeStream(httpURLConnection.inputStream)
                        profile.setImageBitmap(productImgBitmap)
                    } catch(e:Exception){
                        // todo : 예외 처리
                    }
                }
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
            return reviewSubscribeList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val reviewSubscribeItem = reviewSubscribeList[position]
            holder.bindItem(reviewSubscribeItem)
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
    fun showSoftInput(view: View){
        view.requestFocus()

        val inputMethodManger = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        thread {
            SystemClock.sleep(200)
            inputMethodManger.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    data class reviewDataClass(val context:String, val date:String, val idx:String, val imgSrc:String,
        val likeCnt:String, val productIdx:String, val score:String, val title:String, val writerIdx:String)
}
data class ReviewSubscribeClass(val nickname:String, val filename:String, val imgBitmap: Bitmap?)