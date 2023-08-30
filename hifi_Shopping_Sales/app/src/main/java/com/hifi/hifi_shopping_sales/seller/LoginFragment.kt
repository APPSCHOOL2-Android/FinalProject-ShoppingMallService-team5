package com.hifi.hifi_shopping_sales.seller

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

import com.hifi.hifi_shopping_sales.databinding.FragmentLoginBinding
import com.hifi.hifi_shopping_sales.repository.SellerRepository
import com.hifi.hifi_shopping_sales.vm.ProductViewModel
import com.hifi.hifi_shopping_sales.vm.SellerViewModel

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var sellerActivity: SellerActivity
    lateinit var sellerViewModel: SellerViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater)
        sellerActivity = activity as SellerActivity
        sellerViewModel = ViewModelProvider(sellerActivity)[SellerViewModel::class.java]
        fragmentLoginBinding.run{
            buttonLogin.setOnClickListener {
                loginSubmit()
            }
        }
        return fragmentLoginBinding.root
    }

    fun loginSubmit(){
        fragmentLoginBinding.run{
            // 사용자가 입력한 내용을 가져온다.
            // todo : 테스트 데이터 변경
            val loginSellerId = "test@gmail.com"
            val loginSellerPw = "1234"

            if(loginSellerId.isEmpty()){
                val builder = MaterialAlertDialogBuilder(sellerActivity)
                builder.setTitle("로그인 오류")
                builder.setMessage("아이디를 입력해주세요")
                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    sellerActivity.showSoftInput(textInputEditTextLoginUserId)
                }
                builder.show()
                return
            }

            if(loginSellerPw.isEmpty()){
                val builder = MaterialAlertDialogBuilder(sellerActivity)
                builder.setTitle("비밀번호 오류")
                builder.setMessage("비밀번호를 입력해주세요")
                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    sellerActivity.showSoftInput(textInputEditTextLoginUserPw)
                }
                builder.show()
                return
            }
            val errorCode = sellerViewModel.getLoginSellerInfo(loginSellerId, loginSellerPw)
            if(errorCode == 1){
                val builder = MaterialAlertDialogBuilder(sellerActivity)
                builder.setTitle("로그인 오류")
                builder.setMessage("존재하지 않는 아이디 입니다")
                builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                    textInputEditTextLoginUserId.setText("")
                    textInputEditTextLoginUserPw.setText("")
                    sellerActivity.showSoftInput(textInputEditTextLoginUserId)
                }
                builder.show()
            } else if(errorCode == 2){
                val builder = MaterialAlertDialogBuilder(sellerActivity)
                builder.setTitle("로그인 오류")
                builder.setMessage("잘못된 비밀번호 입니다")
                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    textInputEditTextLoginUserPw.setText("")
                    sellerActivity.showSoftInput(textInputEditTextLoginUserPw)
                }
                builder.show()
            }else{
                Snackbar.make(
                    fragmentLoginBinding.root,
                    "로그인 되었습니다",
                    Snackbar.LENGTH_SHORT
                ).show()

                sellerActivity.replaceFragment(
                    SellerActivity.ITEM_LIST_FRAGMENT,
                    false,
                    null
                )
            }
        }
    }
}