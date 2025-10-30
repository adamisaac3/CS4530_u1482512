package com.example.funfacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.funfacts.main.FunFactsApplication
import com.example.funfacts.room.FunFactEntity
import com.example.funfacts.room.FunFactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FunFactViewModel(private val repository: FunFactRepository) : ViewModel(){
    val facts : StateFlow<List<FunFactEntity>> = repository.allFacts.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchNewFact(){
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.fetchAndStoreNewFact()
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to fetch fact"
                }

                _isLoading.value = false

        }
    }

    fun clearError(){
        _error.value = null
    }

    fun deleteFact(factId: Long){
        viewModelScope.launch{
            repository.deleteFact(factId)
        }
    }

}


object FunFactViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FunFactViewModel(
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as FunFactsApplication).repository
            )
        }
    }
}