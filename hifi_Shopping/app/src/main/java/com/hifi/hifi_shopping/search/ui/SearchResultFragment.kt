package com.hifi.hifi_shopping.search.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.FragmentSearchResultBinding
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.search.SearchViewModel
import com.hifi.hifi_shopping.search.adapter.SearchResultListAdapter

class SearchResultFragment : Fragment() {

    lateinit var fragmentSearchResultBinding: FragmentSearchResultBinding
    lateinit var searchActivity: SearchActivity

    lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSearchResultBinding = FragmentSearchResultBinding.inflate(layoutInflater)
        searchActivity = activity as SearchActivity

        searchViewModel = ViewModelProvider(searchActivity)[SearchViewModel::class.java]

        val searchResultListAdapter = SearchResultListAdapter(searchViewModel) {
            val intent = Intent(searchActivity, BuyActivity::class.java)

            val buyProductList = ArrayList<String>()
            buyProductList.add(it)

            intent.putStringArrayListExtra("buyProduct", buyProductList)
            intent.putExtra("userEmail", searchActivity.userDataClass.email)
            intent.putExtra("userIdx", searchActivity.userDataClass.idx)
            intent.putExtra("userNickname", searchActivity.userDataClass.nickname)
            intent.putExtra("userPw", searchActivity.userDataClass.pw)
            intent.putExtra("userVerify", searchActivity.userDataClass.verify)
            intent.putExtra("userPhoneNum", searchActivity.userDataClass.phoneNum)
            intent.putExtra("userProfileImg", searchActivity.userDataClass.profileImg)

            startActivity(intent)
        }

        val searchWord = searchActivity.binding.editTextSearchWord.text.toString()

        searchViewModel.getSearchResult(searchWord)

        fragmentSearchResultBinding.run {
            recyclerViewSearchResultProduct.run {
                adapter = searchResultListAdapter
                layoutManager = GridLayoutManager(searchActivity, 3)
            }
        }

        searchViewModel.searchResultList.observe(viewLifecycleOwner) {
            fragmentSearchResultBinding.run {
                if (it.isNotEmpty()) {
                    linearLayoutNoSearchResult.visibility = View.INVISIBLE
                    recyclerViewSearchResultProduct.visibility = View.VISIBLE
                    textViewSearchResultProduct.visibility = View.VISIBLE
                } else {
                    linearLayoutNoSearchResult.visibility = View.VISIBLE
                    recyclerViewSearchResultProduct.visibility = View.INVISIBLE
                    textViewSearchResultProduct.visibility = View.INVISIBLE

                    val searchWord = "'${searchActivity.binding.editTextSearchWord.text}'"
                    val fullText = "${searchWord}에 대한 검색결과가 없습니다."
                    val coloredText = searchWord

                    // SpannableString 생성
                    val spannableString = SpannableString(fullText)

                    // 글자의 시작과 끝 인덱스 계산
                    val startIndex = fullText.indexOf(coloredText)
                    val endIndex = startIndex + coloredText.length

                    // ForegroundColorSpan을 사용하여 색상 변경
                    val colorSpan = ForegroundColorSpan(Color.parseColor("#6C3428"))
                    spannableString.setSpan(colorSpan, startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

                    // TextView에 SpannableString 적용
                    textViewNoSearchResult.text = spannableString
                }
            }
            searchResultListAdapter.submitList(it)
        }

        return fragmentSearchResultBinding.root
    }
}