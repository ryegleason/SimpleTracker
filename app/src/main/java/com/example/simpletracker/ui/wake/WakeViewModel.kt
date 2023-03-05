package com.example.simpletracker.ui.wake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WakeViewModel : ViewModel() {

    private val _displayText = MutableLiveData<String>().apply {
        value = "Asleep"
    }

    private val _buttonText = MutableLiveData<String>().apply {
        value = "Wake Up"
    }

    fun setSleepStatus(isAwake: Boolean) {
        this.isAwake = isAwake
        if (isAwake) {
            _displayText.apply { value = "Awake" }
            _buttonText.apply { value = "Go To Sleep" }
        } else {
            _displayText.apply { value = "Asleep" }
            _buttonText.apply { value = "Wake Up" }
        }
    }

    var isAwake: Boolean = false
    val displayText: LiveData<String> = _displayText
    val buttonText: LiveData<String> = _buttonText
}