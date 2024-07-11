package com.example.storeapp.ui.login

import com.example.storeapp.R
import com.example.storeapp.data.models.login.BodyLogin
import com.example.storeapp.databinding.FragmentLoginPhoneBinding
import com.example.storeapp.utils.IS_CALLED_VERIFY
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.hideKeyboard
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.LoginViewModel
import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.storeapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginPhoneFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentLoginPhoneBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var body: BodyLogin

    //Other
    private val viewModel by viewModels<LoginViewModel>()
    private val parentActivity by lazy { (activity as MainActivity) }
    private var phone = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginPhoneBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Bottom image
            bottomImg.load(R.drawable.bg_circle)
            //Get hash code
            body.hashCode = parentActivity.hashCode
            //Click
            sendPhoneBtn.setOnClickListener {
                root.hideKeyboard()
                //Phone
                phone = phoneEdt.text.toString()
                if (phone.length == 11) {
                    body.login = phone
                    if (isNetworkAvailable) {
                        IS_CALLED_VERIFY = true
                        viewModel.callLoginApi(body)
                    }
                }
            }
        }
        //Load data
        loadLoginData()
        handleAnimation()
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

    private fun loadLoginData() {
        binding.apply {
            viewModel.loginData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        sendPhoneBtn.enableLoading(true)
                    }

                    is NetworkRequest.Success -> {
                        sendPhoneBtn.enableLoading(false)
                        response.data?.let {
                            if (IS_CALLED_VERIFY) {
                                val direction = LoginPhoneFragmentDirections.actionPhoneToVerify().setPhone(phone)
                                findNavController().navigate(direction)
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        sendPhoneBtn.enableLoading(false)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}