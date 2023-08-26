package com.hifi.hifi_shopping.subscribe.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.subscribe.SubscribeActivity
import com.hifi.hifi_shopping.subscribe.SubscribeClass
import com.hifi.hifi_shopping.subscribe.repository.SubscribeRepository
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class SubscribeViewModel() : ViewModel() {
    val followerIdx = MutableLiveData<String>()
    val loadingProfile = MutableLiveData<Bitmap>()
    val bitmapFilename = MutableLiveData<String>()
    val subscribe = MutableLiveData<SubscribeClass>()
    val subscribeList = MutableLiveData<MutableList<SubscribeClass>>()

    init {
        subscribeList.value = mutableListOf<SubscribeClass>()
    }

    fun getSubscribeAll(userIdx:String){
        SubscribeRepository.getSubscribeAllByUserIdx(userIdx){
            for(c1 in it.result.children) {
                followerIdx.value = c1.child("followerIdx").value as String
            }
        }
    }

    fun getProfileByUserIdx(userIdx:String){
        SubscribeRepository.getProfileByUserIdx(userIdx){
            for(c1 in it.result.children) {
                val filename = c1.child("profileImg").value as String
                val nickname = c1.child("nickname").value as String
                SubscribeRepository.getProfileImgByfilename(filename) {
                    thread {
                        var bitmap:Bitmap? = null
                        if(it.isSuccessful) {
                            // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                            val url = URL(it.result.toString())
                            // 접속한다.
                            val httpURLConnection = url.openConnection() as HttpURLConnection
                            // 이미지 객체를 생성한다.
                            bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
                        }
                        val newSubscribe = SubscribeClass(userIdx, nickname, bitmap)
                        subscribe.postValue(newSubscribe)
                    }
                }
            }
        }
    }
}