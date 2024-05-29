package com.dicoding.asclepius.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.MainApp
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.AppRepository
import com.dicoding.asclepius.data.local.ResultEntity
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.view.adapter.HistoryAdapter
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private val mAdapter: HistoryAdapter by lazy {
        HistoryAdapter { history ->
            gotoResultActivity(history)
        }
    }

    private fun gotoResultActivity(history: ResultEntity) {
        val intent = Intent(this, ResultActivity::class.java)
            .apply {
                putExtra(ResultActivity.EXTRA_ID, history.id)
            }
        startActivity(intent)
    }

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var repository: AppRepository
    private val viewmodel: HistoryViewmodel by viewModels<HistoryViewmodel>(
        factoryProducer = {
            MainViewModelFactory(repository)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = AppRepository.getInstance(MainApp.db.dao)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupView()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.rvHistory.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HistoryActivity)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.state.collect {
                    mAdapter.submitList(it)
                }
            }
        }

    }
}