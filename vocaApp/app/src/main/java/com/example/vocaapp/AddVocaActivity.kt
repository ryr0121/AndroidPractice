package com.example.vocaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.example.vocaapp.databinding.ActivityAddVocaBinding
import com.google.android.material.chip.Chip

class AddVocaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddVocaBinding
    private var originVoca: Voca? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVocaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        binding.addButton.setOnClickListener {
            if(originVoca == null) add() else edit()
        }
    }

    private fun initViews() {
        val types = listOf("명사", "동사", "형용사", "대명사", "부사", "감탄사", "전치사", "접속사")
        binding.typeChipGroup.apply {
            types.forEach { text ->
                addView(createChip(text))
            }
        }

        binding.textInputEditText.addTextChangedListener {
            it?.let {text ->
                binding.textTextInputLayout.error = when(text.length) {
                    0 -> "값을 입력해주세요"
                    1 -> "2자 이상을 입력해주세요"
                    else -> null
                }
            }
        }

        originVoca = intent.getParcelableExtra("originVoca")
        originVoca?.let { voca ->
            binding.textInputEditText.setText(voca.text)
            binding.meanInputEditText.setText(voca.mean)
            val selectedChip = binding.typeChipGroup.children.firstOrNull(){ (it as Chip).text == voca.type } as? Chip
            selectedChip?.isChecked = true
        }
    }

    private fun createChip(text: String): Chip {
        return Chip(this).apply {
            setText(text)
            isCheckable = true
            isCheckable = true
        }
    }

    private fun add() {
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()
        val voca = Voca(text, mean, type)

        Thread {
            AppDatabase.getInstance(this)?.vocaDao()?.insert(voca)
            runOnUiThread {
                Toast.makeText(this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent().putExtra("isUpdated", true)
            setResult(RESULT_OK, intent)
            finish()
        }.start()
    }

    private fun edit() {
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()
        val editVoca = originVoca?.copy(text = text, mean = mean, type = type)

        Thread {
            editVoca?.let { voca ->
                AppDatabase.getInstance(this)?.vocaDao()?.update(voca)
                val intent = Intent().putExtra("editVoca", voca)
                setResult(RESULT_OK, intent)
                runOnUiThread { Toast.makeText(this, "수정을 완료했습니다.", Toast.LENGTH_SHORT).show() }
                finish()
            }
        }.start()
    }
}