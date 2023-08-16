package ru.plumsoftware.weatherforecast.presentation.content.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import ru.plumsoftware.weatherforecast.application.App
import ru.plumsoftware.weatherforecast.data.utilities.showToast
import ru.plumsoftware.weatherforecast.domain.models.UserSettings
import ru.plumsoftware.weatherforecast.domain.storage.SharedPreferencesStorage

class ContentStoreFactory(
    private val storeFactory: StoreFactory,
    private val sharedPreferencesStorage: SharedPreferencesStorage
) {

    @OptIn(ExperimentalMviKotlinApi::class)
    fun create(): ContentStore =
        object : ContentStore,
            Store<ContentStore.Intent, ContentStore.State, ContentStore.Label> by storeFactory.create(
                name = "Content",
                initialState = ContentStore.State(),
                bootstrapper = coroutineBootstrapper {
                    launch {
                        dispatch(ContentStoreFactory.Action.InitLocation)
                        dispatch(ContentStoreFactory.Action.InitTips)
                    }
                },
                reducer = ContentStoreFactory.ReducerImpl,
                executorFactory = ::ExecutorImpl
            ) {
        }

    sealed interface Action {
        object InitLocation : Action
        object InitTips : Action
    }

    sealed interface Msg {
        data class LocationData(
            val city: String,
            val country: String
        ) : Msg

        data class DropDownMenu(val value: Boolean) : Msg

        data class CheckBoxValue(val value: Boolean) : Msg
    }

    private object ReducerImpl : Reducer<ContentStore.State, Msg> {

        override fun ContentStore.State.reduce(msg: Msg): ContentStore.State =
            when (msg) {
                is Msg.LocationData -> copy(
                    city = msg.city,
                    country = msg.country
                )

                is Msg.CheckBoxValue -> copy(checkBoxState = msg.value)
                is Msg.DropDownMenu -> copy(dropDownState = !msg.value)
            }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ContentStore.Intent, Action, ContentStore.State, Msg, ContentStore.Label>() {

        override fun executeIntent(
            intent: ContentStore.Intent,
            getState: () -> ContentStore.State
        ) =
            when (intent) {

                is ContentStore.Intent.CheckBoxChange -> {
                    dispatch(Msg.CheckBoxValue(value = intent.value))
                    sharedPreferencesStorage.save(
                        userSettings = UserSettings(
                            isDarkTheme = sharedPreferencesStorage.get().isDarkTheme,
                            city = sharedPreferencesStorage.get().city,
                            country = sharedPreferencesStorage.get().country,
                            showTips = intent.value
                        )
                    )
                }

                is ContentStore.Intent.DropDownMenuChange -> {
                    dispatch(Msg.DropDownMenu(value = intent.value))
                }

                is ContentStore.Intent.OpenLocation -> {
                    publish(ContentStore.Label.OpenLocation)
                }
            }

        override fun executeAction(action: Action, getState: () -> ContentStore.State) =
            when (action) {
                is Action.InitLocation -> initLocation()
                is Action.InitTips -> initTips()
            }

        private fun initTips() {
            scope.launch {
                val userSettings: UserSettings = sharedPreferencesStorage.get()
                dispatch(
                    ContentStoreFactory.Msg.CheckBoxValue(
                        value = userSettings.showTips
                    )
                )
            }
        }

        private fun initLocation() {
            scope.launch {
                val userSettings: UserSettings = sharedPreferencesStorage.get()
                with(userSettings) {
                    dispatch(
                        ContentStoreFactory.Msg.LocationData(
                            city = city!!,
                            country = country!!
                        )
                    )
                }
            }
        }
    }
}