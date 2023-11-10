package ca.josue_lubaki.di

import ca.josue_lubaki.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * created by Josue Lubaki
 * date : 2023-11-08
 * version : 1.0.0
 */

internal val domainModule = module {
    viewModel<ProfileViewModel> { ProfileViewModel(get(), get(), get(), get()) }
}

val profileModules = listOf(domainModule)