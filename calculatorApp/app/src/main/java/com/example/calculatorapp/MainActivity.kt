package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.calculatorapp.databinding.ActivityMainBinding
import java.lang.StringBuilder
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val firstNumberText = StringBuilder("")
    private val secondNumberText = StringBuilder("")
    private val operatorText = StringBuilder("")
    private val decimalFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun numberClicked(view: View) {
        val numberString = (view as? Button)?.text.toString() ?: ""
        val numberText = if(operatorText.isEmpty()) firstNumberText else secondNumberText
        numberText.append(numberString)
        updateEquationTextView()
    }

    fun clearClicked(view: View) {
        firstNumberText.clear()
        secondNumberText.clear()
        operatorText.clear()
        updateEquationTextView()
        binding.resultTextView.text = ""
    }

    fun equalClicked(view: View) {
        if(firstNumberText.isEmpty() || secondNumberText.isEmpty() || operatorText.isEmpty()) {
            Toast.makeText(this, "올바르지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val firstNum = firstNumberText.toString().toBigDecimal()
        val secondNum = secondNumberText.toString().toBigDecimal()

        var result = when(operatorText.toString()) {
            "+" -> decimalFormat.format(firstNum + secondNum)
            "-" -> decimalFormat.format(firstNum - secondNum)
            else -> "올바르지 않은 수식입니다."
        }
        binding.resultTextView.text = result
    }

    fun operatorClicked(view: View) {
        val operatorString = (view as? Button)?.text.toString() ?: ""

        if(firstNumberText.isEmpty()) {
            Toast.makeText(this, "먼저 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if(secondNumberText.isNotEmpty()) {
            Toast.makeText(this, "한 개의 연산자만 연산이 가능합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        operatorText.append(operatorString)
        updateEquationTextView()
    }

    private fun updateEquationTextView() {
        val firstFormattedNUm = if(firstNumberText.isNotEmpty()) decimalFormat.format(firstNumberText.toString().toBigDecimal()) else ""
        val secondFormattedNUm = if(secondNumberText.isNotEmpty()) decimalFormat.format(secondNumberText.toString().toBigDecimal()) else ""
        binding.equationTextView.text = "$firstFormattedNUm $operatorText $secondFormattedNUm"
    }
}