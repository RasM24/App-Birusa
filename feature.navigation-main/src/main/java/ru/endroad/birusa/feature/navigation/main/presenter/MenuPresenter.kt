package ru.endroad.birusa.feature.navigation.main.presenter

import androidx.lifecycle.LiveData
import ru.endroad.arena.mvi.storage.SingleLiveData
import ru.endroad.birusa.feature.navigation.main.model.BadgeState

internal interface MenuPresenter {

	val navigationVisible: LiveData<Boolean>
	val badgeChanges: SingleLiveData<BadgeState>

	fun clickOnMapItem()
	fun clickOnChatItem()
	fun clickOnLibraryItem()
}