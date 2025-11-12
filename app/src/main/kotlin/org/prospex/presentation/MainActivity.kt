package org.prospex.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.prospex.R
import org.prospex.databinding.ActivityMainBinding
import org.prospex.presentation.auth.AuthActivity
import org.prospex.presentation.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_ideas_list),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        setupDrawer()
    }

    private fun setupDrawer() {
        lifecycleScope.launch {
            authViewModel.jwt.collect { jwt ->
                if (jwt != null) {
                    val email = authViewModel.getEmailFromJwt() ?: "Пользователь"
                    binding.navView.getHeaderView(0)
                        .findViewById<android.widget.TextView>(R.id.nav_header_email)
                        .text = email
                }
            }
        }

        binding.navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            authViewModel.logout()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

