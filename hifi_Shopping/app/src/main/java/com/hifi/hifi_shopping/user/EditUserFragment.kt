package com.hifi.hifi_shopping.user

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentEditUserBinding
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.vm.AddressViewModel
import com.hifi.hifi_shopping.user.vm.ProductViewModel

class EditUserFragment : Fragment() {

    lateinit var fragmentEditUserBinding: FragmentEditUserBinding
    lateinit var userActivity : UserActivity
    lateinit var addressViewModel: AddressViewModel
    var isToggled = false
    var isVerify = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentEditUserBinding = FragmentEditUserBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity

        val userTemp = userActivity.userTemp

        addressViewModel = ViewModelProvider(userActivity)[AddressViewModel::class.java]

        addressViewModel.run {
            addressDataList.observe(userActivity){
                val addr = addressDataList.value?.get(0)?.address?.split("/")
                fragmentEditUserBinding.editUserAddrEditText.setText(addr?.get(0))
                fragmentEditUserBinding.editUserAddrDetailEditText.setText(addr?.get(1))
            }
        }

        fragmentEditUserBinding.run {
            editUserToolbar.run {
                setNavigationOnClickListener {
                    userActivity.removeFragment(UserActivity.MY_PAGE_FRAGMENT)

                }

                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_search -> {
                            val intent = Intent(userActivity, SearchActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.menu_item_cart -> {
                            userActivity.replaceFragment(UserActivity.CART_FRAGMENT, true, null)
                        }
                    }
                    true
                }
            }

            editUserEditNick.setText(userTemp.nickname)
            addressViewModel.getAddressListByUser(userTemp.idx)

            editUserToChangePwdBtnToggle.setOnClickListener {
                toggleButtonClick()
            }

            editUserPhoneBtnCheck.setOnClickListener {
                phoneCheck()

            }
            editUserPhoneBtnCheckVerify.setOnClickListener {
                phoneVerify()
            }

            editUserPwdEditTextSameCheck.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                        // 비밀번호 확인 메서드 호출
                        pwdSameCheck()
                        return true
                    }
                    return false
                }
            })


        }
        return fragmentEditUserBinding.root
    }

    fun toggleButtonClick() {
        isToggled = !isToggled
        if (isToggled) {
            // 토글이 활성화된 경우
            fragmentEditUserBinding.run {
                editUserToChangePwdBtnToggle.setImageResource(R.drawable.expand_less_24px)
                editUserPhone.visibility = View.VISIBLE
                if(isVerify){
                    editUserPwdEditTextChange.visibility = View.VISIBLE
                }
            }
        } else {
            // 토글이 비활성화된 경우
            fragmentEditUserBinding.run {
                editUserToChangePwdBtnToggle.setImageResource(R.drawable.expand_more_24px)
                editUserPhone.visibility = View.GONE
                if(isVerify){
                    editUserPwdEditTextChange.visibility = View.GONE
                }
            }
        }
    }

    fun phoneCheck() {
        fragmentEditUserBinding.run {
            if(editUserPhoneEditText.text.toString().isEmpty()){
                val builder = MaterialAlertDialogBuilder(userActivity)
                builder.setTitle("전화번호 오류")
                builder.setMessage("전화번호를 입력해주세요.")
                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    userActivity.showSoftInput(editUserPhoneEditText)
                }
                builder.show()
                return
            }else {
                userActivity.hideKeyboard(editUserPhoneEditText)
                editUserPhoneEditTextVerify.setText("1312")
            }

        }

    }

    fun phoneVerify(){
        fragmentEditUserBinding.run {
            val verifyText = editUserPhoneEditTextVerify.text.toString()
            Log.d("휴대폰",verifyText.toString())
            if(verifyText == "1312"){
                isVerify=true
                editUserPwdEditTextChange.visibility = View.VISIBLE
            }
            if(isVerify){
                editUserPhoneEditText.isEnabled = false
                editUserPhoneBtnCheck.text = "인증 완료"
                editUserPhoneBtnCheck.isClickable = false
                userActivity.hideKeyboard(editUserPhoneEditText)

                editUserPhoneEditTextVerify.visibility = View.GONE
                editUserPhoneBtnCheckVerify.visibility = View.GONE
            }
        }
    }

    fun pwdSameCheck(){
        fragmentEditUserBinding.run {
            val newPwd = editUserPwdEditTextNew.text.toString()
            val checkNewPwd = editUserPwdEditTextSameCheck.text.toString()
            if(newPwd != checkNewPwd){
                editUserPwdEditTextSameCheck.error ="비밀번호가 일치하지 않습니다."
            }else{
                userActivity.hideKeyboard(editUserPwdEditTextSameCheck)
            }
        }
    }
}