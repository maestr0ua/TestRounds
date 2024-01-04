package com.example.testrounds.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remoteimage.RemoteImage
import com.example.testrounds.databinding.ActivityMainBinding
import com.example.testrounds.domain.ImageEntity
import com.example.testrounds.utils.SpaceItemDecoration
import com.example.testrounds.viewModel.MainActivityViewModel
import com.example.testrounds.viewModel.UIState
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.addItemDecoration(SpaceItemDecoration(this, 16))
        binding.rvList.setHasFixedSize(true)

        binding.btnRetry.setOnClickListener { viewModel.fetchData() }
        binding.btnClearCache.setOnClickListener { RemoteImage.clearCache(this) }

        collectUIState()
        viewModel.fetchData()
    }

    private fun collectUIState() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    is UIState.UIError -> onUIError(it.errorMessage)
                    UIState.UILoading -> onUILoading()
                    is UIState.UIReady -> onUIReady(it.images)
                }
            }
        }
    }

    private fun onUILoading() {
        binding.pbLoading.isVisible = true
        binding.layoutError.isVisible = false
        binding.rvList.isVisible = false
    }

    private fun onUIReady(images: List<ImageEntity>) {
        binding.pbLoading.isVisible = false
        binding.layoutError.isVisible = false
        binding.rvList.isVisible = true
        binding.rvList.adapter = ImageAdapter(images).apply {
            setHasStableIds(true)
        }
    }

    private fun onUIError(errorMessage: String) {
        binding.pbLoading.isVisible = false
        binding.rvList.isVisible = false
        binding.layoutError.isVisible = true
        binding.tvError.text = errorMessage
    }
}