package com.hifi.hifi_shopping.auth

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.R

class GoogleSignInHelper(private val activity: Activity, private val auth: FirebaseAuth) {

    companion object {
        const val RC_GOOGLE_SIGN_IN = 9001
    }

    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        if (activity is AuthActivity) {
            activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(activity, "구글 로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    val userId = user?.uid
                    if (userId != null) {
                        val userData = UserDataClass(
                            idx = userId,
                            email = user.email ?: "",
                            pw = "", // 구글 로그인 시 비밀번호는 없을 것으로 가정
                            nickname = user.displayName ?: "",
                            verify = "false", // 초기 가입 시 본인 인증 여부는 false
                            phoneNum = "", // 전화번호는 초기값으로 설정
                            profileImg = "" // 프로필 이미지는 초기값으로 설정
                        )

                        val database = FirebaseDatabase.getInstance()
                        val usersReference = database.getReference("users")

                        usersReference.child(userId).setValue(userData)
                            .addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    // 사용자 정보 추가 성공 후 프래그먼트를 교체하는 코드
                                    if (activity is AuthActivity) {
                                        activity.replaceFragment(
                                            AuthActivity.AUTH_JOIN_FRAGMENT,
                                            true,
                                            null
                                        )
                                    }
                                } else {
                                    Toast.makeText(activity, "사용자 정보 추가 실패", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(activity, "구글 로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

}