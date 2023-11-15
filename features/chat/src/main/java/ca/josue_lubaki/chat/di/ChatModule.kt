package ca.josue_lubaki.chat.di

import ca.josue_lubaki.chat.presentation.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * created by Josue Lubaki
 * date : 2023-11-11
 * version : 1.0.0
 */

internal val domainModule = module {
    viewModel<ChatViewModel> { ChatViewModel(get(), get(), get(), get()) }
}

val chatModules = listOf(domainModule)