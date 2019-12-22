package ru.endroad.birusa.feature.navigation.main.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.endroad.birusa.feature.navigation.main.MenuNavigationRouter
import ru.endroad.birusa.feature.navigation.main.model.*

class MenuViewModel(private val router: MenuNavigationRouter) : ViewModel(), MenuPresenter {

	override val partialState = MutableLiveData<NavigationBottomPartialState>()

	override fun event(event: MainMenuEvent) {
		when (event) {
			is ClickMenu   -> router.openMenuItem(event.screen)

			is NewMessage  -> addBadgeToMessage(event)
			is NewMapEvent -> addBadgeToMap(event)
			ShowNavigation -> partialState.value = ChangeVisible(true)
			HideNavigation -> partialState.value = ChangeVisible(false)
		}
	}

	private fun addBadgeToMessage(event: NewMessage) {
		partialState.value = ChangeBadge(event.itemId, BadgeState.Number(event.countUnreadMessages))
	}

	private fun addBadgeToMap(event: NewMapEvent) {
		partialState.value = ChangeBadge(event.itemId, BadgeState.Show)
	}
}