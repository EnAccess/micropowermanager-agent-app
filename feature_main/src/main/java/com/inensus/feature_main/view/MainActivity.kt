package com.inensus.feature_main.view

import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.core.view.updateLayoutParams
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.inensus.core.broadcast.MessagingBroadcastReceiver
import com.inensus.core.broadcast.SessionExpireBroadcastReceiver
import com.inensus.core.broadcast.SessionExpireBroadcastReceiver.Companion.SESSION_EXPIRE_INTENT_ACTION
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.extentions.afterMeasured
import com.inensus.feature_main.R
import com.inensus.feature_main.databinding.ActivityMainBinding
import com.inensus.shared_messaging.NotificationHandler
import com.inensus.shared_messaging.NotificationHandler.Companion.EXTRA_PAYLOAD
import com.inensus.shared_messaging.repository.MessagingRepository
import com.inensus.shared_navigation.Feature
import com.inensus.shared_navigation.SharedNavigation
import com.inensus.shared_success.view.SuccessFragment
import org.koin.android.ext.android.inject
import kotlin.math.min

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val sessionExpireReceiver: SessionExpireBroadcastReceiver by inject()
    private val preferences: SharedPreferenceWrapper by inject()
    private val navigation: SharedNavigation by inject()
    private val notificationHandler: NotificationHandler by inject()
    private val messagingRepository: MessagingRepository by inject()

    private lateinit var mainNavigation: MainActivityNavigation
    private lateinit var drawerFragment: DrawerFragment
    private var isAtTheTopLevel = false
    private val topLevelNavigationIds =
        setOf(R.id.dashboardFragment, R.id.customersFragment, R.id.paymentsFragment, R.id.appliancesFragment, R.id.ticketsFragment)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        setupNavigation(savedInstanceState)
        registerSessionExpireReceiver()
        registerMessagingReceiver()

        handleNotification(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        handleNotification(intent)
    }

    override fun onResume() {
        super.onResume()

        if (preferences.resetFirebase) {
            messagingRepository.resetFirebaseToken()
        }
    }

    private fun setupNavigation(savedInstanceState: Bundle?) {
        navController = findNavController(R.id.mainFragment)

        setupDrawerWidth(binding.navView)

        if (savedInstanceState == null) {
            drawerFragment = DrawerFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.navView, drawerFragment, "")
                .commit()
        } else {
            drawerFragment = supportFragmentManager.findFragmentById(R.id.navView) as DrawerFragment
        }

        mainNavigation = MainActivityNavigation(navController)

        val appBarConfiguration =
            AppBarConfiguration(
                topLevelNavigationIds,
                binding.drawerLayout,
            )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            isAtTheTopLevel = topLevelNavigationIds.contains(destination.id)
        }

        setupDrawerCallbacks()
    }

    /*
        Sets the width of navigation drawer - either static width (306dp) or width of the screen - height of actionBar (54dp) - whatever is smaller
     */
    private fun setupDrawerWidth(view: View) {
        view.afterMeasured {
            view.updateLayoutParams<ViewGroup.LayoutParams> {
                width =
                    min(
                        resources.getDimensionPixelSize(R.dimen.max_nav_drawer_width),
                        rootView.width - resources.getDimensionPixelSize(R.dimen.nav_drawer_margin_right),
                    )
            }
        }
    }

    private fun setupDrawerCallbacks() {
        drawerFragment.navCallback = {
            closeDrawers()
            mainNavigation.handleNavigationAction(it)
        }
    }

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun closeDrawers() {
        binding.drawerLayout.closeDrawers()
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END)
    }

    private fun registerSessionExpireReceiver() {
        registerReceiver(sessionExpireReceiver, IntentFilter(SESSION_EXPIRE_INTENT_ACTION))

        sessionExpireReceiver.event.observe(this, {
            logout()
        })
    }

    private fun registerMessagingReceiver() {
        registerReceiver(messagingReceiver, IntentFilter(MessagingBroadcastReceiver.MESSAGING_ACTION))

        messagingReceiver.event.observe(this, {
            drawerFragment.updateMe()
        })
    }

    override fun onDestroy() {
        unregisterReceiver(sessionExpireReceiver)
        unregisterReceiver(messagingReceiver)

        super.onDestroy()
    }

    private fun logout() {
        AlertDialog
            .Builder(this)
            .setTitle(getString(R.string.warning))
            .setCancelable(false)
            .setMessage(getString(R.string.error_session_expired))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                preferences.accessToken = ""
                navigation.navigateTo(this, Feature.Login)
            }.show()
    }

    private fun handleNotification(intent: Intent?) {
        intent?.let {
            if (it.hasExtra(EXTRA_PAYLOAD)) {
                notificationHandler.handleTransactionNotification(it)?.let { keyValuePairs ->
                    SuccessFragment.newInstance(keyValuePairs).also { fragment ->
                        fragment.show(supportFragmentManager, SUCCESS_FRAGMENT_TAG)
                    }
                }

                it.removeExtra(EXTRA_PAYLOAD)
            }
        }
    }

    override fun provideNavController() = navController

    companion object {
        private const val SUCCESS_FRAGMENT_TAG = "success"
    }
}
