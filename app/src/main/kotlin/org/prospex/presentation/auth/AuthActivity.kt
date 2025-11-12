package org.prospex.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.prospex.databinding.ActivityAuthBinding
import org.prospex.presentation.MainActivity
import org.prospex.presentation.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAuth()
        setupObservers()
        setupListeners()
    }

    private fun checkAuth() {
        lifecycleScope.launch {
            val authState = authViewModel.authState.first()
            if (authState is org.prospex.presentation.viewmodels.AuthState.Authenticated) {
                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is org.prospex.presentation.viewmodels.AuthState.Authenticated -> {
                        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                        finish()
                    }
                    is org.prospex.presentation.viewmodels.AuthState.Loading -> {
                        binding.progressBar.visibility = android.view.View.VISIBLE
                    }
                    else -> {
                        binding.progressBar.visibility = android.view.View.GONE
                    }
                }
            }
        }

        lifecycleScope.launch {
            authViewModel.errorMessage.collect { error ->
                error?.let {
                    binding.errorText.text = it
                    binding.errorText.visibility = android.view.View.VISIBLE
                } ?: run {
                    binding.errorText.visibility = android.view.View.GONE
                }
            }
        }
    }

    private fun setupListeners() {
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            authViewModel.signIn(email, password)
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            authViewModel.signUp(email, password, confirmPassword)
        }
    }
}

