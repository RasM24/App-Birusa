package ru.endroad.birusa.feature.navigation.main.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.endroad.arena.mvi.storage.SingleLiveData
import ru.endroad.birusa.feature.navigation.main.MenuNavigationRouter
import ru.endroad.birusa.feature.navigation.main.model.*

internal class MenuViewModel(private val router: MenuNavigationRouter) : ViewModel(), MenuPresenter, NavigationStatePresenter {

	override val navigationVisible = MutableLiveData<Boolean>()

	override val badgeChanges = SingleLiveData<BadgeState>()

	override fun clickOnMapItem() = router.openMapScreen()

	override fun clickOnChatItem() = router.openChatScreen()

	override fun clickOnLibraryItem() = router.openLibraryScreen()

	override fun showBottomNavigation() {
		navigationVisible.value = true
	}

	override fun hideBottomNavigation() {
		navigationVisible.value = false
	}

	override fun addBadge(badgeState: BadgeState) {
		badgeChanges(badgeState)
	}
}