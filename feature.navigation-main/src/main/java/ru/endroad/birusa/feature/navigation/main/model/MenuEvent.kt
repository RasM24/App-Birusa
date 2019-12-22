package ru.endroad.birusa.feature.navigation.main.model

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

sealed class MainMenuEvent

class ClickMenu(val screen: () -> Fragment) : MainMenuEvent()

class NewMessage(val countUnreadMessages: Int, @IdRes val itemId: Int) : MainMenuEvent()
class NewMapEvent(@IdRes val itemId: Int) : MainMenuEvent()

object ShowNavigation : MainMenuEvent()
object HideNavigation : MainMenuEvent()