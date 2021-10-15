package com.anujan.sphassignment.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Switch
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.anujan.sphassignment.R
import com.anujan.sphassignment.app.SPHApplication
import com.anujan.sphassignment.databinding.ActivityLauncherBinding
import com.anujan.sphassignment.home.HomeFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var xyz: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val appComponent = (applicationContext as SPHApplication).appComponent
        appComponent.inject(this)

        super.onCreate(savedInstanceState)
        val binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedViewModel =
            ViewModelProvider(this, viewModelFactory).get(SharedViewModel::class.java)

        binding.viewmodel = sharedViewModel

        setDark()
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer)


        val actionbar = supportActionBar
        actionbar!!.title = "List of Users"

        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.Open,
            R.string.close
        )
        actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.primaryTextColor)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        actionBarDrawerToggle.syncState()

        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.container_fragment,
            HomeFragment()
        )
        fragmentTransaction.commit()

        val navMenu = navigationView.menu
        val menuItem = navMenu.findItem(R.id.switch1)
        xyz = menuItem.actionView as Switch
        if (sharedViewModel.loadThemeMode()){
            xyz!!.isChecked =true
        }
        xyz!!.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                sharedViewModel.setThemeMode(true)
                restartApp()
            }
            else{
                sharedViewModel.setThemeMode(false)
                restartApp()
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                fragmentManager = supportFragmentManager
                fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.container_fragment,
                    HomeFragment()
                )
                fragmentTransaction.commit()
            }
            R.id.Log_out -> {
                sharedViewModel.setLogout()
                val intent = Intent(this,
                    LauncherActivity::class.java)
                startActivity(intent)
                finish();
            }
        }
        return false

    }
    private fun restartApp(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}

