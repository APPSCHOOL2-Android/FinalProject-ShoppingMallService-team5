package com.hifi.hifi_shopping_sales.seller

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

import com.hifi.hifi_shopping_sales.databinding.FragmentLoginBinding
import com.hifi.hifi_shopping_sales.repository.SellerRepository

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var sellerActivity: SellerActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater)
        sellerActivity = activity as SellerActivity
        fragmentLoginBinding.run{
            buttonLogin.setOnClickListener {
                sellerActivity.replaceFragment(SellerActivity.ITEM_LIST_FRAGMENT, true, null)
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

            SellerRepository.getUserInfoByUserId(loginSellerId){
                if(it.result.exists() == false) {
                    val builder = MaterialAlertDialogBuilder(sellerActivity)
                    builder.setTitle("로그인 오류")
                    builder.setMessage("존재하지 않는 아이디 입니다")
                    builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                        textInputEditTextLoginUserId.setText("")
                        textInputEditTextLoginUserPw.setText("")
                        sellerActivity.showSoftInput(textInputEditTextLoginUserId)
                    }
                    builder.show()
                }
                else {
                    for(c1 in it.result.children){
                        // 가져온 데이터에서 비밀번호를 가져온다.
                        val pw = c1.child("pw").value as String

                        // 입력한 비밀번호와 현재 계정의 비밀번호가 다르다면
                        if(loginSellerPw != pw){
                            val builder = MaterialAlertDialogBuilder(sellerActivity)
                            builder.setTitle("로그인 오류")
                            builder.setMessage("잘못된 비밀번호 입니다")
                            builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                textInputEditTextLoginUserPw.setText("")
                                sellerActivity.showSoftInput(textInputEditTextLoginUserPw)
                            }
                            builder.show()
                        }
                        // 입력한 비밀번호와 현재 계정의 비밀번호가 같다면
                        else {
                            // 로그인한 사용자 정보를 가져온다.
                            val idx = c1.child("idx").value as String
                            val email = c1.child("email").value as String
                            val pw = c1.child("pw").value as String
                            val companyName = c1.child("companyName").value as String
                            val name = c1.child("name").value as String

                            sellerActivity.loginSellerClass = SellerClass(
                                idx,
                                companyName,
                                email,
                                name,
                                pw)
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
        }
    }
}