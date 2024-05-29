package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dicoding.asclepius.MainApp
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.AppRepository
import com.dicoding.asclepius.data.local.ResultEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.util.parseTimestamp
import kotlinx.coroutines.launch
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var repository: AppRepository
    private lateinit var binding: ActivityResultBinding
    private val viewmodel: ResultViewModel by viewModels(
        factoryProducer = {
            MainViewModelFactory(repository)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = AppRepository.getInstance(MainApp.db.dao)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra(EXTRA_ID, 0)
        viewmodel.getOneData(id)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.state.collect {
                    if (it != null) {
                        setView(it)
                    }
                }
            }
        }
    }

    private fun setView(it: ResultEntity) {
        val imageUri = Uri.parse(it.imageUri)
        val result = it.label
        val score = NumberFormat.getPercentInstance().format(it.score)
        val saveAt = parseTimestamp(it.savedAt)

        binding.resultImage.setImageURI(imageUri)
        binding.resultClasification.text = getString(R.string.result_score, result, score)
        binding.txtSaveAt.text = saveAt
    }

    companion object {
        const val EXTRA_ID = "extra_id"

    }
}