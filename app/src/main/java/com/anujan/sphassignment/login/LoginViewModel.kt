package com.anujan.sphassignment.login

import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anujan.sphassignment.entity.AppUser
import com.anujan.sphassignment.repository.UserRepository
import com.anujan.sphassignment.util.AppConstant
import com.anujan.sphassignment.util.ErrorObject
import com.anujan.sphassignment.util.TextObservable
import com.anujan.sphassignment.util.loadLoginSharedPrefState
import kotlinx.coroutines.launch
import com.anujan.sphassignment.util.Result
import java.security.MessageDigest
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository, private val sharedPref: SharedPreferences
) : ViewModel() {
    private var _result = MutableLiveData<Result<AppUser>>()
    private var _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
    val result: LiveData<Result<AppUser>> get() = _result

    fun initLogin(){
        login(emailObservable.text.toString(),passwordObservable.text.toString())
    }

    fun login(email:String, password:String) {
        when {
            email == "null" -> {
                _error.value = ErrorObject.EMAIL_OBJECT
            }
            password == "null" -> {
                _error.value = ErrorObject.PASSWORD_OBJECT
            }
            else -> {
                val passwordHash: String = hashAndSavePasswordHash(password)
                viewModelScope.launch {
                    _result.value = userRepository.login(email, passwordHash)
                }
            }
        }

    }

    fun loggedInOrNot(): Boolean {
        return sharedPref.loadLoginSharedPrefState()
    }

    val emailObservable = TextObservable()
    val passwordObservable = TextObservable()

    fun hashAndSavePasswordHash(clearPassword: String): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val result = digest.digest(clearPassword.toByteArray(Charsets.UTF_8))
        val sb = StringBuilder()
        for (b in result) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }
}