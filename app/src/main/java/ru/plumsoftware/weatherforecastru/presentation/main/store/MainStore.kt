package ru.plumsoftware.weatherforecastru.presentation.main.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.plumsoftware.weatherforecastru.application.Screens

interface MainStore :
    Store<MainStore.Intent, MainStore.State, MainStore.Label> {

    sealed interface Intent {
        data class ChangeTheme(val isDarkTheme: Boolean) : Intent
    }

    data class State(
        val isDarkTheme: Boolean = false,
        val screen: String = Screens.Main
    )

    sealed interface Label {
//        data class ChangeTheme(val isDarkTheme: Boolean) : Label

        object OpenAuthorization : Label

        data class SkipAuthorization(val city: String?) : Label
    }
}