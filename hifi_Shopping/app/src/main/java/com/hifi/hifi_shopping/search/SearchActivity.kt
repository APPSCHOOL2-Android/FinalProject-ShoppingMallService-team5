package com.hifi.hifi_shopping.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivitySearchBinding
import com.hifi.hifi_shopping.search.model.SearchData
import com.hifi.hifi_shopping.user.model.UserDataClass
import kotlin.concurrent.thread

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding

    lateinit var searchViewModel: SearchViewModel

    lateinit var userDataClass: UserDataClass

    lateinit var inputMethodManager: InputMethodManager

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentSearch) as NavHostFragment
        navController = navHostFragment.navController

        val receivedIntent = intent
        if (receivedIntent != null && receivedIntent.hasExtra("userEmail")) {
            val email = receivedIntent.getStringExtra("userEmail")!!
            val userIdx = receivedIntent.getStringExtra("userIdx")!!
            val userNickname = receivedIntent.getStringExtra("userNickname")!!
            val userPw = receivedIntent.getStringExtra("userPw")!!
            val userProfileImg = receivedIntent.getStringExtra("userProfileImg")!!
            val userVerify = receivedIntent.getStringExtra("userVerify")!!
            val userPhoneNum = receivedIntent.getStringExtra("userPhoneNum")!!
            val newUserData = UserDataClass(userIdx, email, userPw, userNickname,
                userVerify, userPhoneNum, userProfileImg)
            userDataClass = newUserData
        }

        binding.run {
            materialToolbarSearch.run {
                setNavigationOnClickListener {
                    if (navController.currentDestination?.id == R.id.searchResultFragment) {
                        navController.popBackStack()
                    } else {
                        finish()
                    }
                }
            }

            editTextSearchWord.requestFocus()
            thread {
                SystemClock.sleep(200)
                inputMethodManager.showSoftInput(editTextSearchWord, InputMethodManager.SHOW_IMPLICIT)
            }

            editTextSearchWord.run {
                setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus && navController.currentDestination?.id == R.id.searchResultFragment) {
                        navController.popBackStack()
                    }
                }

                setOnEditorActionListener { v, actionId, event ->
                    if (text.toString().isNotEmpty()) {
                        actionSearch(editTextSearchWord)
                    } else {
                        Snackbar.make(root, "검색어를 입력하고 다시 시도해주세요.", Snackbar.LENGTH_SHORT).show()
                    }
                    true
                }
            }

            imageViewSearchSubmit.setOnClickListener {
                if (navController.currentDestination?.id == R.id.searchResultFragment) {
                    return@setOnClickListener
                }

                if (editTextSearchWord.text.toString().isNotEmpty()) {
                    actionSearch(editTextSearchWord)
                } else {
                    Snackbar.make(root, "검색어를 입력하고 다시 시도해주세요.", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun actionSearch(editText: EditText) {
        editText.run {
            navController.navigate(R.id.searchResultFragment)
            clearFocus()
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)

            searchViewModel.addSearchWord(
                SearchData(
                    userDataClass.idx,
                    text.toString(),
                    System.currentTimeMillis().toString()
                )
            )
        }
    }

    fun actionSearchRecentWord(word: String) {
        binding.editTextSearchWord.setText(word)

        actionSearch(binding.editTextSearchWord)
    }
}