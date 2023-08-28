
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.hifi_shopping.user.model.CartDataClass
import com.hifi.hifi_shopping.user.model.OrderDataClass

import com.hifi.hifi_shopping.user.model.ProductDataClass
import com.hifi.hifi_shopping.user.repository.CartRepository
import com.hifi.hifi_shopping.user.repository.OrderRepository
import com.hifi.hifi_shopping.user.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CartViewModel : ViewModel() {
    var cartProductList =  MutableLiveData<MutableList<ProductDataClass>>()
    var cartDataList =  MutableLiveData<MutableList<CartDataClass>>()

    fun getCartProductList(userIdx : String){
        val tempList = mutableListOf<CartDataClass>()
        val tempList2 = mutableListOf<ProductDataClass>()

        // 백그라운드 스레드에서 비동기 작업을 수행하기 위해 viewModelScope.launch를 사용
        viewModelScope.launch(Dispatchers.IO) {
            // 사용자의 주문 목록을 가져옴

            CartRepository.getCartAllByUserIdx(userIdx) {
                for (c1 in it.result.children) {
                    val userIdx = c1.child("userIdx").value as String
                    val productIdx = c1.child("productIdx").value as String

                    val data = CartDataClass(userIdx, productIdx)
                    tempList.add(data)
                }
                cartDataList.value= tempList

                cartDataList.value?.forEach{
                    // 제품 정보를 가져옴
                    ProductRepository.getProductInfoByIdx(it.productIdx){
                        for(c2 in it.result.children){
                            val category = c2.child("category").value as String
                            val context = c2.child("context").value as String
                            val idx = c2.child("idx").value as String
                            val name = c2.child("name").value as String
                            val pointAmount = c2.child("pointAmount").value as String
                            val price = c2.child("price").value as String
                            val sellerIdx = c2.child("sellerIdx").value as String

                            val data = ProductDataClass(category, context, idx, name, pointAmount, price, sellerIdx)
                            tempList2.add(data)
                        }

                        Log.d("제품테스트2", tempList2.toString())
                        cartProductList.value = tempList2
                    }
                }

            }

        }
    }
}


