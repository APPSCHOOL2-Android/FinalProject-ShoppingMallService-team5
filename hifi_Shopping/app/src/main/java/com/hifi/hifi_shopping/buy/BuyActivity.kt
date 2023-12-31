package com.hifi.hifi_shopping.buy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.datamodel.AddressData
import com.hifi.hifi_shopping.buy.fragment.BuyOrderCompleteFragment
import com.hifi.hifi_shopping.buy.fragment.BuyOrderCompleteListFragment
import com.hifi.hifi_shopping.buy.fragment.DetailItemFragment
import com.hifi.hifi_shopping.buy.fragment.OrderFragment
import com.hifi.hifi_shopping.databinding.ActivityBuyBinding
import com.hifi.hifi_shopping.databinding.RowOrderItemListBinding
import com.hifi.hifi_shopping.user.model.UserDataClass
import kotlin.concurrent.thread

class BuyActivity : AppCompatActivity() {

    companion object{
        val BUY_ORDER_COMPLETE_FRAGMENT = "BuyOrderCompleteFragment"
        val BUY_ORDER_COMPLETELIST_FRAGMENT = "BuyOrderCompleteListFragment"
        val DETAIL_ITEM_FRAGMENT = "DetailItemFragment"
        val ORDER_FRAGMENT = "OrderFragment"
    }

    lateinit var activityBuyBinding: ActivityBuyBinding

    var newFragment:Fragment? = null
    var oldFragment:Fragment? = null

    var orderCompleteProductList = mutableListOf<RowOrderItemListBinding>()
    lateinit var orderCompleteAddress: AddressData
    var totalOrderProductCount = 0
    var totalOrderProductPrice = 0
    var oriTotalOrderProductPrice = 0
    var buyProductList = ArrayList<String>()
    var bundle = Bundle()
    lateinit var newUserData: UserDataClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBuyBinding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(activityBuyBinding.root)

        dataSetting()
        startFragment()

    }

    // 입력 받은 정보에 따라 아이템 상세화면을 보여줄지, 주문창을 보여줄지 결정
    private fun startFragment(){

        var bundle = bundleSetting()

        if(buyProductList?.size == 1){
            bundle.putString("selProduct", buyProductList[0]) // 상품 인덱스
            replaceFragment(DETAIL_ITEM_FRAGMENT, true, bundle)
        } else {
            bundle.putStringArrayList("selProduct", buyProductList)
            replaceFragment(ORDER_FRAGMENT, true, bundle)
        }
    }

    private fun dataSetting(){
        val email = intent.getStringExtra("userEmail")!!
        val userIdx = intent.getStringExtra("userIdx")!!
        val userNickname = intent.getStringExtra("userNickname")!!
        val userPw = intent.getStringExtra("userPw")!!
        val userProfileImg = intent.getStringExtra("userProfileImg")!!
        val userVerify = intent.getStringExtra("userVerify")!!
        val userPhoneNum = intent.getStringExtra("userPhoneNum")!!
        buyProductList = intent.getStringArrayListExtra("buyProduct")!!
        newUserData = UserDataClass(userIdx, email, userPw, userNickname, userVerify, userPhoneNum, userProfileImg)
    }


    private fun bundleSetting(): Bundle{
        val bundle = Bundle()
        buyProductList = intent.getStringArrayListExtra("buyProduct")!!

        bundle.putString("userEmail", newUserData.email)
        bundle.putString("userIdx", newUserData.idx)
        bundle.putString("userNickname", newUserData.nickname)
        bundle.putString("userPw", newUserData.pw)
        bundle.putString("userProfileImg", newUserData.profileImg)
        bundle.putString("userVerify", newUserData.verify)
        bundle.putString("userPhoneNum", newUserData.phoneNum)

        return bundle
    }

    fun intentSetting(returnIntent: Intent): Intent {

        returnIntent.putExtra("userEmail", newUserData.email)
        returnIntent.putExtra("userIdx", newUserData.idx)
        returnIntent.putExtra("userNickname", newUserData.nickname)
        returnIntent.putExtra("userPw", newUserData.pw)
        returnIntent.putExtra("userProfileImg", newUserData.profileImg)
        returnIntent.putExtra("userVerify", newUserData.verify)
        returnIntent.putExtra("userPhoneNum", newUserData.phoneNum)

        return returnIntent
    }


     fun activityKill() {
         this@BuyActivity.finish()
    }

    fun changeWon(price: String, count: Int): String{
        val sb = StringBuilder()
        val sumPrice = price.replace(",","").replace("원","").toInt() * count

        sumPrice.toString().reversed().forEachIndexed { index, c ->
            sb.append("$c")
            if((index+1) % 3 == 0)sb.append(",")
        }

        if(sb.last() == ',') sb.deleteCharAt(sb.lastIndex)

        return "${sb.reverse()}원"
    }

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
            DETAIL_ITEM_FRAGMENT -> DetailItemFragment()
            ORDER_FRAGMENT -> OrderFragment()
            BUY_ORDER_COMPLETELIST_FRAGMENT -> BuyOrderCompleteListFragment()
            BUY_ORDER_COMPLETE_FRAGMENT -> BuyOrderCompleteFragment()
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
            fragmentTransaction.replace(R.id.buyContainer, newFragment!!)

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
        if(supportFragmentManager.backStackEntryCount == 0 ) {
            this@BuyActivity.finish()
        }
    }


    fun softInputVisible(view:View, visible: Boolean){
        if(visible){
            view.requestFocus()
            val inputMethodManger = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            thread {
                SystemClock.sleep(200)
                inputMethodManger.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }else {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            view.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}