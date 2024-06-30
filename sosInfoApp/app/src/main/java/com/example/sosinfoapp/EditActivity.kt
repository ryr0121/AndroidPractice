package com.example.sosinfoapp

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.sosinfoapp.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bloodTypeSpinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.blood_types,
            android.R.layout.simple_list_item_1
        )

        binding.birthLayer.setOnClickListener {
            val listener = OnDateSetListener { _, year, month, dayOfMonth ->
                binding.birthEditText.text = "$year-${month.inc()}-$dayOfMonth"
            }
            DatePickerDialog(
                this,
                listener,
                2000,
                1,
                1
            ).show()
        }

        binding.notiInfoCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.notiInfoEditText.isVisible = isChecked
        }

        binding.notiInfoEditText.isVisible = binding.notiInfoCheckBox.isChecked

        binding.saveButton.setOnClickListener {
            saveData()
            finish()
        }
    }


    private fun saveData() {
        with(getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit()) {
            putString(NAME, binding.nameEditText.text.toString())
            putString(BLOOD_TYPE, getBloodType())
            putString(SOS_PHONE, binding.sosPhoneValueTextView.text.toString())
            putString(BIRTH, binding.birthEditText.text.toString())
            putString(NOTI_INFO, getNotiInfo())
            apply()
        }

        Toast.makeText(this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun getBloodType(): String {
        val bloodAlphabet = binding.bloodTypeSpinner.selectedItem.toString()
        val bloodSign = if(binding.bloodTypePlus.isChecked) "+" else "-"
        return "$bloodSign$bloodAlphabet"
    }

    private fun getNotiInfo(): String {
        return if(binding.notiInfoCheckBox.isChecked) binding.notiInfoEditText.text.toString() else ""
    }
}