package com.example.storeapp.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storeapp.R
import com.example.storeapp.data.models.wallet.BodyIncreaseWallet
import com.example.storeapp.databinding.FragmentIncreaseWalletBinding
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.moneySeparating
import com.example.storeapp.utils.extensions.openBrowser
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.WalletViewModel
import android.annotation.SuppressLint
import android.net.Uri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IncreaseWalletFragment : BottomSheetDialogFragment() {
    //Binding
    private var _binding: FragmentIncreaseWalletBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var body: BodyIncreaseWallet

    //Theme
    override fun getTheme() = R.style.RemoveDialogBackground

    //Other
    private val viewModel by viewModels<WalletViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentIncreaseWalletBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Init views
        binding.apply {
            //Close
            closeImg.setOnClickListener { this@IncreaseWalletFragment.dismiss() }
            //Money separating
            amountEdt.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    amountTxt.text = it.toString().trim().toInt().moneySeparating()
                } else {
                    amountTxt.text = ""
                }
            }
            //Click
            submitBtn.setOnClickListener {
                val amount = amountEdt.text.toString()
                if (amount.isNotEmpty()) {
                    body.amount = amount
                    viewModel.callIncreaseWalletApi(body)
                }
            }
        }
        //Load data
        loadWalletData()
    }

    private fun loadWalletData() {
        binding.apply {
            viewModel.increaseWalletData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        submitBtn.enableLoading(true)
                    }

                    is NetworkRequest.Success -> {
                        submitBtn.enableLoading(false)
                        response.data?.let { data ->
                            Uri.parse(data.startPay).openBrowser(requireContext())
                            this@IncreaseWalletFragment.dismiss()
                        }
                    }

                    is NetworkRequest.Error -> {
                        submitBtn.enableLoading(false)
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