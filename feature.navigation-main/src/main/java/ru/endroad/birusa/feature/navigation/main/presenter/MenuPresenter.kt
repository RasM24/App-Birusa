package ru.endroad.birusa.feature.navigation.main.presenter

import androidx.lifecycle.LiveData
import ru.endroad.birusa.feature.navigation.main.model.MainMenuEvent
import ru.endroad.birusa.feature.navigation.main.model.NavigationBottomPartialState

interface MenuPresenter {

	val partialState: LiveData<NavigationBottomPartialState>

	fun event(event: MainMenuEvent)
}