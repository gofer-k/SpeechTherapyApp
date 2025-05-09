package com.example.learningnumbers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class MainViewModel : ViewModel() {
    fun updatedLanguage(lang: Language) {
        _selectedLanguage.value = lang
    }

    fun updatedNumbersRange(range: IntProgression) {
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
        Language("Polish", Locale("pl"))
    )

    val defaultNumbersRange = 0..100 step 1
    private val _selectedNumbersRange = MutableLiveData<IntProgression>(defaultNumbersRange)
    val selectedNumbersRange: LiveData<IntProgression> = _selectedNumbersRange

    private val _randomized = MutableLiveData<Boolean>(false)
    val randomized: LiveData<Boolean> = _randomized

    val numbersRanges = listOf(
        0..10,
        0..100,
        0..1000,
        0..10000,
        0..100000,
        0..1000000,
        10..100 step 10,
        100..1000 step 100,
        1000..10000 step 1000,
        10000..100000 step 10000,
        100000..1000000 step 100000,
        1000000..10000000 step 1000000
    )
}