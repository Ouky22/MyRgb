package com.myrgb.ledcontroller

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.myrgb.ledcontroller.databinding.ActivityMainBinding
import com.myrgb.ledcontroller.feature.rgbalarmclock.list.RgbAlarmListFragment
import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerFragment
import com.myrgb.ledcontroller.feature.rgbshow.RgbShowFragment
import com.myrgb.ledcontroller.network.NetworkConnectivityObserver
import com.myrgb.ledcontroller.network.WifiStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var netWorkConnectivityObserver: NetworkConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initWifiConnectivityObserver()
        initUi()

        supportFragmentManager.registerFragmentLifecycleCallbacks(
            onFragmentLifeCycleCallbackHandleBottomNavBarVisibility, true
        )
    }

    private fun initUi() {
        val host =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavView.setupWithNavController(host.navController)

        // set root destinations for which no up button should be displayed
        appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.controller_dest,
                    R.id.alarm_list_dest,
                    R.id.rgb_show_dest
                )
            )
        setSupportActionBar(binding.topAppBar)
        setupActionBarWithNavController(host.navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initWifiConnectivityObserver() {
        netWorkConnectivityObserver = NetworkConnectivityObserver(this)
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        netWorkConnectivityObserver.observe(networkRequest).onEach { wifiStatus ->
            if (wifiStatus == WifiStatus.UNAVAILABLE)
                showSnackbarWithText(getString(R.string.no_wifi_connection))
            else if (wifiStatus == WifiStatus.LOST)
                showSnackbarWithText(getString(R.string.wifi_connection_lost))
        }.launchIn(lifecycleScope)
    }

    private fun showSnackbarWithText(text: String) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show()
    }

    private val onFragmentLifeCycleCallbackHandleBottomNavBarVisibility =
        object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?
            ) {
                when (f) {
                    is ControllerFragment, is RgbAlarmListFragment, is RgbShowFragment -> {
                        binding.bottomNavView.visibility = View.VISIBLE
                    }
                    is NavHostFragment -> return
                    else -> binding.bottomNavView.visibility = View.GONE
                }
            }
        }
}