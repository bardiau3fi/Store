package com.example.storeapp.ui.cart

import com.example.storeapp.R
import com.example.storeapp.data.models.cart.ResponseCartList
import com.example.storeapp.databinding.FragmentCartBinding
import com.example.storeapp.utils.DECREMENT
import com.example.storeapp.utils.DELETE
import com.example.storeapp.utils.INCREMENT
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.events.EventBus
import com.example.storeapp.utils.events.Events
import com.example.storeapp.utils.extensions.moneySeparating
import com.example.storeapp.utils.extensions.setupRecyclerview
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.CartViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var cartAdapter: CartAdapter

    //Other
    private val viewModel by viewModels<CartViewModel>()
    private var recyclerViewState: Parcelable? = null
    private var isNavigateToShipping = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Call api
        if (isNetworkAvailable) {
            viewModel.callCartListApi()
        }
        //InitViews
        binding.apply {
            //Fab animation
            cartsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 && continueFABtn.isExtended)
                        continueFABtn.shrink()
                    else if (dy < 0 && !continueFABtn.isExtended)
                        continueFABtn.extend()
                }
            })
        }
        //Load data
        loadCartData()
        loadUpdateCartData()
        loadCartContinueData()
    }

    override fun onResume() {
        super.onResume()
        //Update badge
        lifecycleScope.launch {
            EventBus.publish(Events.IsUpdateCart)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadCartData() {
        binding.apply {
            viewModel.cartListData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible = true
                    }

                    is NetworkRequest.Success -> {
                        loading.isVisible = false
                        response.data?.let { data ->
                            if (data.orderItems != null) {
                                if (data.orderItems.isNotEmpty()) {
                                    emptyLay.isVisible = false
                                    cartsList.isVisible = true
                                    continueFABtn.isVisible = true
                                    //Continue
                                    continueFABtn.setOnClickListener {
                                        isNavigateToShipping = true
                                        if (isNetworkAvailable)
                                            viewModel.callCartContinueApi()
                                    }
                                    //Toolbar txt
                                    toolbarPriceTxt.text = data.itemsPrice.toString().toInt().moneySeparating()
                                    initRecyclerView(data.orderItems)
                                } else {
                                    emptyLay.isVisible = true
                                    cartsList.isVisible = false
                                    continueFABtn.isVisible = false
                                    toolbarPriceTxt.text = "0 ${getString(R.string.toman)}"
                                }
                            } else {
                                emptyLay.isVisible = true
                                cartsList.isVisible = false
                                continueFABtn.isVisible = false
                                toolbarPriceTxt.text = "0 ${getString(R.string.toman)}"
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        loading.isVisible = false
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun loadUpdateCartData() {
        binding.apply {
            viewModel.updateCartData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {}

                    is NetworkRequest.Success -> {
                        response.data?.let {
                            if (isNetworkAvailable)
                                viewModel.callCartListApi()
                            //Update badge
                            lifecycleScope.launch {
                                EventBus.publish(Events.IsUpdateCart)
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

    private fun loadCartContinueData() {
        binding.apply {
            viewModel.cartContinueData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {}

                    is NetworkRequest.Success -> {
                        response.data?.let {
                            //Navigate
                            if (isNavigateToShipping)
                                findNavController().navigate(R.id.actionToShipping)
                            isNavigateToShipping = false
                        }
                    }

                    is NetworkRequest.Error -> {
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initRecyclerView(list: List<ResponseCartList.OrderItem>) {
        cartAdapter.setData(list)
        val linearlayoutManager = LinearLayoutManager(requireContext())
        binding.apply {
            cartsList.setupRecyclerview(linearlayoutManager, cartAdapter)
            //Auto scroll
            cartsList.layoutManager?.onRestoreInstanceState(recyclerViewState)
            //Click
            cartAdapter.setOnItemClickListener { id, type ->
                //Save state
                recyclerViewState = cartsList.layoutManager?.onSaveInstanceState()
                when (type) {
                    INCREMENT -> {
                        if (isNetworkAvailable)
                            viewModel.callIncrementCartApi(id)
                    }

                    DECREMENT -> {
                        if (isNetworkAvailable)
                            viewModel.callDecrementDataCartApi(id)
                    }

                    DELETE -> {
                        if (isNetworkAvailable)
                            viewModel.callDeleteProductApi(id)
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