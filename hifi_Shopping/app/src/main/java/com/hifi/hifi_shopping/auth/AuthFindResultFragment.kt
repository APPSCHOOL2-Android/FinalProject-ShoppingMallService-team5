package com.hifi.hifi_shopping.auth

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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentAuthFindResultBinding

class AuthFindResultFragment : Fragment() {

    private lateinit var fragmentAuthFindResultBinding: FragmentAuthFindResultBinding
    private lateinit var authActivity: AuthActivity
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAuthFindResultBinding = FragmentAuthFindResultBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val nickname = arguments?.getString("nickname")
        if (nickname == null) {
            return fragmentAuthFindResultBinding.root
        }

        fragmentAuthFindResultBinding.buttonFindResultCheck.setOnClickListener {
            val newPassword = fragmentAuthFindResultBinding.editTextFindResultPw.text.toString()
            val newPasswordCheck = fragmentAuthFindResultBinding.editTextFindResultPwCheck.text.toString()

            if (newPassword.isNotEmpty() && newPassword == newPasswordCheck) {
                performPasswordChange(nickname, newPassword)
            } else {
                val errorMessage = "비밀번호와 비밀번호 확인이 일치하지 않습니다."
                showErrorMessageDialog(errorMessage)
            }
        }

        return fragmentAuthFindResultBinding.root
    }

    private fun performPasswordChange(nickname: String, newPassword: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("UserData")
        val query: Query = databaseReference.orderByChild("nickname").equalTo(nickname)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val userReference = snapshot.ref
                        userReference.child("pw").setValue(newPassword)
                        showPasswordChangeSuccessDialog()
                    }
                } else {
                    val errorMessage = "해당 계정을 찾을 수 없습니다."
                    showErrorMessageDialog(errorMessage)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val errorMessage = "데이터베이스 오류가 발생했습니다."
                showErrorMessageDialog(errorMessage)
            }
        })
    }

    private fun showPasswordChangeSuccessDialog() {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_password_change_complete, null)
        val alertDialogBuilder = AlertDialog.Builder(requireContext()).setView(view)

        val alertDialog = alertDialogBuilder.create()
        val buttonDialogDismiss = view.findViewById<Button>(R.id.buttonDialogDismiss)

        buttonDialogDismiss.setOnClickListener {
            alertDialog.dismiss()
            authActivity.replaceFragment(AuthActivity.AUTH_LOGIN_FRAGMENT, true, null)
        }

        alertDialog.show()
    }

    private fun showErrorMessageDialog(message: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("오류")
            .setMessage(message)
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}