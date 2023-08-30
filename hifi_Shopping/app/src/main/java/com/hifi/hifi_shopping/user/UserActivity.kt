package com.hifi.hifi_shopping.user

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.fragment.FAQFragment
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.ActivityUserBinding
import com.hifi.hifi_shopping.parcel.ParcelActivity
import com.hifi.hifi_shopping.review.ReviewActivity
import com.hifi.hifi_shopping.subscribe.SubscribeActivity
import com.hifi.hifi_shopping.user.model.UserDataClass
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class UserActivity : AppCompatActivity() {

    lateinit var activityUserBinding : ActivityUserBinding
    val auth = FirebaseAuth.getInstance()

    var newFragment:Fragment? = null
    var oldFragment:Fragment? = null

    lateinit var userTemp: UserDataClass
    var whereFrom = "myPage"

    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION,
        Manifest.permission.INTERNET
    )

    companion object{
        val MY_PAGE_FRAGMENT = "MyPageFragment"
        val CART_FRAGMENT = "CartFragment"
        val COUPON_FRAGMENT = "CouponFragment"
        val EDIT_USER_FRAGMENT = "EditUserFragment"
        val FAQ_FRAGMENT = "FAQFragment"
        val POINT_FRAGMENT = "PointFragment"
        val PURCHASE_FRAGMENT = "PurchaseFragment"
        val USER_PAGE_FRAGMENT = "UserPageFragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUserBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(activityUserBinding.root)

        requestPermissions(permissionList,0)

        val receivedIntent = intent
        if (receivedIntent != null && receivedIntent.hasExtra("userEmail")) {
            val email = receivedIntent.getStringExtra("userEmail")!!
            val userIdx = receivedIntent.getStringExtra("userIdx")!!
            val userNickname = receivedIntent.getStringExtra("userNickname")!!
            val userPw = receivedIntent.getStringExtra("userPw")!!
            val userProfileImg = receivedIntent.getStringExtra("userProfileImg")!!
            val userVerify = receivedIntent.getStringExtra("userVerify")!!
            val userPhoneNum = receivedIntent.getStringExtra("userPhoneNum")!!
            val newUserData = UserDataClass(userIdx, email, userPw, userNickname,
                userVerify, userPhoneNum, userProfileImg)
            userTemp = newUserData
        }

        if (receivedIntent != null && receivedIntent.hasExtra("userFragmentType")) {
            val userFragmentType = receivedIntent.getStringExtra("userFragmentType")!!
            whereFrom = receivedIntent.getStringExtra("whereFrom")!!
            when(userFragmentType){
                "cart" -> replaceFragment(CART_FRAGMENT, false, null)
                "userPage"->replaceFragment(USER_PAGE_FRAGMENT,false,null)
            }
        }else{
            replaceFragment(MY_PAGE_FRAGMENT, false, null)
        }
        // authTestViewModel = ViewModelProvider()
//        replaceFragment(EDIT_USER_FRAGMENT, false, null)

//        replaceFragment(CART_FRAGMENT, false, null)

    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name:String, addToBackStack:Boolean, bundle:Bundle?){

        SystemClock.sleep(200)

        // Fragment 교체 상태로 설정한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // newFragment 에 Fragment가 들어있으면 oldFragment에 넣어준다.
        if(newFragment != null){
            oldFragment = newFragment
        }

        // 새로운 Fragment를 담을 변수
        newFragment = when(name){
            MY_PAGE_FRAGMENT -> MyPageFragment()
            CART_FRAGMENT -> CartFragment()
            COUPON_FRAGMENT -> CouponFragment()
            EDIT_USER_FRAGMENT -> EditUserFragment()
            FAQ_FRAGMENT -> FAQFragment()
            POINT_FRAGMENT -> PointFragment()
            PURCHASE_FRAGMENT -> PurchaseFragment()
            USER_PAGE_FRAGMENT -> UserPageFragment()
            else -> Fragment()
        }

        newFragment?.arguments = bundle

        if(newFragment != null) {
            // 애니메이션 설정
            if(oldFragment != null){
                oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
                oldFragment?.enterTransition = null
                oldFragment?.returnTransition = null
            }

            newFragment?.exitTransition = null
            newFragment?.reenterTransition = null
            newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

            // Fragment를 교채한다.
            fragmentTransaction.replace(R.id.userContainer, newFragment!!)

            if (addToBackStack == true) {
                // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
                fragmentTransaction.addToBackStack(name)
            }

            // 교체 명령이 동작하도록 한다.
            fragmentTransaction.commit()
        }
    }

    // Fragment를 BackStack에서 제거한다.
    fun removeFragment(name:String){
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
    fun showSoftInput(view: View){
        view.requestFocus()

        val inputMethodManger = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        thread {
            SystemClock.sleep(200)
            inputMethodManger.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getUserProfileImg(userTemp: UserDataClass, imgView: ImageView){
        val storage = FirebaseStorage.getInstance()
        if(userTemp.profileImg.isNullOrBlank() ||  userTemp.profileImg != userTemp.idx ){
            return
        }
        val fileName = "user/"+userTemp.profileImg
        Log.d("fileName",fileName)
        val fileRef = storage.reference.child(fileName)

        // 데이터를 가져올 수 있는 경로를 가져온다.
        fileRef.downloadUrl.addOnCompleteListener { downloadTask ->
            thread {
                // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                val url = URL(downloadTask.result.toString())
                // 접속한다.
                val httpURLConnection = url.openConnection() as HttpURLConnection
                // 이미지 객체를 생성한다.
                val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)

                this@UserActivity.runOnUiThread {
                    imgView.setImageBitmap(bitmap)
                }
            }
        }.addOnFailureListener {
            null
        }
    }


    fun whatIsPrev(fragment :String){
        when(whereFrom){
            "category" -> {
                val intent = Intent(this@UserActivity, CategoryActivity::class.java)
                intent.putExtra("navigateTo",R.id.bottomMenuItemCategoryMain)
                intent.putExtra("userEmail", userTemp.email)
                intent.putExtra("userIdx", userTemp.idx)
                intent.putExtra("userNickname", userTemp.nickname)
                intent.putExtra("userPw", userTemp.pw)
                intent.putExtra("userVerify", userTemp.verify)
                intent.putExtra("userPhoneNum", userTemp.phoneNum)
                intent.putExtra("userProfileImg", userTemp.profileImg)
                startActivity(intent)
            }
            "wish" ->{
                val intent = Intent(this@UserActivity, CategoryActivity::class.java)
                intent.putExtra("navigateTo",R.id.bottomMenuItemWish)
                intent.putExtra("userEmail", userTemp.email)
                intent.putExtra("userIdx", userTemp.idx)
                intent.putExtra("userNickname", userTemp.nickname)
                intent.putExtra("userPw", userTemp.pw)
                intent.putExtra("userVerify", userTemp.verify)
                intent.putExtra("userPhoneNum", userTemp.phoneNum)
                intent.putExtra("userProfileImg", userTemp.profileImg)
                startActivity(intent)
            }
            "recommend" ->{
                val intent = Intent(this@UserActivity, CategoryActivity::class.java)
                intent.putExtra("navigateTo",R.id.bottomMenuItemRecommend)
                intent.putExtra("userEmail", userTemp.email)
                intent.putExtra("userIdx", userTemp.idx)
                intent.putExtra("userNickname", userTemp.nickname)
                intent.putExtra("userPw", userTemp.pw)
                intent.putExtra("userVerify", userTemp.verify)
                intent.putExtra("userPhoneNum", userTemp.phoneNum)
                intent.putExtra("userProfileImg", userTemp.profileImg)
                startActivity(intent)
            }
            "rank" ->{
                val intent = Intent(this@UserActivity, CategoryActivity::class.java)
                intent.putExtra("navigateTo",R.id.bottomMenuItemRankMain)
                intent.putExtra("userEmail", userTemp.email)
                intent.putExtra("userIdx", userTemp.idx)
                intent.putExtra("userNickname", userTemp.nickname)
                intent.putExtra("userPw", userTemp.pw)
                intent.putExtra("userVerify", userTemp.verify)
                intent.putExtra("userPhoneNum", userTemp.phoneNum)
                intent.putExtra("userProfileImg", userTemp.profileImg)
                startActivity(intent)
            }
            "review" ->{
                finish()
            }
            "subscribe" ->{
                val intent = Intent(this@UserActivity, SubscribeActivity::class.java)
                intent.putExtra("userEmail", userTemp.email)
                intent.putExtra("userIdx", userTemp.idx)
                intent.putExtra("userNickname", userTemp.nickname)
                intent.putExtra("userPw", userTemp.pw)
                intent.putExtra("userVerify", userTemp.verify)
                intent.putExtra("userPhoneNum", userTemp.phoneNum)
                intent.putExtra("userProfileImg", userTemp.profileImg)
                startActivity(intent)
            }
            "parcel" ->{
                val intent = Intent(this@UserActivity, ParcelActivity::class.java)
                intent.putExtra("userIdx", userTemp.idx)
                startActivity(intent)
            }
            "buy" -> {
                val intent = Intent(this@UserActivity, ParcelActivity::class.java)
                intent.putExtra("userEmail", userTemp.email)
                intent.putExtra("userIdx", userTemp.idx)
                intent.putExtra("userNickname", userTemp.nickname)
                intent.putExtra("userPw", userTemp.pw)
                intent.putExtra("userVerify", userTemp.verify)
                intent.putExtra("userPhoneNum", userTemp.phoneNum)
                intent.putExtra("userProfileImg", userTemp.profileImg)
                startActivity(intent)
            }
            else -> {
                removeFragment(fragment)
            }

        }
        whereFrom="myPage"
    }


}