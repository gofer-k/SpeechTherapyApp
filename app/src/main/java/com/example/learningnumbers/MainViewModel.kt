package com.example.learningnumbers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class MainViewModel : ViewModel() {
    fun updatedLanguage(lang: Language) {
        _selectedLanguage.value = lang
    }

    fun updatedNumbersRange(range: IntRange) {
        _selectedNumbersRange.value = range
    }

    fun updateRandimizeNumbers(random: Boolean) {
        _randomized.value = random
    }

    fun numbersToLearn(): List<Int> {
        if (_randomized.value == true) {
            _selectedNumbersRange.value?.shuffled()?.let {
                return it
            }
        }
        return _selectedNumbersRange.value?.toList() ?: defaultNumbersRange.toList()
    }

    val defaultLanguage = Language("English", Locale("en"))
    private val _selectedLanguage = MutableLiveData<Language>(defaultLanguage)
    val selectedLanguage: LiveData<Language> = _selectedLanguage

    val languages = listOf(
        Language("English", Locale("en")),
        Language("polish", Locale("pl"))
    )

    val defaultNumbersRange = 0..100
    private val _selectedNumbersRange = MutableLiveData<IntRange>(defaultNumbersRange)
    val selectedNumbersRange: LiveData<IntRange> = _selectedNumbersRange

    private val _randomized = MutableLiveData<Boolean>(false)
    val randomized: LiveData<Boolean> = _randomized

    val numbersRanges = listOf(
        0..10,
        0..100,
        0..1000,
        10..100,
        10..1000,
        100..1000
    )
}