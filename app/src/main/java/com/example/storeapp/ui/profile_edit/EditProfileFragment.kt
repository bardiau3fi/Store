package com.example.storeapp.ui.profile_edit

import com.example.storeapp.R
import com.example.storeapp.data.models.profile.BodyUpdateProfile
import com.example.storeapp.databinding.FragmentEditProfileBinding
import com.example.storeapp.utils.events.EventBus
import com.example.storeapp.utils.events.Events
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.ProfileViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : BottomSheetDialogFragment() {
    //Binding
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var body: BodyUpdateProfile

    //Theme
    override fun getTheme() = R.style.RemoveDialogBackground

    //Other
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Call api
        viewModel.callProfileApi()
        //Init views
        binding.apply {
            //Dismiss
            closeImg.setOnClickListener { this@EditProfileFragment.dismiss() }
            //Open date picker
            birthDateEdt.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    openDatePicker()
                }
                false
            }
            //Submit data
            submitBtn.setOnClickListener {
                if (nameEdt.text.isNullOrEmpty().not())
                    body.firstName = nameEdt.text.toString()
                if (familyEdt.text.isNullOrEmpty().not())
                    body.lastName = familyEdt.text.toString()
                if (idNumberEdt.text.isNullOrEmpty().not())
                    body.idNumber = idNumberEdt.text.toString()
                //Call api
                viewModel.callUpdateProfileApi(body)
            }
        }
        //Load data
        loadProfileData()
        loadUpdateProfileData()
    }

    @SuppressLint("SetTextI18n")
    private fun loadProfileData() {
        binding.apply {
            viewModel.profileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible = true
                    }

                    is NetworkRequest.Success -> {
                        loading.isVisible = false
                        response.data?.let { data ->
                            if (data.firstname.isNullOrEmpty().not())
                                nameEdt.setText(data.firstname)
                            if (data.lastname.isNullOrEmpty().not())
                                familyEdt.setText(data.lastname)
                            if (data.idNumber.isNullOrEmpty().not())
                                idNumberEdt.setText(data.idNumber)
                            if (data.birthDate.isNullOrEmpty().not())
                                birthDateEdt.setText(data.birthDate!!.split("T")[0])
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

    private fun loadUpdateProfileData() {
        binding.apply {
            viewModel.updateProfileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        submitBtn.enableLoading(true)
                    }

                    is NetworkRequest.Success -> {
                        submitBtn.enableLoading(false)
                        response.data?.let {
                            lifecycleScope.launch {
                                EventBus.publish(Events.IsUpdateProfile)
                            }
                            this@EditProfileFragment.dismiss()
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

    private fun openDatePicker() {
        PersianDatePickerDialog(requireContext())
            .setTodayButtonVisible(true)
            .setMinYear(1300)
            .setMaxYear(1400)
            .setInitDate(1370, 3, 13)
            .setTitleType(PersianDatePickerDialog.DAY_MONTH_YEAR)
            .setShowInBottomSheet(true)
            .setListener(object : PersianPickerListener {
                override fun onDateSelected(pDate: PersianPickerDate) {
                    val birthDate = "${pDate.gregorianYear}-${pDate.gregorianMonth}-${pDate.gregorianDay}"
                    val birthDatePersian = "${pDate.persianYear}-${pDate.persianMonth}-${pDate.persianDay}"
                    body.gregorianDate = birthDate
                    binding.birthDateEdt.setText(birthDatePersian)
                }

                override fun onDismissed() {}
            }).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}