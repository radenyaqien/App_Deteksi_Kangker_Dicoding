package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.MainApp
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.AppRepository
import com.dicoding.asclepius.data.local.ResultEntity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import kotlin.random.Random


class MainActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: AppRepository
    private val viewmodel by viewModels<MainViewModel>(
        factoryProducer = {
            MainViewModelFactory(repository)
        }
    )
    private var currentImageUri: Uri? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            cropingImage(it)
        } else {
            showToast("Terjadi Error Coba lagi")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = AppRepository.getInstance(MainApp.db.dao)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
        setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        return when (item.itemId) {
            R.id.article -> {
                goToArticle()
                true
            }

            R.id.archive_menu -> {
                goToHistory()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToHistory() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun goToArticle() {
        val intent = Intent(this, ArticleActivity::class.java)
        startActivity(intent)
    }

    private fun setupView() {
        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }
    }

    private fun cropingImage(uri: Uri) {
        val fileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(filesDir, fileName))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .start(this)
    }

    private fun startGallery() {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        binding.previewImageView.setImageURI(currentImageUri)
    }

    private fun analyzeImage() {
        ImageClassifierHelper(
            context = this,
            classifierListener = this
        ).run {
            currentImageUri?.let { classifyStaticImage(it) }
        }
    }

    private fun moveToResult(id: Int) {
        Log.d(MainActivity::class.java.name, "moveToResult: $id")
        val intent = Intent(this, ResultActivity::class.java)
            .apply {
                putExtra(ResultActivity.EXTRA_ID, id)
            }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onError(error: String) {
        showToast(error)
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        runOnUiThread {
            results?.let { it ->
                if (it.isNotEmpty() && it.first().categories.isNotEmpty()) {
                    val categories =
                        it.first().categories.maxByOrNull { it?.score ?: 0f }
                    val score = categories?.score ?: 0f
                    val label = categories?.label ?: "-"
                    val saveAt = System.currentTimeMillis()
                    val uri = currentImageUri?.toString() ?: "-"
                    val id = Random.nextInt()
                    saveData(
                        ResultEntity(
                            id = id,
                            savedAt = saveAt,
                            imageUri = uri,
                            label = label,
                            score = score
                        )
                    )
                    moveToResult(id)
                } else {
                    showToast(getString(R.string.no_result_found))
                }
            }
        }
    }

    private fun saveData(resultEntity: ResultEntity) {
        viewmodel.insertData(resultEntity)
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            showToast(cropError?.message ?: "Terjadi error")
        }
    }
}