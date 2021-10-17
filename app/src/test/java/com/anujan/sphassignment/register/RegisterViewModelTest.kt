package com.anujan.sphassignment.register

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.anujan.sphassignment.entity.AppUser
import com.anujan.sphassignment.login.LoginViewModel
import com.anujan.sphassignment.repository.UserRepository
import com.anujan.sphassignment.util.ErrorObject
import com.anujan.sphassignment.util.Result
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RegisterViewModelTest {


    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var userRepository: UserRepository
    @Mock
    lateinit var sharedPref: SharedPreferences
    @Mock
    lateinit var registerViewModel: RegisterViewModel

    private val nameNull = "null"
    private val emailNull = "null"
    private val phoneNull = "null"
    private val conformationNull = "null"
    private val passwordNull = "null"
    private val country = "Singapore"

    private val name = "Anujan"
    private val email= "anujann@gmail.com"
    private val phone = "85769463"
    private val conformation = "123456"
    private val password = "123456"
    private val hassPassword = "7C4A8D09CA3762AF61E59520943DC26494F8941B"

    private val wrongPassword ="7C4A8D09CA3762AF61E59520943DC26494F8941BD"
    private val emailC = "anujan@gmail.com"
    private lateinit var mainResponse : Result<String>
    private lateinit var mainResponseObserver : Observer<Result<String>>
    private lateinit var  mainError : Result<String>



    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        mainResponse = mock()
        mainError = mock()
        mainResponse = Result.Success("Registration Success")
        mainError = Result.Error(Exception("email id already exist!"))
        runBlocking {
            whenever(userRepository.registerUser(
                name,
                emailC,
                phone,
                country,
                hassPassword
            )).thenReturn(mainResponse)
            whenever(userRepository.registerUser(
                name,
                email,
                phone,
                country,
                hassPassword
            )).thenReturn(mainError)
        }

        mainResponseObserver = mock()


        registerViewModel = RegisterViewModel(userRepository,sharedPref)
    }

    @Test
    fun registerWithAllCorrectData() = runBlocking{
        runBlocking {
            registerViewModel.result.observeForever(mainResponseObserver)
            registerViewModel.register(
                name,
                emailC,
                phone,
                conformation,
                password,
                country
            )
            delay(100)
            verify(userRepository).registerUser(
                name,
                emailC,
                phone,
                country,
                hassPassword
            )
            verify(mainResponseObserver, timeout(50)).onChanged(mainResponse)
        }
    }

    @Test
    fun registerWithWrongData() = runBlocking{
        runBlocking {
            registerViewModel.result.observeForever(mainResponseObserver)
            registerViewModel.register(
                name,
                email,
                phone,
                conformation,
                password,
                country
            )
            delay(100)
            verify(userRepository).registerUser(
                name,
                email,
                phone,
                country,
                hassPassword
            )
            verify(mainResponseObserver, timeout(50)).onChanged(mainError)
        }
    }

    @Test
    fun userNameNull(){
        registerViewModel.register(
            nameNull,
            email,
            phone,
            conformation,
            password,
            country
        )
        assertEquals(registerViewModel.error.value, ErrorObject.NAME_OBJECT )
    }
    @Test
    fun emailNull(){
        registerViewModel.register(
            name,
            emailNull,
            phone,
            conformation,
            password,
            country
        )
        assertEquals(registerViewModel.error.value, ErrorObject.EMAIL_OBJECT )
    }
    @Test
    fun phoneNull(){
        registerViewModel.register(
            name,
            email,
            phoneNull,
            conformation,
            password,
            country
        )
        assertEquals(registerViewModel.error.value, ErrorObject.PHONE_OBJECT )
    }
    @Test
    fun conformationNull(){
        registerViewModel.register(
            name,
            email,
            phone,
            conformationNull,
            password,
            country
        )
        assertEquals(registerViewModel.error.value, ErrorObject.CONFORM_PASSWORD_OBJECT )
    }
    @Test
    fun passwordNull(){
        registerViewModel.register(
            name,
            email,
            phone,
            conformation,
            passwordNull,
            country
        )
        assertEquals(registerViewModel.error.value, ErrorObject.PASSWORD_OBJECT )
    }
    @Test
    fun passwordAndCpasswordWrongNull(){
        registerViewModel.register(
            name,
            email,
            phone,
            wrongPassword,
            password,
            country
        )
        assertEquals(registerViewModel.error.value, ErrorObject.PASSWORD_NOT_MATCH )
    }


    @Test
    fun getCountry() {
        registerViewModel.getCountry("Singapore")
        assertEquals(registerViewModel.country,"Singapore")
    }

    @Test
    fun loggedInOrNot() {
        assert(registerViewModel.loggedInOrNot() is Boolean)
    }

    @Test
    fun hashAndSavePasswordHash() {
        val newP = registerViewModel.hashAndSavePasswordHash("123456")
        assert(newP is String)
        assertEquals("7C4A8D09CA3762AF61E59520943DC26494F8941B",newP)
    }
}