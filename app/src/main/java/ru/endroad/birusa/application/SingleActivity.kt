package ru.endroad.birusa.application

import android.os.Bundle
import android.os.PersistableBundle
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import ru.endroad.arena.viewlayer.activity.BaseActivity
import ru.endroad.birusa.R
import ru.endroad.feature.auth.view.AuthFragment
import ru.endroad.navigation.changeRoot

class SingleActivity : BaseActivity() {

	override val layout = R.layout.base_layout

	override val theme = R.style.AppTheme

	private val fragmentManagerModule = module { factory { supportFragmentManager } }

	override fun onFirstCreate() {
		supportFragmentManager.changeRoot(AuthFragment(), R.id.root)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		loadKoinModules(fragmentManagerModule)
		super.onCreate(savedInstanceState)
	}

	override fun onDestroy() {
		unloadKoinModules(fragmentManagerModule)
		super.onDestroy()
	}
}