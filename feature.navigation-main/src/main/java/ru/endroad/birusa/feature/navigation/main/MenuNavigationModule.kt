package ru.endroad.birusa.feature.navigation.main

import org.koin.dsl.module
import ru.endroad.arena.viewmodellayer.viewModel
import ru.endroad.birusa.feature.navigation.main.presenter.MenuViewModel

val featureMenuNavigation = module {

	viewModel<MenuViewModel>()
}