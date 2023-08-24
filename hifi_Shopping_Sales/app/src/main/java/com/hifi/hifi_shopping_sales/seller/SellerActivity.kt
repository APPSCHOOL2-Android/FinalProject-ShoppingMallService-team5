package com.hifi.hifi_shopping_sales.seller

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.MaterialSharedAxis
import com.hifi.hifi_shopping_sales.R
import com.hifi.hifi_shopping_sales.databinding.ActivitySellerBinding
import com.hifi.hifi_shopping_sales.vm.SellerViewModel
import kotlin.concurrent.thread

class SellerActivity : AppCompatActivity() {

    lateinit var activitySellerBinding: ActivitySellerBinding
    var newFragment:Fragment? = null
    var oldFragment:Fragment? = null

    companion object{
        val LOGIN_FRAGMENT = "LoginFragment"
        val JOIN_FRAGMENT = "JoinFragment"
        val ITEM_LIST_FRAGMENT = "ItemListFragment"
        val ADD_ITEM_FRAGMENT = "AddItemFragment"
    }

    lateinit var loginSellerClass:SellerClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySellerBinding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(activitySellerBinding.root)

        replaceFragment(LOGIN_FRAGMENT, true, null)
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
            LOGIN_FRAGMENT -> LoginFragment()
            ITEM_LIST_FRAGMENT -> ItemListFragment()
            ADD_ITEM_FRAGMENT -> AddItemFragment()
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
            fragmentTransaction.replace(R.id.mainContainer, newFragment!!)

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

    // 입력 요소에 포커스를 주는 메서드
    fun showSoftInput(view: View){
        view.requestFocus()

        val inputMethodManger = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        thread {
            SystemClock.sleep(200)
            inputMethodManger.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}
data class SellerClass(var idx:String, var companyName:String, var email:String, var name: String, var pw:String)
data class ProductClass(val idx:String, val category:String, val context:String, val price:String,
                        val name:String, val pointAmount:String, val sellerIdx:String, var imgList:MutableList<ImgClass>?)

data class ImgClass(val order:String, val default:String, val imgSrc:String, var bitmap: Bitmap?,
                    val productIdx:String)

data class ProductRowItemClass(val idx:String, var img:Bitmap?, val name:String, val price:String, val orderCnt:String, val reviewScore:String)