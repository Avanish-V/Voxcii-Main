package com.byteapps.voxcii.Utils

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class InterestSelectionViewModel:ViewModel() {

    val allInterests = listOf(
        "💻 Coding", "🎮 Gaming", "✈️ Travel", "📸 Photography", "🎵 Music",
        "🏋️ Fitness", "👨‍🍳 Cooking", "📚 Reading", "⚽ Sports", "🎬 Movies",
        "👨‍💻 Programming", "👗 Fashion", "🎨 Art", "🔬 Science", "🏛️ History",
        "💰 Finance", "🐶 Pets", "🛠️ DIY & Crafts", "🧘‍♂️ Wellness"
    )

    private val _selectedInterests = mutableStateListOf<String>()
    val selectedInterests: List<String> get() = _selectedInterests

    fun toggleInterest(interest: String) {
        if (_selectedInterests.contains(interest)) {
            _selectedInterests.remove(interest)
        } else {
            _selectedInterests.add(interest)
        }
    }

}