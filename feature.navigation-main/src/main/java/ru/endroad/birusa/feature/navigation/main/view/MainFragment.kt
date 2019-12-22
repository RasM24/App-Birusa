package ru.endroad.birusa.feature.navigation.main.view

import android.view.Menu
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.endroad.arena.data.flow.extension.subcribe
import ru.endroad.arena.viewlayer.fragment.BaseFragment
import ru.endroad.birusa.feature.navigation.main.R
import ru.endroad.birusa.feature.navigation.main.model.*
import ru.endroad.birusa.feature.navigation.main.presenter.MenuPresenter
import ru.endroad.birusa.feature.navigation.main.presenter.MenuViewModel

class MainFragment : BaseFragment() {

	override val layout = R.layout.main_fragment

	private val viewModel: MenuPresenter by viewModel<MenuViewModel>()

	override fun setupViewModel() {
		viewModel.partialState.subcribe(this)
		{ partialState ->
			when (partialState) {
				is SetupMenuNavigation -> setupMenuNavigation(partialState.items)
				is ChangeVisible       -> changeVisible(partialState.isShow)
				is ChangeBadge         -> resolveBadgeState(partialState.itemId, partialState.badgeState)
			}
		}
	}

	private fun setupMenuNavigation(menuItems: List<MenuItem>) {
		menuItems.forEach { item ->
			navigation.menu
				.add(Menu.NONE, item.itemId, item.position, item.name)
				.setIcon(item.icon)

			resolveBadgeState(item.itemId, item.badgeState)
		}
	}

	private fun resolveBadgeState(@IdRes itemId: Int, badge: BadgeState) {
		when (badge) {
			BadgeState.None      -> navigation.removeBadge(itemId)
			BadgeState.Show      -> navigation.getBadge(itemId)
			is BadgeState.Number -> navigation.getBadge(itemId)?.apply { number = badge.number }
		}
	}

	private fun changeVisible(isShow: Boolean) {
		navigation.isVisible = isShow
	}

}