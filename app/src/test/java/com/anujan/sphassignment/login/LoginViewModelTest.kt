package com.anujan.sphassignment.login

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.anujan.sphassignment.entity.AppUser
import com.anujan.sphassignment.repository.UserRepository
import com.anujan.sphassignment.util.ErrorObject
import com.anujan.sphassignment.util.Result
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner


class LoginViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()


    @Mock
    lateinit var userRepository: UserRepository
    @Mock
    lateinit var sharedPref: SharedPreferences

    @Mock
    lateinit var loginViewModel: LoginViewModel


    private val emailNull = "null"
    private val passwordNull = "null"
    private val wrongUserName = "SomeOne"
    private val wrongPassword ="123456"
    private val emailC = "anujan@gmail.com"
    private val passwordC = "7C4A8D09CA3762AF61E59520943DC26494F8941B"
    private val successUser= Result.Success(
        AppUser(
            1,
            "Anujan",
            "85769463",
            "Singapore",
            "7C4A8D09CA3762AF61E59520943DC26494F8941B",
            "anujan@gmail.com"
        ))
    private lateinit var mainResponse : Result<AppUser>
    private lateinit var mainResponseObserver : Observer<Result<AppUser>>
    private lateinit var mainErrorObserver : Observer<Result<AppUser>>
    private lateinit var  mainError : Result<AppUser>



    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        userRepository = mock()
        mainResponse = mock()
        mainError = mock()
        mainError= Result.Error(Exception("User not exist!"))
        runBlocking{
            whenever(userRepository.login(emailC,passwordC)).thenReturn(successUser)
            whenever(userRepository.login(wrongUserName,passwordC)).thenReturn(mainError)
        }
        mainResponseObserver = mock()
        mainErrorObserver = mock()
        loginViewModel = LoginViewModel(userRepository,sharedPref)
    }

    @Test
    fun loginWithCorrectUserNamePassword(){
        runBlocking {
            loginViewModel.result.observeForever(mainResponseObserver)
            loginViewModel.login(emailC, wrongPassword)
            delay(100)
            verify(userRepository).login(emailC,passwordC)
            verify(mainResponseObserver, timeout(50)).onChanged(successUser)
        }
    }
    @Test
    fun loginWIthEmailNull()= runBlocking{
        loginViewModel.login(emailNull,passwordC)
        assertEquals(loginViewModel.error.value,ErrorObject.EMAIL_OBJECT )
    }
    @Test
    fun loginWIthPasswordNull()= runBlocking{
        loginViewModel.login(emailC,passwordNull)
        assertEquals(loginViewModel.error.value,ErrorObject.PASSWORD_OBJECT )
    }

    @Test
    fun loggedInOrNot() {
        assert(loginViewModel.loggedInOrNot() is Boolean)
    }

    @Test
    fun hashAndSavePasswordHash() {
        val newP = loginViewModel.hashAndSavePasswordHash("123456")
        assert(newP is String)
        assertEquals("7C4A8D09CA3762AF61E59520943DC26494F8941B",newP)
    }

    @Test
    fun loginWithWrongUserNamePassword(){
        runBlocking {
            loginViewModel.result.observeForever(mainErrorObserver)
            loginViewModel.login(wrongUserName, wrongPassword)
            delay(100)
            verify(userRepository).login(wrongUserName,passwordC)
            verify(mainErrorObserver, timeout(50)).onChanged(mainError)
        }
    }
}

