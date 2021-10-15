package com.anujan.sphassignment.register

import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anujan.sphassignment.util.loadLoginSharedPrefState
import com.anujan.sphassignment.repository.UserRepository
import com.anujan.sphassignment.util.ErrorObject
import com.anujan.sphassignment.util.TextObservable
import kotlinx.coroutines.launch
import com.anujan.sphassignment.util.Result
import java.security.MessageDigest
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreferences
) : ViewModel() {
    private var _result = MutableLiveData<Result<String>>()
    private var _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
    val result: LiveData<Result<String>> get() = _result
    var country: String? = null

    fun initRegister(){
        val name = nameObservable.text
        val email = emailObservable.text
        val phone = phoneObservable.text
        val conformation = conformPasswordObservable.text
        val password = passwordObservable.text
        register(
            name.toString(),
            email.toString(),
            phone.toString(),
            conformation.toString(),
            password.toString(),
            country.toString())
    }

    fun register(name:String,email:String,phone:String,conformation:String,password:String,country:String) {


        when {
            name == "null" -> {
                _error.value = ErrorObject.NAME_OBJECT
            }
            email == "null" -> {
                _error.value = ErrorObject.EMAIL_OBJECT
            }
            phone == "null" -> {
                _error.value = ErrorObject.PHONE_OBJECT
            }
            conformation == "null" -> {
                _error.value = ErrorObject.CONFORM_PASSWORD_OBJECT
            }
            password == "null" -> {
                _error.value = ErrorObject.PASSWORD_OBJECT
            }
            else -> {
                if (password == conformation) {
                    val passwordHash: String = hashAndSavePasswordHash(password)
                    viewModelScope.launch {
                        val result2 = userRepository.registerUser(
                            name,
                            email,
                            phone,
                            country,
                            passwordHash
                        )
                        _result.value = result2.value
                    }
                } else {
                    _error.value = ErrorObject.PASSWORD_NOT_MATCH
                }
            }
        }

    }

    val emailObservable = TextObservable()
    val passwordObservable = TextObservable()
    val nameObservable = TextObservable()
    val phoneObservable = TextObservable()
    val conformPasswordObservable = TextObservable()

    fun hashAndSavePasswordHash(clearPassword: String): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val result = digest.digest(clearPassword.toByteArray(Charsets.UTF_8))
        val sb = StringBuilder()
        for (b in result) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }

    fun getCountry(country: String) {
        this.country = country
    }

    fun loggedInOrNot(): Boolean {
        return sharedPref.loadLoginSharedPrefState()
    }
}