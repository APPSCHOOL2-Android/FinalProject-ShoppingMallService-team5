package com.hifi.hifi_shopping.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.databinding.FragmentAuthJoinBinding
import java.util.UUID


class AuthJoinFragment : Fragment() {

    lateinit var fragmentAuthJoinBinding: FragmentAuthJoinBinding
    lateinit var authActivity: AuthActivity
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthJoinBinding = FragmentAuthJoinBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        // Initialize Firebase Authentication and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        fragmentAuthJoinBinding.run {
            // 네비게이션 아이콘을 클릭했을 때의 동작 설정
            toolbarAuthJoin.setNavigationOnClickListener {
                authActivity.removeFragment(AuthActivity.AUTH_JOIN_FRAGMENT)
            }

            // '완료' 버튼을 클릭했을 때의 동작 설정
            buttonJoinCheck.setOnClickListener {
                val email = editTextJoinUserId.text.toString()
                val password = editTextJoinUserPw.text.toString()
                val nickname = editTextJoinUserNickName.text.toString()

                // Firebase 인증을 통한 회원 가입
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val userId = user?.uid ?: ""
                            // 다른 브랜치에서 가져오는 로직 필요
                            val phoneNum = ""
                            // val profileImg = "프로필 이미지 경로"

                            // UserData 객체 생성
                            val userData = UserDataClass(
                                idx = UUID.randomUUID().toString(),
                                email = email,
                                pw = password,
                                nickname = nickname,
                                verify = "false", // 초기 가입 시 본인 인증 여부는 false
                                phoneNum = phoneNum,
                                profileImg = ""
                            )

                            // Realtime Database에 UserData 추가
                            val databaseReference = firebaseDatabase.getReference("users")
                            databaseReference.child(userId).setValue(userData)
                                .addOnSuccessListener {
                                    // 가입 성공 시 로그인 프래그먼트로 전환
                                    authActivity.replaceFragment(
                                        AuthActivity.AUTH_LOGIN_FRAGMENT,
                                        true,
                                        null
                                    )
                                }
                                .addOnFailureListener { exception ->
                                    // 가입 실패 처리
                                    val errorMessage = exception.message
                                    // 실패 메시지를 토스트로 표시
                                    Toast.makeText(
                                        requireContext(),
                                        "회원 가입 실패: $errorMessage",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            // 회원 가입 실패 처리
                            val exception = task.exception
                            if (exception != null) {
                                val errorMessage = exception.message
                                // 실패 메시지를 토스트로 표시
                                Toast.makeText(
                                    requireContext(), "회원 가입 실패: $errorMessage", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }

            return fragmentAuthJoinBinding.root
        }
    }
}