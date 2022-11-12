package com.example.cryptoapp.di

import android.app.Application
import com.example.cryptoapp.presentation.activity.CoinPriceListActivity
import com.example.cryptoapp.presentation.fragment.CoinDetailFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(fragment: CoinDetailFragment)

    fun inject(activity: CoinPriceListActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}