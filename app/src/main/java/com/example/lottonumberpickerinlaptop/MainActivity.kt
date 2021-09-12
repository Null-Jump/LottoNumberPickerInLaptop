package com.example.lottonumberpickerinlaptop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val clearButton: Button by lazy {
        findViewById(R.id.clear_button)
    }

    private val addButton: Button by lazy {
        findViewById(R.id.add_button)
    }

    private val runButton: Button by lazy {
        findViewById(R.id.run_button)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.number_picker)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.textView1),
            findViewById(R.id.textView2),
            findViewById(R.id.textView3),
            findViewById(R.id.textView4),
            findViewById(R.id.textView5),
            findViewById(R.id.textView6)
        )
    } //LinearLayout 에 담긴 TextView 초기화를 한 list 에 담아서 관리

    private var didRun = false //runButton 을 사용여부를 확인하기 위한 스위치값

    private val pickNumberSet = hashSetOf<Int>() //선택된 번호를 담는 Collection 으로 중복된 값을 막기위해 Set 을 사용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //numberPicker 의 최솟값과 최댓값을 설정
        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        //각 버트들의 클릭이벤트 설정
        initRunButton()
        initAddButton()
        initClearButton()
    }

    /**
     * RunButton 의 클릭이벤트
     * list 를 생성해서 getRandomNumber() 을 사용하고
     * LinearLayout 에 담긴 textView 들에 백그라운드를 지정하는 메소드
     */
    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber()

            didRun = true

            //.forEachIndexed -> list 의 각각 index 에 number 값을 가져와서 함수를 실행
            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]

                textView.text =
                    number.toString() //numberTextViewList[index]에 담긴 값(number)을 textView 로 설정
                textView.isVisible = true

                setNumberBackground(number, textView) //number 값에 따른 textView 색상을 설정하는 함수
            }
        }
    }

    /**
     * list 를 생성하여 1부터 45까지의 숫자를 list 에 담고
     * 랜덤으로 섞은뒤 1~6번째의 숫자값을 반환하는 메소드
     */
    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>()
            //apply : apply 를 사용했던 객체 자체를 this로 블록에서 사용하기때문에 주로 초기화나 함수를 사용하여 데이터를 미리 넣을때 사용
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)) {
                        continue
                    }

                    this.add(i)
                }
            }
        //.shuffle() -> list 값을 임의로 섞음
        numberList.shuffle()

        //.toList() -> list 에 담긴 모든 값을 반환   /   .subList() -> list 에서 formIndex(포함)와 toIndex(제외) 사이의 값들을 반환
        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)

        //.sorted() -> List 에 담긴 값을 오름차순으로 정렬
        return newList.sorted()
    }

    /**
     * 설정된 숫자에 따라 textView 의 바탕색을 바꾸는 메소드
     * runButton 과 addButton 둘다 들어가는 코드 내용이라 따로 함수로 만들어서 출력
     */
    private fun setNumberBackground(number: Int, textView: TextView) {
        /**
         * 자바와 달리 case 대신 -> 을 사용함
         * return 을 사용하지 않고 바로 결과값이 when 의 return 형으로 바로 사용됨 (expression)
         * in 이라는 범위연산자를 통해서 해당 범위의 값인지 확인.
         */
        when (number) {
            in 1..9 ->
                textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 10..19 ->
                textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 20..29 ->
                textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 30..39 ->
                textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else ->
                textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }

    /**
     * addButton 초기화 메소드
     * 모든 예외처리에 문제가 없다면, textView 에 번호와 번호에 따른 색상을 추가하여 보여줌
     */
    private fun initAddButton() {
        addButton.setOnClickListener {
            if (didRun) { //addButton 을 한 뒤에 초기화가 되었는지 확인하기 위한 예외처리
                Toast.makeText(this, "Please Try after clearing", Toast.LENGTH_LONG).show()
                return@setOnClickListener //initAddButton()함수로 return 할지 setOnClickListener 로 리턴할지 명확히 하기 위해 @setOnClickListener 를 붙임
            }

            if (pickNumberSet.size >= 5) { //pickNumberSet 리스트의 갯수가 6개 이상 추가 못 하게 하기 위한 예외처리
                Toast.makeText(this, "You can only choose up to 5 numbers", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener //initAddButton()함수로 return 할지 setOnClickListener 로 리턴할지 명확히 하기 위해 @setOnClickListener 를 붙임
            }

            if (pickNumberSet.contains(numberPicker.value)) { //이미 추가한 숫자를 중복 추가를 막기 위한 예외처리
                Toast.makeText(this, "It's already chosen number", Toast.LENGTH_LONG).show()
                return@setOnClickListener //initAddButton()함수로 return 할지 setOnClickListener 로 리턴할지 명확히 하기 위해 @setOnClickListener 를 붙임
            }

            val textView = numberTextViewList[pickNumberSet.size]//List 에 담은 textView 중 pickNumberSet 에 담긴 사이즈를 통해 textView 값을 초기화
            textView.text = numberPicker.value.toString()
            textView.isVisible = true

            setNumberBackground(numberPicker.value, textView)//NumberPicker 에서 선택한 숫자에 따른 textView 색상 선택

            pickNumberSet.add(numberPicker.value)//pickNumberSet 에 현재 추가된 숫자를 추가
        }
    }

    /**
     * clearButton 초기화 메소드
     * 현재까지 선택한 숫자들을 list 에서 지우기 위한 메소드
     */
    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear() //현재까지 선택된 숫자들을 지움

            //.forEach{} -> for 문 처럼 범위를 정할 필요 없이 해당 list 의 사이즈 만큼 자동으로 반복함 (list 값이 많아질 수 록 for 문 보다 연산 비용이 저렴하다
            numberTextViewList.forEach {
                it.isVisible = false //각 list 의 visible 을 안 보이게 설정
            }

            didRun = false //runButton 의 스위치 값도 false 로 설정 
        }
    }
}