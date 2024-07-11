package com.example.storeapp.ui.profile_favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storeapp.R
import com.example.storeapp.data.models.profile_favorite.ResponseProfileFavorites
import com.example.storeapp.databinding.FragmentProfileFavoriteBinding
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.setupRecyclerview
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.ProfileFavoritesViewModel
import android.os.Parcelable
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFavoriteFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentProfileFavoriteBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var favoritesAdapter: FavoritesAdapter

    //Other
    private val viewModel by viewModels<ProfileFavoritesViewModel>()
    private var recyclerviewState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Call api
        if (isNetworkAvailable)
            viewModel.callFavoritesApi()
        //InitViews
        binding.apply {
            //Toolbar
            toolbar.apply {
                toolbarTitleTxt.text = getString(R.string.yourFavorites)
                toolbarOptionImg.isVisible = false
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
            }
        }
        //Load data
        loadFavoritesData()
        loadDeleteFavoriteData()
    }

    private fun loadFavoritesData() {
        binding.apply {
            viewModel.favoritesData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        favoritesList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        favoritesList.hideShimmer()
                        response.data?.let { data ->
                            if (data.data.isNotEmpty()) {
                                initRecycler(data.data)
                            } else {
                                emptyLay.isVisible = true
                                favoritesList.isVisible = false
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        favoritesList.hideShimmer()
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun loadDeleteFavoriteData() {
        binding.apply {
            viewModel.deleteFavoriteData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {}

                    is NetworkRequest.Success -> {
                        response.data?.let {
                            if (isNetworkAvailable)
                                viewModel.callFavoritesApi()
                        }
                    }

                    is NetworkRequest.Error -> {
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initRecycler(data: List<ResponseProfileFavorites.Data>) {
        binding.apply {
            favoritesAdapter.setData(data)
            favoritesList.setupRecyclerview(LinearLayoutManager(requireContext()), favoritesAdapter)
            //Auto scroll
            favoritesList.layoutManager?.onRestoreInstanceState(recyclerviewState)
            //Click
            favoritesAdapter.setOnItemClickListener {
                //Save state
                recyclerviewState = favoritesList.layoutManager?.onSaveInstanceState()
                //Call delete api
                if (isNetworkAvailable)
                    viewModel.callDeleteFavoriteApi(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}