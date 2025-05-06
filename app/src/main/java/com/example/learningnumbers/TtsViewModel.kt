package com.example.learningnumbers

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class TtsViewModel : ViewModel() {
    private var tts: TextToSpeech? = null
    private val _state = mutableStateOf("")

    // Function to initialize TextToSpeech
    private fun initializeTts(context: Context, locale: Locale) {
        if (tts == null) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(locale)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported")
                        // Handle unsupported language, perhaps inform the user
                    }
                } else {
                    Log.e("TTS", "Initialization failed")
                    // Handle TTS initialization failure
                }
            }
        } else {
            // If TTS is already initialized, just set the language
            tts?.setLanguage(locale)
        }
    }

    fun onListenSinglePhrase(
        number: String,
        locale: Locale,
        speechRate: Float, context: Context,
        onFinishedSpeech: ((Boolean) -> Unit)? = null
    ) {
        initializeTts(context, locale)
        _state.value = number
        tts?.setSpeechRate(speechRate)
        val result = tts?.speak(
            _state.value,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
        if (result == TextToSpeech.SUCCESS) {
            onFinishedSpeech?.invoke(true)
        } else {
            onFinishedSpeech?.invoke(false)
        }
    }

    fun playNumbersSequence(
        numbers: List<Int>,
        locale: Locale,
        speechRate: Float,
        context: Context,
        delayMillis: Long = 1000, // Default delay of 1 second
        onSequenceFinished: ((Boolean) -> Unit)? = null,
        onNumberSpoken: ((Int, Boolean) -> Unit)? = null // Callback for each number
    ) {
        viewModelScope.launch {
            initializeTts(context, locale) // Ensure TTS is initialized
            tts?.setSpeechRate(speechRate)

            var allSuccessful = true
            for (number in numbers) {
                val phrase = number.toString()
                val result = tts?.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, null)

                val success = result == TextToSpeech.SUCCESS
                if (!success) {
                    allSuccessful = false
                    Log.e("TTS", "Failed to speak number: $number")
                }

                onNumberSpoken?.invoke(
                    number,
                    success
                ) // Notify the UI about this number's playback

                // Wait for the speech to complete before adding a delay
                // This is a simplified approach; for precise timing and handling interruptions,
                // you'd use UtteranceProgressListener.
                // A simple way to wait for the current utterance to finish:
                // You might need a loop checking isSpeaking() or use UtteranceProgressListener
                // For this example, a small delay to allow TTS engine to start is a workaround
                delay(200) // Small delay to allow TTS to start before checking

                while (tts?.isSpeaking == true) {
                    delay(50) // Wait while TTS is speaking
                }

                if (number != numbers.last()) {
                    delay(delayMillis) // Add the delay after each number, except the last one
                }
            }
            onSequenceFinished?.invoke(allSuccessful) // Notify the UI when the whole sequence is done
        }
    }

    fun stopSpeaking() {
        onCleared()
    }

    // Release TextToSpeech resources when the ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
