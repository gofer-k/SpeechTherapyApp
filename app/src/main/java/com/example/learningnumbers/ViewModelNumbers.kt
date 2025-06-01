package com.example.learningnumbers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ViewModelNumbers : BaseViewModel() {
    val defaultNumbersRange = 0..100 step 1
    private val _selectedNumbersRange = MutableLiveData<IntProgression>(defaultNumbersRange)
    val selectedNumbersRange: LiveData<IntProgression> = _selectedNumbersRange

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

    fun updatedNumbersRange(range: IntProgression) {
        _selectedNumbersRange.value = range
    }

    fun updateRandomizeNumbers(random: Boolean) {
        updateRandomizationConfig { RandomizationConfig(randomValues = random) }
    }

    fun numbersToLearn(): List<Int> {
        if (randomizationConfig.value.randomValues == true) {
            _selectedNumbersRange.value?.shuffled()?.let {
                return it
            }
        }
        return _selectedNumbersRange.value?.toList() ?: defaultNumbersRange.toList()
    }
}