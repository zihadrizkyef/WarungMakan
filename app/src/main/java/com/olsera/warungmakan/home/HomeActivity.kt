package com.olsera.warungmakan.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.olsera.warungmakan.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Handler().postDelayed({ viewModel.getList() }, 1000)
    }
}