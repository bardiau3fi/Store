package com.example.storeapp.ui.detail_comments

import com.example.storeapp.R
import com.example.storeapp.data.models.comments.ResponseCommentsList
import com.example.storeapp.databinding.FragmentDetailCommentsBinding
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.extensions.setupRecyclerview
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.DetailViewModel
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
class DetailCommentsFragment : BaseFragment() {
    //Binding

    private var _binding: FragmentDetailCommentsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var commentsAdapter: CommentsAdapter

    //Other
    private val viewModel by activityViewModels<DetailViewModel>()
    private var productId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailCommentsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Get id
        viewModel.productID.observe(viewLifecycleOwner) {
            productId = it
            if (isNetworkAvailable)
                viewModel.callProductComments(it)
        }
        //Add new comment
        binding.addNewCommentTxt.setOnClickListener {
            findNavController().navigate(R.id.actionDetailToAddComment)
        }
        //Load data
        loadCommentsData()
    }

    private fun loadCommentsData() {
        binding.apply {
            viewModel.commentsData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        commentsLoading.isVisible(true, commentsList)
                    }

                    is NetworkRequest.Success -> {
                        commentsLoading.isVisible(false, commentsList)
                        response.data?.let { data ->
                            if (data.data!!.isNotEmpty()) {
                                initRecycler(data.data)
                                emptyLay.isVisible = false
                            } else {
                                emptyLay.isVisible = true
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        commentsLoading.isVisible(false, commentsList)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initRecycler(data: List<ResponseCommentsList.Data>) {
        commentsAdapter.setData(data)
        binding.commentsList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true), commentsAdapter
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}