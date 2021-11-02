package com.olsera.warungmakan

import com.olsera.warungmakan.home.HomeViewModel
import com.olsera.warungmakan.warungeditor.WarungEditorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { WarungEditorViewModel(get()) }
}