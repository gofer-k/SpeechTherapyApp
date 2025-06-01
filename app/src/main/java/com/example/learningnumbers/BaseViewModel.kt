package com.example.learningnumbers

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Configuration for randomization options
data class RandomizationConfig(
    val randomYears: Boolean = false,
    val randomMonths: Boolean = false,
    val randomDays: Boolean = false,
    val randomValues: Boolean = false // A generic flag for randomizing primary values (numbers, items, etc.)
)

open class BaseViewModel : ViewModel() {
    // --- Language Management ---
    private val _availableLanguages = MutableStateFlow(
        listOf(Language.ENGLISH, Language.POLISH)
    )
    val availableLanguages: StateFlow<List<Language>> = _availableLanguages.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(Language.ENGLISH) // Default selected language
    val selectedLanguage: StateFlow<Language> = _selectedLanguage.asStateFlow()

    open fun setAvailableLanguages(languages: List<Language>) {
        _availableLanguages.value = languages
        // Optionally, ensure the selected language is still valid or reset it
        if (languages.isNotEmpty() && !_selectedLanguage.value.code.equals(languages.first().code, ignoreCase = true) && !languages.contains(_selectedLanguage.value)) {
            _selectedLanguage.value = languages.first()
        }
    }

    fun availableLanguages(): List<Language> {
        return _availableLanguages.value
    }

    fun defaultLanguage(): Language {
        return _selectedLanguage.value
    }

    fun selectLanguage(language: Language) {
        // Here you might want to trigger app localization changes
        // For example, using AppCompatDelegate.setApplicationLocales()
        // This part is application-specific and might involve context or other services.
        // For now, we just update the state.
        _selectedLanguage.value = language
        // Example: updateAppLocale(language.locale) // You'd need to implement this
    }

    // --- Randomization Management ---
    private val _randomizationConfig = MutableStateFlow(RandomizationConfig())
    val randomizationConfig: StateFlow<RandomizationConfig> = _randomizationConfig.asStateFlow()

    fun updateRandomizationConfig(configUpdater: (RandomizationConfig) -> RandomizationConfig) {
        _randomizationConfig.value = configUpdater(_randomizationConfig.value)
    }

    fun setRandomYears(enabled: Boolean) {
        updateRandomizationConfig { it.copy(randomYears = enabled) }
    }

    fun setRandomMonths(enabled: Boolean) {
        updateRandomizationConfig { it.copy(randomMonths = enabled) }
    }

    fun setRandomDays(enabled: Boolean) {
        updateRandomizationConfig { it.copy(randomDays = enabled) }
    }

    fun setRandomValues(enabled: Boolean) { // For generic value randomization
        updateRandomizationConfig { it.copy(randomValues = enabled) }
    }

    // --- Common utility for loading initial data or settings ---
    // open fun loadInitialSettings() {
    //    // Load saved language, randomization preferences from SharedPreferences or a repository
    // }
}