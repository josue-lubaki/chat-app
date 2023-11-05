package ca.josue_lubaki.dashboard.di

import ca.josue_lubaki.dashboard.presentation.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

internal val domainModule = module {
    viewModel<DashboardViewModel> { DashboardViewModel(get()) }
}

val dashboardModules = listOf(domainModule)