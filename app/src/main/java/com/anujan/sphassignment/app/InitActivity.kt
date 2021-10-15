package com.anujan.sphassignment.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.anujan.sphassignment.ui.LauncherActivity
import com.anujan.sphassignment.ui.MainActivity
import com.anujan.sphassignment.ui.SharedViewModel
import javax.inject.Inject

class InitActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        val appComponent = (applicationContext as SPHApplication).appComponent
        appComponent.inject(this)

        super.onCreate(savedInstanceState)

        sharedViewModel =
            ViewModelProvider(this, viewModelFactory).get(SharedViewModel::class.java)

        if (sharedViewModel.loggedInOrNot()) restartApp() else longToLogin()

    }

    private fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun longToLogin() {
        val intent = Intent(this, LauncherActivity::class.java)
        startActivity(intent)
    }

}