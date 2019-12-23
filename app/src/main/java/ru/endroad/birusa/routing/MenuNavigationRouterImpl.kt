package ru.endroad.birusa.routing

import androidx.fragment.app.FragmentManager
import endroad.birusa.FragmentLibrary
import endroad.birusa.FragmentMap
import ru.endroad.birusa.R
import ru.endroad.birusa.feature.chat.view.ChatFragment
import ru.endroad.birusa.feature.navigation.main.MenuNavigationRouter
import ru.endroad.navigation.changeRoot

class MenuNavigationRouterImpl(private val fragmentManager: FragmentManager) : MenuNavigationRouter {

	override fun openMapScreen() {
		fragmentManager.changeRoot(FragmentMap(), R.id.content)
	}

	override fun openChatScreen() {
		fragmentManager.changeRoot(ChatFragment(), R.id.content)
	}

	override fun openLibraryScreen() {
		fragmentManager.changeRoot(FragmentLibrary(), R.id.content)
	}
}