package com.example.learningnumbers

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.Locale

class TtsViewModel : ViewModel() {
    private var tts: TextToSpeech? = null
    private val _state = mutableStateOf("")

    fun onListenTrainingPhrase(
        number: String,
        locale: Locale,
        speechRate: Float, context: Context,
        onFinishedSpeech: (Boolean) -> Unit
    ) {
        _state.value = number
        textToSpeak(context, locale, speechRate, onFinishedSpeech)
    }

    fun textToSpeak(
        context: Context,
        locale: Locale,
        speechRate: Float,
        onFinishedSpeech: (Boolean) -> Unit
    ) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                Log.d("TextToSpeech", "Initialization Success")
                tts?.let {
                    val result = it.setLanguage(locale)
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED
                    ) {
                        Log.d("SpeechTraining", "Initialize TTS language")
                    }
                    it.setSpeechRate(speechRate)
                    if (it.speak(
                            _state.value,
                            TextToSpeech.QUEUE_ADD,
                            null,
                            null
                        ) == TextToSpeech.SUCCESS
                    ) {
                        onFinishedSpeech(it.isSpeaking)
                    }
                }
            } else {
                Log.d("TextToSpeech", "Initialization Failed")
            }
        }
    }
}
