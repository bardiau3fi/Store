package com.example.storeapp.ui.categories

import com.example.storeapp.R
import com.example.storeapp.data.models.categories.ResponseCategories.ResponseCategoriesItem
import com.example.storeapp.databinding.FragmentCategoriesBinding
import com.example.storeapp.ui.categories.adapters.CategoryAdapter
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.setupRecyclerview
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.CategoriesViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    //Other
    private val viewModel by activityViewModels<CategoriesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Init views
        binding.apply {
            //Toolbar
            toolbar.apply {
                //Visibility
                toolbarBackImg.isVisible = false
                toolbarOptionImg.isVisible = false
                //Title
                toolbarTitleTxt.text = getString(R.string.categories)
            }
        }
        //Load data
        loadCategoriesData()
    }

    private fun loadCategoriesData() {
        binding.apply {
            viewModel.categoriesData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        categoriesList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        categoriesList.hideShimmer()
                        response.data?.let { data ->
                            initCategoriesRecycler(data)
                        }
                    }

                    is NetworkRequest.Error -> {
                        categoriesList.hideShimmer()
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initCategoriesRecycler(data: List<ResponseCategoriesItem>) {
        categoryAdapter.setData(data.dropLast(1))
        binding.categoriesList.setupRecyclerview(LinearLayoutManager(requireContext()), categoryAdapter)
        //Click
        categoryAdapter.setOnItemClickListener {
            val direction = CategoriesFragmentDirections.actionToCategoriesProduct(it)
            findNavController().navigate(direction)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}