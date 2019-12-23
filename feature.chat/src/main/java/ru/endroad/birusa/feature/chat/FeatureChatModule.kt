package ru.endroad.birusa.feature.chat

import org.koin.dsl.module
import ru.endroad.arena.viewmodellayer.viewModel
import ru.endroad.birusa.feature.chat.presenter.ChatViewModel

val featureChatModule = module {

	viewModel<ChatViewModel>()
}