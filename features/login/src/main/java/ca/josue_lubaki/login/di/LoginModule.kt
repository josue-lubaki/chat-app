package ca.josue_lubaki.login.di

import ca.josue_lubaki.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

internal val domainModule = module {
    viewModel<LoginViewModel> { LoginViewModel(get()) }
}

val loginModules = listOf(domainModule)

