package com.hifi.hifi_shopping.user

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentEditUserBinding
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.model.AddressDataClass
import com.hifi.hifi_shopping.user.model.UserDataClass
import com.hifi.hifi_shopping.user.repository.AddressRepository
import com.hifi.hifi_shopping.user.repository.UserRepository
import com.hifi.hifi_shopping.user.vm.AddressViewModel
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class EditUserFragment : Fragment() {

    lateinit var fragmentEditUserBinding: FragmentEditUserBinding
    lateinit var userActivity : UserActivity
    lateinit var addressViewModel: AddressViewModel
    lateinit var userProfileImgLauncher: ActivityResultLauncher<Intent>
    var isToggled = false
    var isVerify = false
    var isNewImg = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentEditUserBinding = FragmentEditUserBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity

        val userTemp = userActivity.userTemp
        var userProfileImgsrc = userTemp.profileImg

        uploadProfileImg(userTemp)

        addressViewModel = ViewModelProvider(userActivity)[AddressViewModel::class.java]

        addressViewModel.run {
            addressDataList.observe(userActivity){
                val addr = addressDataList.value?.get(0)?.address?.split("/")
                fragmentEditUserBinding.editUserAddrEditText.setText(addr?.get(0))
                fragmentEditUserBinding.editUserAddrDetailEditText.setText(addr?.get(1))
            }
        }

        fragmentEditUserBinding.run {
            editUserToolbar.run {
                setNavigationOnClickListener {
                    userActivity.removeFragment(UserActivity.MY_PAGE_FRAGMENT)
                }

                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_search -> {
                            val intent = Intent(userActivity, SearchActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.menu_item_cart -> {
                            userActivity.replaceFragment(UserActivity.CART_FRAGMENT, true, null)
                        }
                    }
                    true
                }
            }

            // 프로필 사진
            editUserProfileImg.run {
                userActivity.getUserProfileImg(userTemp,this)
                setOnClickListener {
                    val newIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    newIntent.setType("image/*")
                    val mimeType = arrayOf("image/*")
                    newIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                    userProfileImgLauncher.launch(newIntent)
                }

            }
            // 프로필 닉네임
            editUserEditNick.run {
                setText(userTemp.nickname)
                if(text.isNullOrBlank()){
                    editUserEditNick.error = "필수 입력 요소"
                }
                userActivity.showSoftInput(this)
            }


            // 주소
            addressViewModel.getAddressListByUser(userTemp.idx)

            // 비밀번호 토글
            editUserToChangePwdBtnToggle.setOnClickListener {
                toggleButtonClick()
            }

            // 핸드폰 본인인증
            editUserPhoneBtnCheck.setOnClickListener {
                phoneCheck()

            }

            editUserPhoneBtnCheckVerify.setOnClickListener {
                phoneVerify()
            }

            // 비밀번호 변경
            editUserPwdEditTextSameCheck.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                        // 비밀번호 확인 메서드 호출
                        pwdSameCheck()
                        return true
                    }
                    return false
                }
            })

            editUserBtnComplete.setOnClickListener {
                checkInfoOk()
                //                userActivity.removeFragment(UserActivity.EDIT_USER_FRAGMENT)
            }



        }
        return fragmentEditUserBinding.root
    }

    fun toggleButtonClick() {
        isToggled = !isToggled
        if (isToggled) {
            // 토글이 활성화된 경우
            fragmentEditUserBinding.run {
                editUserToChangePwdBtnToggle.setImageResource(R.drawable.expand_less_24px)
                editUserPhone.visibility = View.VISIBLE
                if(isVerify){
                    editUserPwdEditTextChange.visibility = View.VISIBLE
                }
            }
        } else {
            // 토글이 비활성화된 경우
            fragmentEditUserBinding.run {
                editUserToChangePwdBtnToggle.setImageResource(R.drawable.expand_more_24px)
                editUserPhone.visibility = View.GONE
                if(isVerify){
                    editUserPwdEditTextChange.visibility = View.GONE
                }
            }
        }
    }

    fun phoneCheck() {
        fragmentEditUserBinding.run {
            if(editUserPhoneEditText.text.toString().isEmpty()){
                val builder = MaterialAlertDialogBuilder(userActivity)
                builder.setTitle("전화번호 오류")
                builder.setMessage("전화번호를 입력해주세요.")
                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    userActivity.showSoftInput(editUserPhoneEditText)
                }
                builder.show()
                return
            }else {
                userActivity.hideKeyboard(editUserPhoneEditText)
                editUserPhoneEditTextVerify.setText("1312")
            }

        }

    }

    fun phoneVerify(){
        fragmentEditUserBinding.run {
            val verifyText = editUserPhoneEditTextVerify.text.toString()
            Log.d("휴대폰",verifyText.toString())
            if(verifyText == "1312"){
                isVerify=true
                editUserPwdEditTextChange.visibility = View.VISIBLE
            }
            if(isVerify){
                editUserPhoneEditText.isEnabled = false
                editUserPhoneBtnCheck.text = "인증 완료"
                editUserPhoneBtnCheck.isClickable = false
                userActivity.hideKeyboard(editUserPhoneEditText)

                editUserPhoneEditTextVerify.visibility = View.GONE
                editUserPhoneBtnCheckVerify.visibility = View.GONE
            }
        }
    }

    fun pwdSameCheck(){
        fragmentEditUserBinding.run {
            val newPwd = editUserPwdEditTextNew.text.toString()
            val checkNewPwd = editUserPwdEditTextSameCheck.text.toString()
            if(newPwd != checkNewPwd){
                editUserPwdEditTextSameCheck.error ="비밀번호가 일치하지 않습니다."
            }else{
                userActivity.hideKeyboard(editUserPwdEditTextSameCheck)
            }
        }
    }

    fun uploadProfileImg(userTemp:UserDataClass){
        val contract1 = ActivityResultContracts.StartActivityForResult()
        userProfileImgLauncher = registerForActivityResult(contract1){
            if(it?.resultCode == AppCompatActivity.RESULT_OK){
                val storage = FirebaseStorage.getInstance()
                val fileName = "user/${userTemp.idx}"

                // 파일에 접근할 수 있는 객체를 가져온다.
                val fileRef = storage.reference.child(fileName)
                // 파일을 업로드한다.
                isNewImg = true

                fileRef.putFile(it.data?.data!!).addOnCompleteListener{  uploadTask ->
                    if (uploadTask.isSuccessful) {
                        fileRef.downloadUrl.addOnCompleteListener { downloadTask ->
                            thread {
                                if (downloadTask.isSuccessful) {
                                    val url = URL(downloadTask.result.toString())
                                    val httpURLConnection = url.openConnection() as HttpURLConnection
                                    val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
                                    userActivity.runOnUiThread {
                                        fragmentEditUserBinding.editUserProfileImg.setImageBitmap(bitmap)
                                    }
                                } else {
                                    Snackbar.make(fragmentEditUserBinding.root, "이미지 다운로드에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                        Snackbar.make(fragmentEditUserBinding.root, "프로필 이미지 변경이 완료되었습니다.", Snackbar.LENGTH_SHORT).show()
                        userTemp.profileImg = userTemp.idx
                    } else {
                        Snackbar.make(fragmentEditUserBinding.root, "이미지 업로드를 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
                    }

                }

            }
        }
    }

    fun checkInfoOk(){
        fragmentEditUserBinding.run {
            val idx = userActivity.userTemp.idx

            val email = userActivity.userTemp.idx

            var pw = if(editUserPwdEditTextSameCheck.text.isNullOrBlank()||editUserPwdEditTextSameCheck.error.isNotBlank()){

                userActivity.userTemp.pw

            }else{
                editUserPwdEditTextSameCheck.text.toString()
            }

            var nickname = if(editUserEditNick.error.isNullOrBlank()){
                editUserEditNick.text.toString()
            } else{
                userActivity.userTemp.nickname
            }

            var verify = if(isVerify){
                isVerify.toString()
            }else{
                userActivity.userTemp.verify
            }

            var phoneNum = if(isVerify){
                userActivity.userTemp.profileImg
            }else{
                userActivity.userTemp.phoneNum
            }

            var profileImg = if(isNewImg){
                if(userActivity.userTemp.profileImg.isNullOrBlank()){
                    userActivity.userTemp.idx
                } else {
                    userActivity.userTemp.profileImg
                }
            }else{
                userActivity.userTemp.profileImg

            }

            val u = UserDataClass(idx, email, pw, nickname, verify, phoneNum, profileImg)

            val addr = addressViewModel.addressDataList.value?.get(0)
            val address = if(editUserAddrDetailEditText.text.isNullOrBlank()||editUserAddrEditText.text.isNullOrBlank()){
                addr!!.address
            }else{
                "${editUserAddrEditText.text}/${editUserAddrDetailEditText.text}"
            }

            val newAddr=AddressDataClass(addr!!.idx,addr!!.userIdx,addr!!.receiver,addr!!.receiverPhoneNum,address, addr!!.context)
            var userModifyOk = false
            var addressModifyOk = false
            UserRepository.modifyUserInfo(u){

                if(it.isSuccessful){
                    userModifyOk = true
                }

            }

            AddressRepository.modifyAddressInfo(newAddr){
                if(it.isSuccessful){
                    addressModifyOk = true
                }
            }

            if(userModifyOk&&addressModifyOk){
                Snackbar.make(fragmentEditUserBinding.root, "정보 수정이 완료되었습니다.", Snackbar.LENGTH_SHORT).show()
            }




        }
    }


}