package com.anujan.sphassignment.di

import com.anujan.sphassignment.app.InitActivity
import com.anujan.sphassignment.home.HomeFragment
import com.anujan.sphassignment.login.LoginActivity
import com.anujan.sphassignment.register.RegisterActivity
import com.anujan.sphassignment.ui.BaseActivity
import com.anujan.sphassignment.ui.LauncherActivity
import com.anujan.sphassignment.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,  NetworkModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(registerActivity: RegisterActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(launcherActivity: LauncherActivity)
    fun inject(initActivity: InitActivity)
    fun inject(baseActivity: BaseActivity)
}