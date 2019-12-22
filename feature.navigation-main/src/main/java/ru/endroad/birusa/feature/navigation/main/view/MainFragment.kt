package ru.endroad.birusa.feature.navigation.main.view

import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.endroad.arena.data.flow.extension.subcribe
import ru.endroad.arena.viewlayer.fragment.BaseFragment
import ru.endroad.birusa.feature.navigation.main.R
import ru.endroad.birusa.feature.navigation.main.model.BadgeState
import ru.endroad.birusa.feature.navigation.main.presenter.MenuPresenter
import ru.endroad.birusa.feature.navigation.main.presenter.MenuViewModel

class MainFragment : BaseFragment() {

	override val layout = R.layout.main_fragment

	private val viewModel: MenuPresenter by viewModel<MenuViewModel>()

	private val initialTab = R.id.tab_chat

	override fun setupViewModel() {
		viewModel.badgeChanges.subcribe(this, ::resolveBadgeState)
		viewModel.navigationVisible.subcribe(this) {
			navigation.isVisible = it
		}
	}

	override fun setupViewComponents() {
		navigation.setOnNavigationItemSelectedListener { menu ->
			when (menu.itemId) {
				R.id.tab_map     -> viewModel.clickOnMapItem()
				R.id.tab_chat    -> viewModel.clickOnChatItem()
				R.id.tab_library -> viewModel.clickOnLibraryItem()
				else             -> return@setOnNavigationItemSelectedListener false
			}
			true
		}
		//if (childFragmentManager.findFragmentById(R.id.mainContainer) == null) {
		navigation.selectedItemId = initialTab
		//}
	}

	private fun resolveBadgeState(badge: BadgeState) {
		when (badge) {
			is BadgeState.None   -> navigation.removeBadge(badge.itemId)
			is BadgeState.Show   -> navigation.getBadge(badge.itemId)
			is BadgeState.Number -> navigation.getBadge(badge.itemId)?.apply { number = badge.number }
		}
	}
}