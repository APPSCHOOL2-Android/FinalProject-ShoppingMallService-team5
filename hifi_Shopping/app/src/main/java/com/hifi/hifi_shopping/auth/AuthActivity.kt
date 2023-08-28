package com.hifi.hifi_shopping.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.FirebaseAuth
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.ActivityAuthBinding
import kotlin.concurrent.thread

class AuthActivity : AppCompatActivity() {

    lateinit var activityAuthBinding: ActivityAuthBinding

    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    companion object {
        val AUTH_LOGIN_FRAGMENT = "AuthLoginFragment"
        val AUTH_JOIN_FRAGMENT = "AuthJoinFragment"
        val AUTH_FIND_PW_FRAGMENT = "AuthFindPwFragment"
        val AUTH_FIND_RESULT_FRAGMENT = "AuthFindResultFragment"
    }

    // nullable한 FirebaseAuth 객체 선언 (Authentication)
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(activityAuthBinding.root)

        replaceFragment(AUTH_LOGIN_FRAGMENT, false, null)

        // auth 객체 초기화 (Authentication)
        auth = FirebaseAuth.getInstance()
        if(auth != null){
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
            auth!!.signOut()
        }
    }


    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name: String, addToBackStack: Boolean, bundle: Bundle?) {

        SystemClock.sleep(200)

        // Fragment 교체 상태로 설정한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // newFragment 에 Fragment가 들어있으면 oldFragment에 넣어준다.
        if (newFragment != null) {
            oldFragment = newFragment
        }

        // 새로운 Fragment를 담을 변수
        newFragment = when (name) {
            AUTH_LOGIN_FRAGMENT -> AuthLoginFragment()
            AUTH_JOIN_FRAGMENT -> AuthJoinFragment()
            AUTH_FIND_PW_FRAGMENT -> AuthFindPwFragment()
            AUTH_FIND_RESULT_FRAGMENT -> AuthFindResultFragment()
            else -> Fragment()
        }

        newFragment?.arguments = bundle

        if (newFragment != null) {

            // 애니메이션 설정
            if (oldFragment != null) {
                oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
                oldFragment?.enterTransition = null
                oldFragment?.returnTransition = null
            }
            newFragment?.exitTransition = null
            newFragment?.reenterTransition = null
            newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

            // Fragment를 교체한다.
            fragmentTransaction.replace(R.id.authMainContainer, newFragment!!)

            if (addToBackStack == true) {
                // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
                fragmentTransaction.addToBackStack(name)
            }
            // 교체 명령이 동작하도록 한다.
            fragmentTransaction.commit()
        }
    }

    // Fragment를 BackStack에서 제거한다.
    fun removeFragment(name: String) {
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    // 입력 요소에 포커스를 주는 메서드
    fun showSoftInput(view: View) {
        view.requestFocus()

        val inputMethodManger = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        thread {
            SystemClock.sleep(200)
            inputMethodManger.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

}


