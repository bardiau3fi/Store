package com.example.storeapp.ui.profile_orders

import com.example.storeapp.R
import com.example.storeapp.data.models.profile_order.ResponseProfileOrdersList
import com.example.storeapp.databinding.FragmentProfileOrdersBinding
import com.example.storeapp.ui.profile_orders.adapters.OrdersAdapter
import com.example.storeapp.utils.CANCELED
import com.example.storeapp.utils.DELIVERED
import com.example.storeapp.utils.PENDING
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.setupRecyclerview
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.ProfileOrdersViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileOrdersFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentProfileOrdersBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var ordersAdapter: OrdersAdapter

    //Other
    private val viewModel by viewModels<ProfileOrdersViewModel>()
    private val args by navArgs<ProfileOrdersFragmentArgs>()
    private var status = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileOrdersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Args
        args.status.let {
         status= it
        }
        //Call api
        if (isNetworkAvailable)
            viewModel.callOrdersApi(status)
        //Init views
        binding.apply {
            //Toolbar
            toolbar.apply {
                toolbarTitleTxt.text = when (status) {
                    DELIVERED -> getString(R.string.delivered)
                    PENDING -> getString(R.string.pending)
                    CANCELED -> getString(R.string.canceled)
                    else -> ""

                }
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                toolbarOptionImg.isVisible = false
            }
        }
        //Load data
        loadOrdersData()
    }

    private fun loadOrdersData() {
        binding.apply {
            viewModel.ordersData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        ordersList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        ordersList.hideShimmer()
                        response.data?.let { data ->
                            if (data.data.isNotEmpty()) {
                                initRecycler(data.data)
                            } else {
                                emptyLay.isVisible = true
                                ordersList.isVisible = false
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        ordersList.hideShimmer()
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initRecycler(data: List<ResponseProfileOrdersList.Data>) {
        binding.apply {
            ordersAdapter.setData(data)
            ordersList.setupRecyclerview(LinearLayoutManager(requireContext()), ordersAdapter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}