package com.anujan.sphassignment.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anujan.sphassignment.R
import com.anujan.sphassignment.app.SPHApplication
import com.anujan.sphassignment.databinding.ActivityLauncherBinding
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {

        val appComponent = (applicationContext as SPHApplication).appComponent
        appComponent.inject(this)

        super.onCreate(savedInstanceState, persistentState)
        val binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedViewModel =
            ViewModelProvider(this, viewModelFactory).get(SharedViewModel::class.java)

    }
    fun setDark() {
        if (sharedViewModel.loadThemeMode()){
            setTheme(R.style.AppThemeDark)
        }
        else{
            setTheme(R.style.AppThemeLite)
        }
    }
    fun setDarkAction() {
        if (sharedViewModel.loadThemeMode()){
            setTheme(R.style.AppThemeDarkActionBar)
        }
        else{
            setTheme(R.style.AppThemeLiteActionBar)
        }
    }

}