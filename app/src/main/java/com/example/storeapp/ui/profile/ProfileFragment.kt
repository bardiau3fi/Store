package com.example.storeapp.ui.profile

import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentProfileBinding
import com.example.storeapp.utils.AVATAR
import com.example.storeapp.utils.CANCELED
import com.example.storeapp.utils.DELIVERED
import com.example.storeapp.utils.METHOD
import com.example.storeapp.utils.MULTIPART_FROM_DATA
import com.example.storeapp.utils.PENDING
import com.example.storeapp.utils.POST
import com.example.storeapp.utils.UTF_8
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.events.EventBus
import com.example.storeapp.utils.events.Events
import com.example.storeapp.utils.extensions.getRealFileFromUri
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.extensions.loadImage
import com.example.storeapp.utils.extensions.moneySeparating
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.ProfileViewModel
import com.example.storeapp.viewmodel.WalletViewModel
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.app.imagepickerlibrary.ImagePicker
import com.app.imagepickerlibrary.ImagePicker.Companion.registerImagePicker
import com.app.imagepickerlibrary.listener.ImagePickerResultListener
import com.app.imagepickerlibrary.model.PickExtension
import com.app.imagepickerlibrary.model.PickerType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.net.URLEncoder

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), ImagePickerResultListener {
    //Binding
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    //Other
    private val viewModel by activityViewModels<ProfileViewModel>()
    private val walletViewModel by viewModels<WalletViewModel>()
    private val imagePicker: ImagePicker by lazy { registerImagePicker(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Init views
        binding.apply {
            //Choose image
            avatarEditImg.setOnClickListener {
                openImagePicker()
            }
            //Menu items
            menuLay.apply {
                menuEditLay.setOnClickListener { findNavController().navigate(R.id.actionToEditProfile) }
                menuWalletLay.setOnClickListener { findNavController().navigate(R.id.actionToIncreaseWallet) }
                menuCommentsLay.setOnClickListener { findNavController().navigate(R.id.actionToProfileComments) }
                menuFavoritesLay.setOnClickListener { findNavController().navigate(R.id.actionToProfileFavorites) }
                menuAddressesLay.setOnClickListener { findNavController().navigate(R.id.actionToProfileAddresses) }
            }
            //Order
            orderLay.apply {
                menuDeliveredLay.setOnClickListener {
                    navigateToOrderPage(DELIVERED)
                }
                menuPendingLay.setOnClickListener {
                    navigateToOrderPage(PENDING)
                }
                menuCanceledLay.setOnClickListener {
                    navigateToOrderPage(CANCELED)
                }
            }
        }
        //Auto update profile
        lifecycleScope.launch {
            EventBus.subscribe<Events.IsUpdateProfile> {
                if (isNetworkAvailable)
                    viewModel.callProfileApi()
            }
        }
        //Load data
        loadProfileData()
        loadWalletData()
        loadLoadAvatarData()
    }

    override fun onResume() {
        super.onResume()
        walletViewModel.callWalletApi()
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
                            //Avatar
                            if (data.avatar != null) {
                                avatarImg.loadImage(data.avatar)
                            } else {
                                avatarImg.load(R.drawable.placeholder_user)
                            }
                            //Name
                            if (data.firstname.isNullOrEmpty().not())
                                nameTxt.text = "${data.firstname} ${data.lastname}"
                            //Info
                            infoLay.apply {
                                phoneTxt.text = data.cellphone
                                //Birthdate
                                if (data.birthDate!!.isNotEmpty()) {
                                    birthDateTxt.text = data.birthDate.split("T")[0]
                                        .replace("-", " / ")
                                } else {
                                    infoBirthDateLay.isVisible = false
                                    line2.isVisible = false
                                }
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

    private fun loadWalletData() {
        binding.infoLay.apply {
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

    private fun openImagePicker() {
        imagePicker
            .title(getString(R.string.galleryImages))
            .multipleSelection(false)
            .showFolder(true)
            .cameraIcon(true)
            .doneIcon(true)
            .allowCropping(true)
            .compressImage(false)
            .maxImageSize(2.5f)
            .extension(PickExtension.ALL)
        imagePicker.open(PickerType.GALLERY)
    }

    override fun onImagePick(uri: Uri?) {
        val imageFile = getRealFileFromUri(requireContext(), uri!!)?.let { path -> File(path) }

        val multipart = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(METHOD, POST)

        //Image body
        if (imageFile != null) {
            val fileName = URLEncoder.encode(imageFile.absolutePath, UTF_8)
            val reqFile = imageFile.asRequestBody(MULTIPART_FROM_DATA.toMediaTypeOrNull())
            multipart.addFormDataPart(AVATAR, fileName, reqFile)
        }
        //Call api
        val requestBody = multipart.build()
        if (isNetworkAvailable) {
            viewModel.callUploadAvatarApi(requestBody)
        }
    }

    override fun onMultiImagePick(uris: List<Uri>?) {

    }

    private fun loadLoadAvatarData() {
        binding.apply {
            viewModel.avatarData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        avatarLoading.isVisible = true
                    }

                    is NetworkRequest.Success -> {
                        avatarLoading.isVisible = false
                        if (isNetworkAvailable)
                            viewModel.callProfileApi()
                    }

                    is NetworkRequest.Error -> {
                        avatarLoading.isVisible = false
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun navigateToOrderPage(status: String) {
        val direction = ProfileFragmentDirections.actionToProfileOrders(status)
        findNavController().navigate(direction)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}