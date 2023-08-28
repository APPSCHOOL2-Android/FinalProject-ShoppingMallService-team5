package com.hifi.hifi_shopping.auth

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.auth.vm.AuthViewModel
import com.hifi.hifi_shopping.databinding.FragmentAuthJoinBinding
import java.io.ByteArrayOutputStream

class AuthJoinFragment : Fragment() {
    private lateinit var fragmentAuthJoinBinding: FragmentAuthJoinBinding
    private lateinit var authActivity: AuthActivity
    private lateinit var authViewModel: AuthViewModel
    private val firebaseDatabase = FirebaseDatabase.getInstance() // Firebase Database 인스턴스 생성

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        fragmentAuthJoinBinding = FragmentAuthJoinBinding.inflate(inflater)
        authActivity = activity as AuthActivity
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        fragmentAuthJoinBinding.run {
            toolbarAuthJoin.setNavigationOnClickListener {
                authActivity.removeFragment(AuthActivity.AUTH_JOIN_FRAGMENT)
            }

            buttonJoinCheck.setOnClickListener {
                val email = editTextJoinUserId.text.toString()
                val password = editTextJoinUserPw.text.toString()
                val nickname = editTextJoinUserNickName.text.toString()
                val passwordCheck = editTextJoinUserPwCheck.text.toString()

                // 경고 메시지 초기화
                warningJoinEmailFormat.visibility = View.GONE
                warningJoinPassword.visibility = View.GONE
                warningJoinPasswordCheck.visibility = View.GONE
                warningJoinNicknameFormat.visibility = View.GONE
                warningJoinEmailAlready.visibility = View.GONE
                warningJoinNicknameAlready.visibility = View.GONE

                // 예외처리
                if (!authViewModel.isEmailValid(email)) {
                    warningJoinEmailFormat.visibility = View.VISIBLE
                    return@setOnClickListener
                } else {
                    warningJoinEmailFormat.visibility = View.GONE
                }

                if (!authViewModel.isPasswordValid(password)) {
                    warningJoinPassword.visibility = View.VISIBLE
                    return@setOnClickListener
                } else {
                    warningJoinPassword.visibility = View.GONE
                }

                if (password != passwordCheck) {
                    warningJoinPasswordCheck.visibility = View.VISIBLE
                    return@setOnClickListener
                } else {
                    warningJoinPasswordCheck.visibility = View.GONE
                }

                if (!authViewModel.isNicknameValid(nickname)) {
                    warningJoinNicknameFormat.visibility = View.VISIBLE
                    return@setOnClickListener
                } else {
                    warningJoinNicknameFormat.visibility = View.GONE
                }

                // 이메일 중복 검사
                val databaseReference = firebaseDatabase.getReference("UserData")
                databaseReference.orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                // 이미 등록된 이메일이 있을 경우
                                warningJoinEmailAlready.visibility = View.VISIBLE
                                return
                            } else {
                                warningJoinEmailAlready.visibility = View.GONE

                                // 닉네임 중복 검사
                                databaseReference.orderByChild("nickname").equalTo(nickname)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                // 이미 등록된 닉네임이 있을 경우
                                                warningJoinNicknameAlready.visibility = View.VISIBLE
                                            } else {
                                                warningJoinNicknameAlready.visibility = View.GONE

                                                // Repository를 활용하여 데이터 처리
                                                val imageByteArray = loadYourImageAsByteArray()
                                                authViewModel.registerUserData(
                                                    email,
                                                    password,
                                                    nickname,
                                                    ""
                                                )
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            // 에러 처리
                                            showErrorMessageDialog("연결에 문제가 발생했습니다.")
                                        }
                                    })
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // 에러 처리
                            showErrorMessageDialog("연결에 문제가 발생했습니다.")
                        }
                    })
            }

            // LiveData를 옵저빙하여 결과 처리
            authViewModel.registrationResult.observe(
                viewLifecycleOwner,
                Observer { registrationSuccess ->
                    if (registrationSuccess) {
                        showRegistrationSuccessDialog()
                        // 가입 성공 시 화면 이동 로직 추가
                        authActivity.replaceFragment(AuthActivity.AUTH_LOGIN_FRAGMENT, true, null)
                    } else {
                        showErrorMessageDialog("가입 실패")
                    }
                })

            return fragmentAuthJoinBinding.root
        }
    }

    // 내 이미지를 바이트로 변환하는 함수
    private fun loadYourImageAsByteArray(): ByteArray {
        val imageResource = R.drawable.sample_img
        val imageBitmap = BitmapFactory.decodeResource(resources, imageResource)

        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    // 가입 성공 다이얼로그를 보여주는 함수
    private fun showRegistrationSuccessDialog() {
        val view =
            requireActivity().layoutInflater.inflate(R.layout.dialog_join_success_message, null)
        val alertDialogBuilder = AlertDialog.Builder(requireContext()).setView(view)
        val alertDialog = alertDialogBuilder.create()
        val buttonDialogDismiss = view.findViewById<Button>(R.id.buttonDialogDismiss)

        buttonDialogDismiss.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    // 오류 처리 다이얼로그를 보여주는 함수
    private fun showErrorMessageDialog(message: String) {
        val alertDialogBuilder =
            AlertDialog.Builder(requireContext()).setTitle("오류").setMessage(message)
                .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}