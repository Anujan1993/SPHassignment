package com.anujan.sphassignment.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anujan.sphassignment.ui.MainActivity
import com.anujan.sphassignment.register.RegisterActivity
import com.anujan.sphassignment.app.SPHApplication
import com.anujan.sphassignment.databinding.ActivityLoginBinding
import com.anujan.sphassignment.util.AppConstant
import com.anujan.sphassignment.util.Result
import com.anujan.sphassignment.util.ErrorMessages
import com.anujan.sphassignment.util.ErrorObject
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val appComponent = (applicationContext as SPHApplication).appComponent
        appComponent.inject(this)

        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel =
            ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.viewmodel = loginViewModel
        binding.emailObservable = loginViewModel.emailObservable
        binding.passwordObservable = loginViewModel.passwordObservable

        errorObserveViewModel(binding)
        observeViewModel()

        if (loginViewModel.loggedInOrNot()) navigateToHome()

        loginBtn.setOnClickListener {
            loginViewModel.initLogin()
        }
        registerNow.setOnClickListener{
            navigateToRegister()
        }

    }

    private fun observeViewModel() {
        loginViewModel.result.observe(this, Observer {
            it?.let {
                when (it) {
                    is Result.Success -> navigateToHome()
                    is Result.Error ->Toast.makeText(this, it.exception.message, Toast.LENGTH_LONG).show()
                    else -> {}
                }
            }
        })
    }

    private fun errorObserveViewModel(binding: ActivityLoginBinding) {
        loginViewModel.error.observe(this, Observer {
            it.let {
                when (it) {
                    ErrorObject.EMAIL_OBJECT -> {
                        binding.resetEmailMain.error = ErrorMessages.EMAIL_ERROR
                        binding.resetEmailMain.requestFocus()
                    }
                    ErrorObject.PASSWORD_OBJECT -> {
                        binding.LoginPasswordMain.error = ErrorMessages.PASSWORD_ERROR
                        binding.LoginPasswordMain.requestFocus()
                    }
                    else -> {}
                }
            }
        })
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}