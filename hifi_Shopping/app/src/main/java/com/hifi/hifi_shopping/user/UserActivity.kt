package com.hifi.hifi_shopping.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityUserBinding
import com.hifi.hifi_shopping.user.model.UserDataClass
import java.util.UUID

class UserActivity : AppCompatActivity() {

    lateinit var activityUserBinding: ActivityUserBinding

    var newFragment:Fragment? = null
    var oldFragment:Fragment? = null

    lateinit var userData : UserDataClass

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
        setContentView(R.layout.activity_user)
        getUserData()
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

            // oldFragment -> newFragment로 이동
            // oldFramgent : exit
            // newFragment : enter

            // oldFragment <- newFragment 로 되돌아가기
            // oldFragment : reenter
            // newFragment : return

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

    fun addUserData(){
        val uuid = UUID.randomUUID()
        val idx = uuid.toString()

        val userData = UserDataClass(idx,"ohsso98@naver.com", "0618","김대박",false,"01000000000","user_sample.jpg")

        val database = FirebaseDatabase.getInstance()
        val userDataRef = database.getReference("UserData")
        userDataRef.push().setValue(userData).addOnCompleteListener{
            Log.d("유저 데이터",userData.toString())
        }
    }

    fun getUserData(){
        val database = FirebaseDatabase.getInstance()
        val userDataRef = database.getReference("UserData")
        val loginUserId = "ohsso98@naver.com"

        userDataRef.orderByChild("userId").equalTo(loginUserId).get().addOnCompleteListener {
            for(c1 in it.result.children) {
                val idx = c1.child("idx").value as String
                val email = c1.child("email").value as String
                val pw = c1.child("pw").value as String
                val nickname = c1.child("nickname").value as String
                val isVerify = c1.child("verify").value as Boolean
                val phoneNum = c1.child("phoneNum").value as String
                val profileImg = c1.child("profileImg").value as String

                userData = UserDataClass(idx,email, pw, nickname, isVerify, phoneNum, profileImg)
            }
        }
    }
}