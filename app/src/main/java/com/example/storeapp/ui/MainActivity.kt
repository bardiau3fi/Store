package com.example.storeapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.storeapp.R
import com.example.storeapp.data.stored.SessionManager
import com.example.storeapp.databinding.ActivityMainBinding
import com.example.storeapp.utils.base.BaseActivity
import com.example.storeapp.utils.events.EventBus
import com.example.storeapp.utils.events.Events
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkChecker
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.utils.otp.AppSignatureHelper
import com.example.storeapp.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var signatureHelper: AppSignatureHelper

    @Inject
    lateinit var networkChecker: NetworkChecker

    @Inject
    lateinit var sessionManager: SessionManager

    //Other
    private val viewModelCart by viewModels<CartViewModel>()
    private lateinit var navHost: NavHostFragment
    private var isNetworkAvailable = true
    var hashCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Init nav host
        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        //Bottom nav menu
        binding.bottomNav.apply {
            setupWithNavController(navHost.navController)
            //Disable double click on items
            setOnNavigationItemReselectedListener {}
        }
        //Gone bottom menu
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.apply {
                when (destination.id) {

                    R.id.homeFragment -> bottomNav.isVisible= true
                    R.id.cartFragment -> bottomNav.isVisible = true
                    R.id.categoriesFragment -> bottomNav.isVisible = true
                    R.id.profileFragment -> bottomNav.isVisible = true
                    else -> bottomNav.isVisible = false
                }
            }
        }
        //Generate Hashcode
        signatureHelper.appSignatures.forEach {
            hashCode = it
            Log.e("HashcodeLogs", "Hashcode : $hashCode")
        }
        //Check network
        lifecycleScope.launch {
            networkChecker.checkNetwork().collect {
                isNetworkAvailable = it
            }
        }
        //Update badge
        lifecycleScope.launch {
            EventBus.subscribe<Events.IsUpdateCart> {
                viewModelCart.callCartListApi()
            }
        }
        lifecycleScope.launch {
            delay(200)
            sessionManager.getToken.collect { token ->
                token?.let {
                    if (isNetworkAvailable) {
                        viewModelCart.callCartListApi()
                    }
                }
            }
        }
        //Load data
        loadCartData()
    }

    private fun loadCartData() {
        binding.apply {
            viewModelCart.cartListData.observe(this@MainActivity) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                    }

                    is NetworkRequest.Success -> {
                        response.data?.let { data ->
                            if (data.quantity != null) {
                                if (data.quantity.toString().toInt() > 0) {
                                    bottomNav.getOrCreateBadge(R.id.cartFragment).apply {
                                        number = data.quantity.toString().toInt()
                                        backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.caribbeanGreen)
                                    }
                                } else {
                                    bottomNav.removeBadge(R.id.cartFragment)
                                }
                            } else {
                                bottomNav.removeBadge(R.id.cartFragment)
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        return navHost.navController.navigateUp() || super.onNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}