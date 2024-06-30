package com.example.sosinfoapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.sosinfoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moveToInputActivity.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        binding.deleteButton.setOnClickListener {
            deleteData()
        }

        binding.moveToSosCallLayer.setOnClickListener {
            with(Intent(Intent.ACTION_VIEW)) {
                val phoneNum = binding.sosPhoneValueTextView.text.toString().replace("-","")
                data = Uri.parse("tel:$phoneNum")
                startActivity(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getDataAndUiUpdate()
    }

    private fun getDataAndUiUpdate() {
        with(getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)) {
            binding.nameValueTextView.text = getString(NAME, "미입력")
            binding.birthValueTextView.text = getString(BIRTH, "미입력")
            binding.bloodTypeValueTextView.text = getString(BLOOD_TYPE, "미입력")
            binding.sosPhoneValueTextView.text = getString(SOS_PHONE, "미입력")
            val notiInfo = getString(NOTI_INFO, "")

            binding.notiInfoTextView.isVisible = notiInfo.isNullOrEmpty().not()
            binding.notiInfoValueTextView.isVisible = notiInfo.isNullOrEmpty().not()

            if(!notiInfo.isNullOrEmpty()) {
                binding.notiInfoValueTextView.text = notiInfo
            }
        }
    }

    private fun deleteData() {
        with(getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit()) {
            clear()
            apply()
            getDataAndUiUpdate()
        }
        Toast.makeText(this, "의료 정보가 초기화 되었습니다.", Toast.LENGTH_SHORT).show()
    }
}