package com.hifi.hifi_shopping_sales.seller

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.icu.lang.UCharacter.VerticalOrientation
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hifi.hifi_shopping_sales.R
import com.hifi.hifi_shopping_sales.databinding.FragmentAddItemBinding
import com.hifi.hifi_shopping_sales.databinding.RowItemBinding
import com.hifi.hifi_shopping_sales.databinding.RowProductCategoryBinding
import java.util.UUID

class AddItemFragment : Fragment(){
    var categoryNum:Int = 0
    val categoryData = mapOf(
        0 to arrayOf("준비", "바디케어", "취미", "연애"),
        //1차
        1 to arrayOf("욕실", "얼굴", "헤어 세팅", "향기"),
        2 to arrayOf("피부관리", "제모", "운동", "건강 기능 식품"),
        3 to arrayOf("PC", "스포츠", "자동차", "집들이"),
        4 to arrayOf("선물", "데이트"),
        //준비 하위
        11 to arrayOf("면도", "클렌징폼", "바디워시", "헤어케어"),
        12 to arrayOf("피부관리", "메이크업", "립", "눈,코털"),
        13 to arrayOf("왁스", "스프레이", "에선스", "고대기",
            "헤어 토닉", "드라이기"),
        14 to arrayOf("향수", "탈취제", "섬유 유연제", "핸드크림"),
        // 바디 케어 하위
        21 to arrayOf("마스크팩", "로션", "토너", "올인원",
            "선크림", "스킨", "수분크림", "클렌징폼"),
        22 to arrayOf("눈썹", "코털", "잔털", "수염"),
        23 to arrayOf("런닝", "홈트", "보호대", "운동복", "보충제"),
        24 to arrayOf("유산균", "비타민", "홍삼", "오메가3",
            "마크네슘", "종합영양제"),
        // 취미 하위
        31 to arrayOf("마우스", "키보드", "헤드셋", "스피커",
            "패드", "의자"),
        32 to arrayOf("배드민턴", "골프", "낚시", "볼링",
            "자전거", "등산"),
        33 to arrayOf("인테리어", "익스테리어", "세차/카케어", "차량용 전자기기"),
        34 to arrayOf("거치대", "무드등", "의자", "키털트",
            "쿠션", "슬리퍼"),
        // 연애 하위
        41 to arrayOf("기념일용", "평상시 선물", "커플용품"),
        42 to arrayOf("소지품", "메이크업", "헤어 세팅", "눈썹, 코털"),
        // 욕실 하위
        111 to arrayOf("면도기", "전기 면도기", "쉐이빙폼", "면도 날",
            "제모"),
        112 to arrayOf("지성", "건성", "지+건성", "약산성",
            "여드름", "거품형"),
        113 to arrayOf("하위 카테고리 없음"),
        114 to arrayOf("트리트먼트", "린스", "트윈원 샴푸", "탈모 샴푸",
            "샴푸", "쿨링샴푸"),
        // 얼굴 하위
        121 to arrayOf("로션", "토너", "올인원",
            "선크림", "스킨", "수분크림"),
        122 to arrayOf("비비크림", "컨실러", "파운데이션", "아이브로우"
            , "쿠션"),
        123 to arrayOf("유색", "무색"),
        124 to arrayOf("눈썹칼", "코털제거", "브로우 펜슬", "눈썹 가위"),
        // 헤어 세팅 하위
        131 to arrayOf("소프트", "노말", "하드", "슈퍼하드"),
        132 to arrayOf("액상타입", "가스타입"),
        133 to arrayOf("NONE"),
        134 to arrayOf("NONE"),
        135 to arrayOf("NONE"),
        136 to arrayOf("NONE"),
        // 향기 하위
        141 to arrayOf("NONE"),
        142 to arrayOf("NONE"),
        143 to arrayOf("NONE"),
        144 to arrayOf("NONE"),
        // 파부 관리 하위
        211 to arrayOf("NONE"),
        212 to arrayOf("NONE"),
        213 to arrayOf("NONE"),
        214 to arrayOf("NONE"),
        215 to arrayOf("NONE"),
        216 to arrayOf("NONE"),
        217 to arrayOf("NONE"),
        218 to arrayOf("NONE"),
        // 제모 하위
        221 to arrayOf("눈썹칼", "눈썹 가위", "브로우 펜슬"),
        222 to arrayOf("코털제거"),
        223 to arrayOf("제모기", "제모 크림"),
        224 to arrayOf("면도기", "전기 면도기", "쉐이빙폼", "면도 날",
            "수염관리", "에프터 쉐이빙"),
        // 운동 하위
        231 to arrayOf("런닝화"),
        232 to arrayOf("매트", "덤벨", "바벨", "각종기구"),
        233 to arrayOf("스트랩", "허리", "다리", "무릎"),
        234 to arrayOf("기능성 티", "기능성 반바지"),
        235 to arrayOf("NONE"),
        // 건강 기능 식품 하위
        241 to arrayOf("NONE"),
        242 to arrayOf("A", "B", "C", "D"),
        243 to arrayOf("NONE"),
        244 to arrayOf("NONE"),
        245 to arrayOf("NONE"),
        246 to arrayOf("NONE"),
        // PC 하위
        311 to arrayOf("유션", "무선"),
        312 to arrayOf("유션", "무선"),
        313 to arrayOf("마이크 유", "마이크 무"),
        314 to arrayOf("NONE"),
        315 to arrayOf("롱", "숏"),
        316 to arrayOf("NONE"),
        // 스포츠 하위
        321 to arrayOf("라켓", "셔틀콕", "가방", "의류",
            "신발", "그립", "아대"),
        322 to arrayOf("라켓", "골프공", "가방", "의류",
            "신발", "악세사리"),
        323 to arrayOf("낚시대", "그 외 용품"),
        324 to arrayOf("아대", "볼링화", "볼링공"),
        325 to arrayOf("헬멧", "자전거백", "글러브", "쿨토시"),
        326 to arrayOf("등산스틱", "그 외 용품"),
        // 자동차 하위
        331 to arrayOf("디퓨저", "쿠션", "핸들용품", "카매트"
            ,"차박매트", "통풍&온열시트", "햇빛 가리개"),
        332 to arrayOf("스티커엠블럼", "문콕방지몰딩", "미러용품"),
        333 to arrayOf("브러시", "버킷", "세차용품세트", "각종수건"),
        334 to arrayOf("무선충전거치대", "블랙박스", "하이패스", "거치대",
            "HUD", "청소기", "공기청청기"),
        // 집돌이 하위
        341 to arrayOf("일반 거치대", "침대 거치대"),
        342 to arrayOf("NONE"),
        343 to arrayOf("NONE"),
        344 to arrayOf("퍼즐", "레고", "건담", "피큐어"),
        345 to arrayOf("바디필로우", "대쿠션"),
        346 to arrayOf("NONE"),
        // 선물 하위
        411 to arrayOf("악세사리", "전자제품", "같이 선물", "커플용품"),
        412 to arrayOf("소모품", "간식", "텀블러", "인형"),
        413 to arrayOf("신발", "커플티", "휴대폰 케이스", "잠옷", "팔찌"),
        // 데이트 하위
        421 to arrayOf("물티슈", "미니백", "향수", "핸드크림",
            "기름종이", "립밤", "손수건", "칫솔", "탈취제"),
        422 to arrayOf("비비크림", "컨실러", "파운데이션", "아이브로우"
            , "쿠션", "립밤"),
        423 to arrayOf("왁스", "스프레이", "에선스", "고대기",
            "헤어 토닉", "드라이기"),
        424 to arrayOf("눈썹칼", "코털제거", "브로우 펜슬", "눈썹 가위"),
        // 면도 하위
        1115 to arrayOf("눈썹칼", "눈썹", "가위", "코털제거기",
            "제모크림"),
        // 헤드셋 하위
        3131 to arrayOf("유선", "무선"),
        3132 to arrayOf("유선", "무선"),
        // 기념일용 하위
        4111 to arrayOf("목걸이", "귀걸이", "시계", "팔찌",
            "반지", "지갑", "에코백/미니백"),
        4112 to arrayOf("카메라", "헤드셋", "에어팟", "애플워치/갤럭시 워치"),
        4113 to arrayOf("편지지/펜", "러쉬 바디미스트", "러쉬 입욕제", "러쉬 바디워시",
            "러쉬 비누", "텀블러", "인형", "포장"),
        4114 to arrayOf("신발", "커플티", "잠옷", "휴대폰 케이스",
            "팔찌"),
        // 평상시 선물 하위
        4121 to arrayOf("향수", "핸드크림", "디퓨저", "러쉬 바디미스트",
            "러쉬 입욕제", "러쉬 바디워시", "러쉬 비누"),
        // 헤어 세팅 하위
        4231 to arrayOf("소프트", "노말", "하드", "슈퍼하드"),
        4232 to arrayOf("액상타입", "가스타입"),
    )
    var previousCategory = Array(10){
        ""
    }
    var previousNum = 0

    lateinit var fragmentAddItemBinding: FragmentAddItemBinding
    lateinit var sellerActivity: SellerActivity

    // 업로드할 이미지의 Uri
    var titleUploadUri: Uri? = null
    var sub1UploadUri: Uri? = null
    var sub2UploadUri: Uri? = null
    var productInfoUploadUri: Uri? = null
    lateinit var titleAlbumLauncher: ActivityResultLauncher<Intent>
    lateinit var subAlbumLauncher1: ActivityResultLauncher<Intent>
    lateinit var subAlbumLauncher2: ActivityResultLauncher<Intent>
    lateinit var productInfoAlbumLauncher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddItemBinding = FragmentAddItemBinding.inflate(inflater)
        sellerActivity = activity as SellerActivity

        // 앨범 설정
        titleAlbumLauncher = albumSetting(fragmentAddItemBinding.titleImageView, "TITLE")
        subAlbumLauncher1 = albumSetting(fragmentAddItemBinding.subImageView1, "SUB1")
        subAlbumLauncher2 = albumSetting(fragmentAddItemBinding.subImageView2, "SUB2")
        productInfoAlbumLauncher = albumSetting(fragmentAddItemBinding.productInfoImageView, "PRODUCT_INFO")

        fragmentAddItemBinding.run{
            addItemToolbar.run{
                setNavigationOnClickListener {
                    sellerActivity.removeFragment(SellerActivity.ADD_ITEM_FRAGMENT)
                }
            }
            categoryListRecyclerView.run{
                adapter = CategoryRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            categoryBackBtn.setOnClickListener {
                if(previousNum > 0) {
                    previousNum--
                    categoryNum /= 10
                    currentCategoryTextView.text = previousCategory[previousNum]
                    categoryListRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
            titleImageView.setOnClickListener {
                clickAlbumLaunch(titleAlbumLauncher)
            }
            subImageView1.setOnClickListener {
                clickAlbumLaunch(subAlbumLauncher1)
            }
            subImageView2.setOnClickListener {
                clickAlbumLaunch(subAlbumLauncher2)
            }
            productInfoImageView.setOnClickListener {
                clickAlbumLaunch(productInfoAlbumLauncher)
            }
            addProductBtn.setOnClickListener {
                val name = textInputEditTextProductName.text.toString()
                val price = textInputEditTextProductPrice.text.toString()

                if(name.isEmpty()){
                    val builder = MaterialAlertDialogBuilder(sellerActivity)
                    builder.setTitle("제품명 입력 오류")
                    builder.setMessage("제품명을 입력해주세요")
                    builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                        sellerActivity.showSoftInput(textInputEditTextProductName)
                    }
                    builder.show()
                    return@setOnClickListener
                }

                if(price.isEmpty()){
                    val builder = MaterialAlertDialogBuilder(sellerActivity)
                    builder.setTitle("가격 입력 오류")
                    builder.setMessage("가격을 입력해주세요")
                    builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                        sellerActivity.showSoftInput(textInputEditTextProductName)
                    }
                    builder.show()
                    return@setOnClickListener
                }

                if(categoryNum == 0){
                    val builder = MaterialAlertDialogBuilder(sellerActivity)
                    builder.setTitle("카테고리 종류 선택 오류")
                    builder.setMessage("카테고리 종류를 선택해주세요")
                    builder.setPositiveButton("확인", null)
                    builder.show()
                    return@setOnClickListener
                }
                val idx = UUID.randomUUID().toString()
                val titleFileName = if(titleUploadUri == null){
                    "None"
                } else {
                    "image/img_${System.currentTimeMillis()}.jpg"
                }
                val sub1FileName = if(sub1UploadUri == null){
                    "None"
                } else {
                    "image/img_${System.currentTimeMillis()}.jpg"
                }
                val sub2FileName = if(sub2UploadUri == null){
                    "None"
                } else {
                    "image/img_${System.currentTimeMillis()}.jpg"
                }
                val productInfoFileName = if(productInfoUploadUri == null){
                    "None"
                } else {
                    "image/img_${System.currentTimeMillis()}.jpg"
                }
                Log.d("testaaa", "${idx}\n")
                Log.d("testaaa", "${name}\n")
                Log.d("testaaa", "${price}\n")
                Log.d("testaaa", "${categoryNum}\n")
                Log.d("testaaa", "${titleFileName}\n")
                Log.d("testaaa", "${sub1FileName}\n")
                Log.d("testaaa", "${sub2FileName}\n")
                Log.d("testaaa", "${productInfoFileName}\n")
            }
        }
        return fragmentAddItemBinding.root
    }

    inner class CategoryRecyclerViewAdapter:RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder>() {
        inner class CategoryViewHolder(rowProductCategoryBinding: RowProductCategoryBinding) :RecyclerView.ViewHolder(rowProductCategoryBinding.root){
            var categoryNameTextView:TextView

            init{
                categoryNameTextView = rowProductCategoryBinding.categoryNameTextView

                rowProductCategoryBinding.root.setOnClickListener {
                    previousCategory[previousNum] = fragmentAddItemBinding.currentCategoryTextView.text as String
                    fragmentAddItemBinding.currentCategoryTextView.text =
                        "${previousCategory[previousNum]} > ${categoryData[categoryNum]?.get(adapterPosition)}"
                    categoryNum *= 10
                    categoryNum += adapterPosition+1
                    previousNum++
                    fragmentAddItemBinding.categoryListRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val rowProductCategoryBinding = RowProductCategoryBinding.inflate(layoutInflater)
            val categoryViewHolder = CategoryViewHolder(rowProductCategoryBinding)

            rowProductCategoryBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            return categoryViewHolder
        }

        override fun getItemCount(): Int {

            return if(categoryData[categoryNum] == null){
                0
            }else {
                categoryData[categoryNum]?.size!!
            }
        }
        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.categoryNameTextView.text = categoryData[categoryNum]?.get(position)
        }
    }

    // 앨범 관련 설정
    fun albumSetting(previewImageView: ImageView, kind:String) : ActivityResultLauncher<Intent>{
        val albumContract = ActivityResultContracts.StartActivityForResult()
        val albumLauncher = registerForActivityResult(albumContract){

            if(it.resultCode == AppCompatActivity.RESULT_OK){
                // 선택한 이미지에 접근할 수 있는 Uri 객체를 추출한다.
                if(it.data?.data != null){
                    val uploadUri = it.data?.data
                    when(kind){
                        "TITLE" -> titleUploadUri = uploadUri
                        "SUB1" -> sub1UploadUri = uploadUri
                        "SUB2" -> sub2UploadUri = uploadUri
                        "PRODUCT_INFO" -> productInfoUploadUri = uploadUri
                    }
                    // 안드로이드 10 (Q) 이상이라면...
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        // 이미지를 생성할 수 있는 디코더를 생성한다.
                        val source = ImageDecoder.createSource(sellerActivity.contentResolver, uploadUri!!)
                        // Bitmap객체를 생성한다.
                        val bitmap = ImageDecoder.decodeBitmap(source)

                        previewImageView.setImageBitmap(bitmap)
                    } else {
                        // 컨텐츠 프로바이더를 통해 이미지 데이터 정보를 가져온다.
                        val cursor = sellerActivity.contentResolver.query(uploadUri!!, null, null, null, null)
                        if(cursor != null){
                            cursor.moveToNext()

                            // 이미지의 경로를 가져온다.
                            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                            val source = cursor.getString(idx)

                            // 이미지를 생성하여 보여준다.
                            val bitmap = BitmapFactory.decodeFile(source)
                            previewImageView.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }

        return albumLauncher
    }

    fun clickAlbumLaunch(albumLauncher:ActivityResultLauncher<Intent>){
        val newIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        newIntent.setType("image/*")
        val mimeType = arrayOf("image/*")
        newIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        albumLauncher.launch(newIntent)
    }
}