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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.auth.model.UserDataClass
import com.hifi.hifi_shopping.databinding.FragmentAuthJoinBinding
import java.io.ByteArrayOutputStream
import java.util.UUID


class AuthJoinFragment : Fragment() {

    lateinit var fragmentAuthJoinBinding: FragmentAuthJoinBinding
    lateinit var authActivity: AuthActivity
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    // 닉네임 금지 특수기호
    val INVALID_NICKNAME_CHARACTERS = listOf(
        "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=", "[", "]", "{", "}",
        "|", "\\", ":", ";", "\"", "'", "<", ">", ",", ".", "/", "?"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        fragmentAuthJoinBinding = FragmentAuthJoinBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

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
                // 이메일 형식 검사
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    warningJoinEmailFormat.visibility = View.VISIBLE
                    return@setOnClickListener
                } else {
                    warningJoinEmailFormat.visibility = View.GONE
                }
                // 비밀번호 길이 검사
                if (password.length < 6) {
                    warningJoinPassword.visibility = View.VISIBLE
                    return@setOnClickListener
                } else {
                    warningJoinPassword.visibility = View.GONE
                }
                // 비밀번호 확인 검사
                if (password != passwordCheck) {
                    warningJoinPasswordCheck.visibility = View.VISIBLE
                    return@setOnClickListener
                } else {
                    warningJoinPasswordCheck.visibility = View.GONE
                }
                // 닉네임 길이 및 특수 기호 검사
                if (nickname.length < 2 || nickname.length > 12 || INVALID_NICKNAME_CHARACTERS.any {
                        nickname.contains(it)
                    }) {
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
                            }

                            // 닉네임 중복 검사
                            databaseReference.orderByChild("nickname").equalTo(nickname)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            // 이미 등록된 닉네임이 있을 경우
                                            warningJoinNicknameAlready.visibility = View.VISIBLE
                                            return
                                        } else {
                                            warningJoinNicknameAlready.visibility = View.GONE
                                        }

                                        // Firebase 인증을 통한 회원 가입
                                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val user = firebaseAuth.currentUser
                                                    val userId = user?.uid ?: ""
                                                    val phoneNum = ""

                                                    // 이미지 업로드
                                                    val imageByteArray = loadYourImageAsByteArray()
                                                    val storageReference: StorageReference = FirebaseStorage.getInstance().reference
                                                    val profileImgRef: StorageReference = storageReference.child("user/sample_img")
                                                    val uploadTask: UploadTask = profileImgRef.putBytes(imageByteArray)

                                                    uploadTask.addOnSuccessListener { taskSnapshot ->
                                                        profileImgRef.downloadUrl.addOnCompleteListener { urlTask ->
                                                            if (urlTask.isSuccessful) {
                                                                val imageUrl = ("user/sample_img")

                                                                val userData = UserDataClass(
                                                                    idx = UUID.randomUUID().toString(),
                                                                    email = email,
                                                                    pw = password,
                                                                    nickname = nickname,
                                                                    verify = "false", // 초기 가입 시 본인 인증 여부는 false
                                                                    phoneNum = phoneNum,
                                                                    profileImg = imageUrl // 이미지의 URL을 저장
                                                                )

                                                                // Realtime Database에 UserData 추가
                                                                val databaseReference = firebaseDatabase.getReference("UserData")
                                                                databaseReference.child(userId).setValue(userData)
                                                                    .addOnSuccessListener {
                                                                        // 가입 성공 시 다이얼로그를 띄움
                                                                        showRegistrationSuccessDialog()
                                                                    }.addOnFailureListener { exception ->
                                                                        // 가입 실패 처리
                                                                        val errorMessage = "연결 중 오류가 발생했습니다."
                                                                        showErrorMessageDialog(errorMessage)
                                                                    }
                                                            }
                                                        }
                                                    }.addOnFailureListener { exception ->
                                                        // 이미지 업로드 실패 처리
                                                        val errorMessage = "이미지 업로드 중 오류가 발생했습니다."
                                                        showErrorMessageDialog(errorMessage)
                                                    }
                                                } else {
                                                    // 회원 가입 실패 처리
                                                    val exception = task.exception
                                                    if (exception != null) {
                                                        val errorMessage = "연결 중 오류가 발생했습니다."
                                                        showErrorMessageDialog(errorMessage)
                                                    }
                                                }
                                            }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        val errorMessage = "연결 중 오류가 발생했습니다."
                                        showErrorMessageDialog(errorMessage)
                                    }
                                })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            val errorMessage = "연결 중 오류가 발생했습니다."
                            showErrorMessageDialog(errorMessage)
                        }
                    })
            }

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
            // 가입 성공 시 로그인 프래그먼트로 전환
            authActivity.replaceFragment(AuthActivity.AUTH_LOGIN_FRAGMENT, true, null)
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