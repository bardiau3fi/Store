package com.example.storeapp.ui.shipping

import com.example.storeapp.R
import com.example.storeapp.data.models.address.BodySetAddressForShipping
import com.example.storeapp.data.models.shipping.BodyCoupon
import com.example.storeapp.data.models.shipping.ResponseShipping
import com.example.storeapp.databinding.DialogChangeAddressBinding
import com.example.storeapp.databinding.FragmentShippingBinding
import com.example.storeapp.ui.shipping.adapters.AddressesAdapter
import com.example.storeapp.ui.shipping.adapters.ShippingAdapter
import com.example.storeapp.utils.ENABLE
import com.example.storeapp.utils.PERCENT
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.extensions.moneySeparating
import com.example.storeapp.utils.extensions.openBrowser
import com.example.storeapp.utils.extensions.setupRecyclerview
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.extensions.transparentCorners
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.ShippingViewModel
import com.example.storeapp.viewmodel.WalletViewModel
import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShippingFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentShippingBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var shippingAdapter: ShippingAdapter

    @Inject
    lateinit var addressesAdapter: AddressesAdapter

    @Inject
    lateinit var bodySetAddress: BodySetAddressForShipping

    @Inject
    lateinit var bodyCoupon: BodyCoupon

    //Other
    private val viewModel by viewModels<ShippingViewModel>()
    private val walletViewModel by viewModels<WalletViewModel>()
    private var coupon = ""
    private var finalPrice = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShippingBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Call api
        if (isNetworkAvailable) {
            viewModel.callShippingApi()
            walletViewModel.callWalletApi()
        }
        //InitViews
        binding.apply {
            //Toolbar
            toolbar.apply {
                toolbarTitleTxt.text = getString(R.string.invoiceWithDeliveryPrice)
                toolbarOptionImg.isVisible = false
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
            }
            //Discount
            shippingDiscountLay.apply {
                checkTxt.setOnClickListener {
                    coupon = codeEdt.text.toString()
                    bodyCoupon.couponId = coupon
                    if (isNetworkAvailable)
                        viewModel.callCheckCouponApi(bodyCoupon)
                }
                //Ato scroll
                codeEdt.setOnTouchListener { view, _ ->
                    view.performClick()
                    binding.scrollLay.postDelayed({
                        binding.scrollLay.fullScroll(View.FOCUS_DOWN)
                    }, 300)
                    false
                }
            }
        }
        //Load data
        loadShippingData()
        loadWalletData()
        loadCouponData()
        loadPaymentData()
    }

    private fun loadShippingData() {
        binding.apply {
            viewModel.shippingData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible(true, containerGroup)
                    }

                    is NetworkRequest.Success -> {
                        loading.isVisible(false, containerGroup)
                        response.data?.let { data ->
                            initShippingViews(data)
                        }
                    }

                    is NetworkRequest.Error -> {
                        loading.isVisible(false, containerGroup)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadCouponData() {
        binding.shippingDiscountLay.apply {
            viewModel.couponData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        couponLoading.isVisible(true, checkTxt)
                    }

                    is NetworkRequest.Success -> {
                        couponLoading.isVisible(false, checkTxt)
                        response.data?.let { data ->
                            checkTxt.isVisible = false
                            removeTxt.isVisible = true
                            couponTitle.text = "${getString(R.string.discountCode)} (${data.title})"
                            //Status
                            if (data.status == ENABLE) {
                                coupon = data.code!!
                                bodyCoupon.couponId = coupon
                                //Type
                                val discountPrice = if (data.type == PERCENT) {
                                    finalPrice - ((finalPrice * data.percent.toString().toInt()) / 100)
                                } else {
                                    finalPrice - data.percent.toString().toInt()
                                }
                                binding.invoiceTitle.text = discountPrice.moneySeparating()
                                //Remove
                                removeTxt.setOnClickListener {
                                    checkTxt.isVisible = true
                                    removeTxt.isVisible = false
                                    codeEdt.setText("")
                                    couponTitle.text = getString(R.string.discountCode)
                                    coupon = ""
                                    bodyCoupon.couponId = null
                                    binding.invoiceTitle.text = finalPrice.moneySeparating()
                                }
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        bodyCoupon.couponId = null
                        couponLoading.isVisible(false, checkTxt)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initShippingViews(data: ResponseShipping) {
        binding.apply {
            //Order
            data.order?.let { order ->
                finalPrice = order.finalPrice.toString().toInt()
                invoiceTitle.text = order.finalPrice.toString().toInt().moneySeparating()
                //Orders list
                initRecycler(order.orderItems)
            }
            //Addresses
            if (data.addresses.isNullOrEmpty().not()) {
                data.addresses?.get(0)?.let { address ->
                    setAddressData(address)
                }
                //More address
                if (data.addresses!!.size > 1) {
                    shippingAddressLay.changeAddressTxt.apply {
                        isVisible = true
                        setOnClickListener {
                            showChangeAddressDialog(data.addresses)
                        }
                    }
                }
            }
            //Payment
            submitBtn.setOnClickListener {
                if (data.addresses.isNullOrEmpty().not()) {
                    if (isNetworkAvailable)
                        viewModel.callPaymentApi(bodyCoupon)
                } else {
                    root.showSnackBar(getString(R.string.addressCanNotBeEmpty))
                }
            }
        }
    }

    private fun showChangeAddressDialog(list: List<ResponseShipping.Addresse>) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogChangeAddressBinding.inflate(layoutInflater)
        dialog.transparentCorners()
        dialog.setContentView(dialogBinding.root)
        addressesAdapter.setData(list)
        dialogBinding.addressList.setupRecyclerview(LinearLayoutManager(requireContext()), addressesAdapter)
        //Click
        addressesAdapter.setOnItemCLickListener { address ->
            setAddressData(address)
            //Dismiss
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setAddressData(address: ResponseShipping.Addresse) {
        //Body
        bodySetAddress.addressId = address.id.toString()
        //Set data
        binding.shippingAddressLay.apply {
            recipientNameTxt.text = "${address.receiverFirstname} ${address.receiverLastname}"
            locationTxt.text = address.postalAddress
            phoneTxt.text = address.receiverCellphone
        }
        //Call set address
        if (isNetworkAvailable)
            viewModel.callSetAddressApi(bodySetAddress)
    }

    private fun initRecycler(list: List<ResponseShipping.Order.OrderItem>) {
        shippingAdapter.setData(list)
        binding.productsList.setupRecyclerview(LinearLayoutManager(requireContext()), shippingAdapter)
    }

    private fun loadWalletData() {
        binding.apply {
            walletViewModel.walletData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        walletLoading.isVisible(true, walletTxt)
                    }

                    is NetworkRequest.Success -> {
                        walletLoading.isVisible(false, walletTxt)
                        response.data?.let { data ->
                            walletTxt.text = data.wallet.toString().toInt().moneySeparating()
                        }
                    }

                    is NetworkRequest.Error -> {
                        walletLoading.isVisible(false, walletTxt)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun loadPaymentData() {
        binding.apply {
            viewModel.paymentData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        submitBtn.enableLoading(true)
                    }

                    is NetworkRequest.Success -> {
                        submitBtn.enableLoading(false)
                        response.data?.let { data ->
                            if (data.startPay != null)
                                Uri.parse(data.startPay).openBrowser(requireContext())
                            else
                                root.showSnackBar(data.message!!)
                            //Close
                            findNavController().popBackStack(R.id.shippingFragment, true)
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