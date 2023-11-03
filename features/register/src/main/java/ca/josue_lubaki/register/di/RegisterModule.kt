package ca.josue_lubaki.register.di

import ca.josue_lubaki.register.presentation.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

internal val domainModule = module {
    viewModel<RegisterViewModel> { RegisterViewModel(get(), get(), get()) }
}

val registerModules = listOf(domainModule)