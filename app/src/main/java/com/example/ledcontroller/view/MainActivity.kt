package com.example.ledcontroller.view

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ledcontroller.R
import com.example.ledcontroller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var showUiFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        val connectivityManager = ContextCompat.getSystemService(
            this, ConnectivityManager::class.java
        ) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        binding.ivNoWifi.visibility = View.VISIBLE
        binding.navHostFragment.visibility = View.GONE
    }

    private fun initUi() {
        val host =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavView.setupWithNavController(host.navController)

        // set root destinations for which no up button should be displayed
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.controller_dest, R.id.alarm_list_dest, R.id.rgb_show_dest))
        setSupportActionBar(binding.topAppBar)
        setupActionBarWithNavController(host.navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            runOnUiThread {
                if (showUiFirstTime) {
                    initUi()
                    showUiFirstTime = false
                }

                binding.ivNoWifi.visibility = View.GONE
                binding.navHostFragment.visibility = View.VISIBLE
                Log.d("test", "onAvailable")
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            runOnUiThread {
                binding.ivNoWifi.visibility = View.VISIBLE
                binding.navHostFragment.visibility = View.GONE
                Log.d("test", "onLost")
            }
        }
    }
}