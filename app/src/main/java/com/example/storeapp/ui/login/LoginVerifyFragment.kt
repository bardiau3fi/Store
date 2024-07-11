package com.example.storeapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storeapp.R
import com.example.storeapp.data.models.login.BodyLogin
import com.example.storeapp.data.stored.SessionManager
import com.example.storeapp.databinding.FragmentLoginVerifyBinding
import com.example.storeapp.utils.IS_CALLED_VERIFY
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.hideKeyboard
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.utils.otp.SMSBroadcastReceiver
import com.example.storeapp.viewmodel.LoginViewModel
import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.goodiebag.pinview.Pinview
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginVerifyFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentLoginVerifyBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var smsReceiver: SMSBroadcastReceiver

    @Inject
    lateinit var body: BodyLogin

    @Inject
    lateinit var sessionManager: SessionManager

    //Other
    private val viewModel by viewModels<LoginViewModel>()
    private val args by navArgs<LoginVerifyFragmentArgs>()
    private var intentFilter: IntentFilter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginVerifyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        IS_CALLED_VERIFY = false
        //Args
        args.let {
            body.login = it.phone
        }
        //InitViews
        binding.apply {
            //Bottom image
            bottomImg.load(R.drawable.bg_circle)
            //Customize pin view text color
            pinView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            //Complete code
            pinView.setPinViewEventListener(object : Pinview.PinViewEventListener {
                override fun onDataEntered(pinview: Pinview?, fromUser: Boolean) {
                    body.code = pinview?.value?.toInt()
                    if (isNetworkAvailable)
                        viewModel.callVerifyApi(body)
                }
            })
            //Send again code
            sendAgainBtn.setOnClickListener {
                if (isNetworkAvailable)
                    viewModel.callLoginApi(body)
                handleTimer().cancel()
            }
        }
        //Start timer
        lifecycleScope.launch {
            delay(500)
            handleTimer().start()
        }
        //Load data
        handleAnimation()
        loadLoginData()
        loadVerifyData()
        //Receive Code
        initBroadcast()
        smsListener()
    }

    private fun handleTimer(): CountDownTimer {
        binding.apply {
            return object : CountDownTimer(60_000, 1_000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millis: Long) {
                    sendAgainBtn.isVisible = false
                    timerTxt.isVisible = true
                    timerProgress.isVisible = true
                    timerTxt.text = "${millis / 1000} ${getString(R.string.second)}"
                    timerProgress.setProgressCompat((millis / 1000).toInt(), true)
                    if (millis < 1000)
                        timerProgress.progress = 0
                }

                override fun onFinish() {
                    sendAgainBtn.isVisible = true
                    timerTxt.isVisible = false
                    timerProgress.isVisible = false
                    timerProgress.progress = 0
                }
            }
        }
    }

    private fun loadLoginData() {
        binding.apply {
            viewModel.loginData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                            sendAgainBtn.enableLoading(true)
                    }

                    is NetworkRequest.Success -> {
                        sendAgainBtn.enableLoading(false)
                        response.data?.let {
                            handleTimer().start()
                        }
                    }

                    is NetworkRequest.Error -> {
                        sendAgainBtn.enableLoading(false)
                        root.showSnackBar(response.error!!)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun loadVerifyData() {
        binding.apply {
            viewModel.verifyData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        timerLay.alpha = 0.3f
                    }

                    is NetworkRequest.Success -> {
                        timerLay.alpha = 1.0f
                        response.data?.let { data ->
                            lifecycleScope.launch {
                                sessionManager.saveToken(data.accessToken.toString())
                            }
                            root.hideKeyboard()
                            findNavController().popBackStack(R.id.loginVerifyFragment, true)
                            findNavController().popBackStack(R.id.loginPhoneFragment, true)
                            //Home
                            findNavController().navigate(R.id.actionToMain)
                        }
                    }

                    is NetworkRequest.Error -> {
                        timerLay.alpha = 1.0f
                        root.showSnackBar(response.error!!)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun handleAnimation() {
        binding.animationView.apply {
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    lifecycleScope.launch {
                        delay(2000)
                        playAnimation()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
    }

    private fun initBroadcast() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsReceiver.onReceiveMessage {
            val code = it.split(":")[1].trim().subSequence(0, 4)
            binding.pinView.value = code.toString()
        }
    }

    private fun smsListener() {
        val client = SmsRetriever.getClient(requireActivity())
        client.startSmsRetriever()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(smsReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onStop() {
        super.onStop()
        handleTimer().cancel()
        requireContext().unregisterReceiver(smsReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}