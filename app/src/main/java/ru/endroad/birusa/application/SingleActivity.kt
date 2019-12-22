package ru.endroad.birusa.application

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import ru.endroad.arena.viewlayer.activity.BaseActivity
import ru.endroad.birusa.R
import ru.endroad.feature.auth.view.AuthFragment
import ru.endroad.navigation.changeRoot

class SingleActivity : BaseActivity() {

	override val layout = R.layout.base_layout

	override val theme = R.style.AppTheme

	init {
		loadKoinModules(module {
			single { supportFragmentManager }
		})



	}

	override fun onFirstCreate() {
		supportFragmentManager.changeRoot(AuthFragment(), R.id.root)
	}
}