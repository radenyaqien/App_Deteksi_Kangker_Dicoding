package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.dicoding.asclepius.databinding.ActivityArticleBinding
import com.dicoding.asclepius.view.adapter.ArticleAdapter
import kotlinx.coroutines.launch


class ArticleActivity : AppCompatActivity() {
    private val mAdapter: ArticleAdapter by lazy {
        ArticleAdapter {
            if (it.url != null) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                startActivity(browserIntent)
            }
        }
    }
    private lateinit var repository: AppRepository
    private val viewmodel by viewModels<ArticleViewModel>(
        factoryProducer = {
            MainViewModelFactory(repository)
        }
    )
    private lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = AppRepository.getInstance(MainApp.db.dao)
        enableEdgeToEdge()
        binding = ActivityArticleBinding.inflate(layoutInflater)
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
        binding.rvArticle.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ArticleActivity)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.state.collect {
                    mAdapter.submitList(it.data)
                    if (it.message != null) {
                        Toast.makeText(this@ArticleActivity, it.message, Toast.LENGTH_LONG).show()
                        viewmodel.onFailure(null)
                    }
                    if (it.isLoading) {
                        binding.shimmerViewContainer.visibility = View.VISIBLE
                        binding.shimmerViewContainer.startShimmer()
                    } else {
                        binding.shimmerViewContainer.stopShimmer()
                        binding.shimmerViewContainer.visibility = View.GONE
                    }
                }
            }
        }

    }
}