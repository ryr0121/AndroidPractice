package com.example.vocaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocaapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), VocaAdapter.ItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var vocaAdapter: VocaAdapter
    private var selectedVoca: Voca? = null

    private val updateAddVocaResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val isUpdated = result.data?.getBooleanExtra("isUpdated", false) ?: false
        if(result.resultCode == RESULT_OK && isUpdated) {
            updateAddVoca()
        }
    }

    private val updateEditVocaResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val updatedVoca = result.data?.getParcelableExtra<Voca>("editVoca")
        if(result.resultCode == RESULT_OK && updatedVoca != null) {
            updateEditWord(updatedVoca)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        binding.addButton.setOnClickListener {
            Intent(this, AddVocaActivity::class.java).let {
                updateAddVocaResult.launch(it)
            }
        }

        binding.deleteImageView.setOnClickListener {
            delete()
        }

        binding.editImageView.setOnClickListener {
            edit()
        }
    }



    private fun initRecyclerView() {
        vocaAdapter = VocaAdapter(mutableListOf(), this)
        binding.vocaRecyclerView.apply {
            adapter = vocaAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }

        Thread {
            val list = AppDatabase.getInstance(this)?.vocaDao()?.getAll() ?: emptyList()
            vocaAdapter.list.addAll(list)
            runOnUiThread { vocaAdapter.notifyDataSetChanged() }
        }.start()
    }

    private fun updateAddVoca() {
        Thread {
            AppDatabase.getInstance(this)?.vocaDao()?.getLatestVoca()?.let {voca ->
                vocaAdapter.list.add(0, voca)
                runOnUiThread { vocaAdapter.notifyDataSetChanged() }
            }
        }.start()
    }

    private fun delete() {
        if(selectedVoca == null) return

        Thread {
            selectedVoca?.let { voca ->
                AppDatabase.getInstance(this)?.vocaDao()?.delete(voca)
                runOnUiThread {
                    vocaAdapter.list.remove(voca)
                    vocaAdapter.notifyDataSetChanged()
                    binding.textTextView.text = ""
                    binding.meanTextView.text = ""
                    Toast.makeText(this, "삭제가 완료됐습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun edit() {
        if(selectedVoca == null) return

        val intent = Intent(this, AddVocaActivity::class.java).putExtra("originVoca", selectedVoca)
        updateEditVocaResult.launch(intent)
    }

    private fun updateEditWord(voca: Voca) {
        val index = vocaAdapter.list.indexOfFirst { it.id == voca.id }
        vocaAdapter.list[index] = voca
        runOnUiThread {
            selectedVoca = voca
            vocaAdapter.notifyItemChanged(index)
            binding.textTextView.text = voca.text
            binding.meanTextView.text = voca.mean
        }
    }

    override fun onClick(voca: Voca) {
        selectedVoca = voca
        binding.textTextView.text = voca.text
        binding.meanTextView.text = voca.mean
    }
}