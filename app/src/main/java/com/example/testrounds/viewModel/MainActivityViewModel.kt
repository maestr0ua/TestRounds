package com.example.testrounds.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testrounds.domain.ImageEntity
import com.example.testrounds.domain.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class UIState {
    data object UILoading : UIState()
    data class UIReady(val images: List<ImageEntity>) : UIState()
    data class UIError(val errorMessage: String) : UIState()
}

class MainActivityViewModel : ViewModel() {

    private val imageRepository = ImageRepository()

    private val _uiState = MutableStateFlow<UIState>(UIState.UILoading)
    val uiState = _uiState.asStateFlow()

    fun fetchData() {
        _uiState.tryEmit(UIState.UILoading)

        viewModelScope.launch {
            val result = imageRepository.fetchImages()
            if (result.isSuccess) {
                _uiState.tryEmit(UIState.UIReady(result.getOrDefault(emptyList())))
            } else {
                _uiState.tryEmit(UIState.UIError(result.exceptionOrNull()?.message ?: ""))
            }
        }
    }

}