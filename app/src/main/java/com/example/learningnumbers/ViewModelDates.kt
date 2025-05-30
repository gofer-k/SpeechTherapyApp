package com.example.learningnumbers

import android.util.Log
import androidx.activity.result.launch
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import kotlin.random.Random

data class DateRange(val start: LocalDate? = null, val end: LocalDate? = null) {
    val isSelected: Boolean
        get() = start != null && end != null
}

class ViewModelDates : ViewModel(){
    private val _selectedDateRange = MutableStateFlow(DateRange())
    val selectedDateRange: StateFlow<DateRange> = _selectedDateRange

    private val _startDate = MutableStateFlow<LocalDate?>(null)
    val startDate: StateFlow<LocalDate?> = _startDate

    private val _endDate = MutableStateFlow<LocalDate?>(null)
    val endDate: StateFlow<LocalDate?> = _endDate

    init {
        // Keep individual date flows in sync with the combined DateRange flow
        // This provides flexibility: update the range, and individual dates update,
        // or update an individual date, and the range updates.
        viewModelScope.launch {
            _selectedDateRange.collect { dateRange ->
                if (_startDate.value != dateRange.start) {
                    _startDate.value = dateRange.start
                }
                if (_endDate.value != dateRange.end) {
                    _endDate.value = dateRange.end
                }
            }
        }
        viewModelScope.launch {
            _startDate.collect { newStartDate ->
                _selectedDateRange.update { currentRange ->
                    currentRange.copy(start = newStartDate)
                }
            }
        }
        viewModelScope.launch {
            _endDate.collect { newEndDate ->
                _selectedDateRange.update { currentRange ->
                    currentRange.copy(end = newEndDate)
                }
            }
        }
    }

    /**
     * Sets the start date of the range.
     * Consider adding validation (e.g., startDate is not after endDate).
     */
    fun setStartDate(date: LocalDate?) {
        _startDate.value = date
        // Optional: Auto-clear end date if start date is after current end date
        // if (date != null && _endDate.value != null && date.isAfter(_endDate.value)) {
        //     _endDate.value = null
        // }
    }

    /**
     * Sets the end date of the range.
     * Consider adding validation (e.g., endDate is not before startDate).
     */
    fun setEndDate(date: LocalDate?) {
        _endDate.value = date
    }

    /**
     * Sets both start and end dates.
     * Ensures that startDate is not after endDate. If it is, they might be swapped or an error handled.
     */
    fun setDateRange(start: LocalDate?, end: LocalDate?) {
        if (start != null && end != null && start.isAfter(end)) {
            // Option 1: Swap them
            _selectedDateRange.value = DateRange(start = end, end = start)
            // Option 2: Set to invalid/clear, or handle error
            // _selectedDateRange.value = DateRange(startDate = start, endDate = null) // for example
            // Log.e("ViewModelDates", "Start date cannot be after end date.")
            return
        }
        _selectedDateRange.value = DateRange(start = start, end = end)
    }

    /**
     * Clears the selected date range.
     */
    fun clearDateRange() {
        _selectedDateRange.value = DateRange()
    }

    /**
     * Checks if a valid date range (both start and end) is selected.
     */
    fun hasValidRangeSelected(): Boolean {
        val currentRange = _selectedDateRange.value
        return currentRange.start != null &&
                currentRange.end != null &&
                !currentRange.start.isAfter(currentRange.end)
    }

    fun getDates(randomYears: Boolean, randomMonths: Boolean, randomDays: Boolean): List<LocalDate> {
        val baseRange = _selectedDateRange.value.start ?: return emptyList()

        val targetYear = if (randomYears) {
            // Random year within +/- 5 years of the base year
            // Ensure it stays within a reasonable range (e.g., 1970-2099)
            val yearOffset = Random.nextInt(-5, 6) // TODO: configure years range
            (baseRange.year + yearOffset).coerceIn(1970, 2099)
        } else {
            baseRange.year
        }

        val targetMonth = if (randomMonths) {
            // Random month within the base month range
            Random.nextInt(1, 13) // month range [1, 12)
        } else {
            baseRange.month.value
        }

        // Determine the number of days in the target month and year
        val yearMonth = YearMonth.of(targetYear, targetMonth)
        val daysInMonth = yearMonth.lengthOfMonth()

        val generatedDates = mutableListOf<LocalDate>()

        if (randomDays) {
            val noOfDaysToRandom = Random.nextInt(1, daysInMonth)   // al days of a month
            val selectedRandomDays = mutableSetOf<Int>()    // to ensure unique random days

            // Generate unique days within the target month and year
            while (selectedRandomDays.size < noOfDaysToRandom) {
                selectedRandomDays.add(Random.nextInt(1, daysInMonth + 1))
            }
            for (day in selectedRandomDays) {
                try {
                    generatedDates.add(LocalDate.of(targetYear, targetMonth, day))
                } catch (e: Exception) {
                    Log.e("ViewModelDates", "Error creating date: $targetYear-$targetMonth-$day", e)
                    return listOf()
                }
            }
        } else {
            // If not randomizing days, use the day from the baseStartDate,
            // but ensure it's valid for the potentially randomized targetMonth and targetYear.
            val dayToUse = baseRange.dayOfMonth.coerceAtMost(daysInMonth)
            try {
                generatedDates.add(LocalDate.of(targetYear, targetMonth, dayToUse))
            } catch (e: Exception) {
                Log.e("ViewModelDates", "Error creating date: $targetYear-$targetMonth-$dayToUse", e)
                return listOf()
            }
        }
        return generatedDates.toList()
    }
}