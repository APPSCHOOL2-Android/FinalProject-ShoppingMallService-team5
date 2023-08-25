package com.hifi.hifi_shopping.user

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.MaterialSharedAxis
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityUserBinding
import com.hifi.hifi_shopping.user.model.UserDataClass
import kotlin.concurrent.thread

class UserActivity : AppCompatActivity() {

    lateinit var activityUserBinding : ActivityUserBinding

    var newFragment:Fragment? = null
    var oldFragment:Fragment? = null

    var userTemp = UserDataClass("e8fa83ce-5341-4f10-9929-5521d9c5fe82", "ohsso98@naver.com", "0618", "김대박", "true", "010-1111-1111", "")

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

        replaceFragment(MY_PAGE_FRAGMENT, false, null)
//        replaceFragment(EDIT_USER_FRAGMENT, false, null)
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


}