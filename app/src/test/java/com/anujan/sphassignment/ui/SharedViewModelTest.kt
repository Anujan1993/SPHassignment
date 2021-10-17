package com.anujan.sphassignment.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.anujan.sphassignment.util.SharedPreferencesData
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class SharedViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var sharedPref: SharedPreferences
    private lateinit var context: Context


    @Mock
    lateinit var sharedViewModel: SharedViewModel


    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        MockitoAnnotations.initMocks(this)
        sharedPref = context.getSharedPreferences(SharedPreferencesData.APP_USER, Context.MODE_PRIVATE)
        sharedViewModel = SharedViewModel(sharedPref)

    }


    @Test
    fun loggedInOrNot() {
        assert(sharedViewModel.loggedInOrNot() is Boolean)
    }

    @Test
    fun setLogout() {
        sharedViewModel.setLogout()
        Assert.assertFalse(sharedViewModel.loggedInOrNot())
    }

    @Test
    fun setThemeModeTrue() {
        sharedViewModel.setThemeMode(true)
        assert(sharedViewModel.loadThemeMode() is Boolean)
        Assert.assertTrue(sharedViewModel.loadThemeMode())
    }
    @Test
    fun setThemeModeFalse() {
        sharedViewModel.setThemeMode(false)
        assert(sharedViewModel.loadThemeMode() is Boolean)
        Assert.assertFalse(sharedViewModel.loadThemeMode())
    }

    @Test
    fun loadThemeMode() {
        assert(sharedViewModel.loadThemeMode() is Boolean)
    }
}