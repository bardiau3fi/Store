package com.example.storeapp.ui.profile_address_add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storeapp.R
import com.example.storeapp.data.models.address.BodySubmitAddress
import com.example.storeapp.databinding.DialogDeleteAddressBinding
import com.example.storeapp.databinding.FragmentProfileAddressAddBinding
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.events.EventBus
import com.example.storeapp.utils.events.Events
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.setTint
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.extensions.transparentCorners
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.ProfileAddressViewModel
import android.app.Dialog
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileAddressAddFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentProfileAddressAddBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var body: BodySubmitAddress

    //Other
    private val viewModel by viewModels<ProfileAddressViewModel>()
    private val args by navArgs<ProfileAddressAddFragmentArgs>()
    private val provincesNamesList = mutableListOf<String>()
    private lateinit var provincesAdapter: ArrayAdapter<String>
    private var provinceId = 0
    private val citiesNamesList = mutableListOf<String>()
    private lateinit var citiesAdapter: ArrayAdapter<String>
    private var addressId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileAddressAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Call api
        if (isNetworkAvailable)
            viewModel.callProvincesListApi()
        //InitViews
        binding.apply {
            //Toolbar
            toolbar.apply {
                //Args
                if (args.data != null) {
                    toolbarTitleTxt.text = getString(R.string.editAddress)
                    toolbarOptionImg.apply {
                        setImageResource(R.drawable.trash_can)
                        setTint(R.color.red)
                        setOnClickListener { showDeleteAddressDialog() }
                    }
                    //Set data
                    args.data?.apply {
                        addressId = id!!
                        body.addressId = id.toString()
                        nameEdt.setText(receiverFirstname)
                        familyEdt.setText(receiverLastname)
                        phoneEdt.setText(receiverCellphone)
                        body.provinceId = province?.id.toString()
                        provinceAutoTxt.setText(province?.title)
                        cityInpLay.isVisible = true
                        body.cityId = cityId
                        cityAutoTxt.setText(city?.title)
                        floorEdt.setText(floor)
                        plateEdt.setText(plateNumber)
                        postalEdt.setText(postalCode)
                        addressEdt.setText(postalAddress)
                    }
                } else {
                    toolbarTitleTxt.text = getString(R.string.addNewAddress)
                    toolbarOptionImg.isVisible = false
                }
                //Back
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
            }
            //Submit
            submitBtn.setOnClickListener {
                body.receiverFirstname = nameEdt.text.toString()
                body.receiverLastname = familyEdt.text.toString()
                body.receiverCellphone = phoneEdt.text.toString()
                body.floor = floorEdt.text.toString()
                body.plateNumber = plateEdt.text.toString()
                body.postalCode = postalEdt.text.toString()
                body.postalAddress = addressEdt.text.toString()
                //Call api
                if (isNetworkAvailable)
                    viewModel.callSubmitAddressApi(body)
            }
        }
        //Load data
        loadProvincesData()
        loadCitiesData()
        loadSubmitAddressData()
        loadDeleteAddressData()
    }

    private fun loadProvincesData() {
        binding.apply {
            viewModel.provincesData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {}

                    is NetworkRequest.Success -> {
                        response.data?.let { data ->
                            if (data.isNotEmpty()) {
                                data.forEach {
                                    provincesNamesList.add(it.title!!)
                                }
                                provincesAdapter = ArrayAdapter<String>(
                                    requireContext(), R.layout.dropdown_menu_popup_item, provincesNamesList
                                )
                                provinceAutoTxt.apply {
                                    setAdapter(provincesAdapter)
                                    setOnItemClickListener { _, _, position, _ ->
                                        provinceId = data[position].id!!
                                        body.provinceId = provinceId.toString()
                                        if (isNetworkAvailable)
                                            viewModel.callCitiesListApi(provinceId)
                                    }
                                }
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

    private fun loadCitiesData() {
        binding.apply {
            viewModel.citiesData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {}

                    is NetworkRequest.Success -> {
                        response.data?.let { data ->
                            cityInpLay.isVisible = true
                            if (data.isNotEmpty()) {
                                citiesNamesList.clear()
                                data.forEach {
                                    citiesNamesList.add(it.title!!)
                                }
                                citiesAdapter = ArrayAdapter<String>(
                                    requireContext(), R.layout.dropdown_menu_popup_item, citiesNamesList
                                )
                                cityAutoTxt.apply {
                                    setAdapter(citiesAdapter)
                                    setOnItemClickListener { _, _, position, _ ->
                                        body.cityId = data[position].id!!.toString()
                                    }
                                }
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

    private fun loadSubmitAddressData() {
        binding.apply {
            viewModel.submitAddressData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        submitBtn.enableLoading(true)
                    }

                    is NetworkRequest.Success -> {
                        submitBtn.enableLoading(false)
                        response.data?.let {
                            lifecycleScope.launch { EventBus.publish(Events.IsUpdateAddress) }
                            findNavController().popBackStack()
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

    private fun loadDeleteAddressData() {
        binding.apply {
            viewModel.deleteAddressData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        submitBtn.enableLoading(true)
                    }

                    is NetworkRequest.Success -> {
                        submitBtn.enableLoading(false)
                        response.data?.let {
                            lifecycleScope.launch { EventBus.publish(Events.IsUpdateAddress) }
                            findNavController().popBackStack()
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

    private fun showDeleteAddressDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogDeleteAddressBinding.inflate(layoutInflater)
        dialog.transparentCorners()
        dialog.setContentView(dialogBinding.root)

        dialogBinding.noBtn.setOnClickListener { dialog.dismiss() }

        dialogBinding.yesBtn.setOnClickListener {
            dialog.dismiss()
            if (isNetworkAvailable)
                viewModel.callDeleteAddressApi(addressId)
        }

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}