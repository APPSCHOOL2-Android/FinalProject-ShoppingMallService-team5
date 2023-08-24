package com.hifi.hifi_shopping.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentAuthFindPwBinding

class AuthFindPwFragment : Fragment() {

    lateinit var fragmentAuthFindPwBinding: FragmentAuthFindPwBinding
    lateinit var authActivity: AuthActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthFindPwBinding = FragmentAuthFindPwBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        fragmentAuthFindPwBinding.toolbarAuthFindPw.setNavigationOnClickListener {
            authActivity.removeFragment(AuthActivity.AUTH_FIND_PW_FRAGMENT)
        }

        fragmentAuthFindPwBinding.buttonFindPwCheck.setOnClickListener {
            val nickname = fragmentAuthFindPwBinding.editTextFindPwNickname.text.toString()
            val number = fragmentAuthFindPwBinding.editTextFindPwNumber.text.toString()

            checkNicknameInFirebase(nickname, number)
        }

        return fragmentAuthFindPwBinding.root
    }

    private fun checkNicknameInFirebase(nickname: String, number: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("UserData")

        val query: Query = databaseReference.orderByChild("nickname").equalTo(nickname)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // 닉네임이 파이어베이스에 등록되어 있는 경우
                    val bundle = Bundle()
                    bundle.putString("nickname", nickname)
                    bundle.putString("number", number)

                    val authFindResultFragment = AuthFindResultFragment()
                    authFindResultFragment.arguments = bundle

                    authActivity.replaceFragment(AuthActivity.AUTH_FIND_RESULT_FRAGMENT, true, null
                    )
                } else {
                    // 닉네임이 파이어베이스에 등록되어 있지 않은 경우
                    showNicknameNotRegisteredDialog()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 조회 취소 시 동작
            }
        })
    }

    private fun showNicknameNotRegisteredDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_nickname_not_registered, null)
        val dialogButton = dialogView.findViewById<Button>(R.id.buttonDialogDismiss)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = alertDialogBuilder.create()
        dialogButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}

