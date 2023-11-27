package ca.josue_lubaki.users.di

import ca.josue_lubaki.users.presentation.ContactViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

internal val domainModule = module {
    viewModel<ContactViewModel> { ContactViewModel(get(), get(), get(), get(), get()) }
}

val dashboardModules = listOf(domainModule)