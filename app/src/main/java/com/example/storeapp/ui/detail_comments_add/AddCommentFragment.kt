package com.example.storeapp.ui.detail_comments_add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storeapp.R
import com.example.storeapp.data.models.comments.BodySendComment
import com.example.storeapp.databinding.FragmentAddCommentBinding
import com.example.storeapp.utils.PRODUCT_ID
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.DetailViewModel
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddCommentFragment : BottomSheetDialogFragment() {
    //Binding
    private var _binding: FragmentAddCommentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var body: BodySendComment

    //Theme
    override fun getTheme() = R.style.RemoveDialogBackground

    //Other
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddCommentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Close
            closeImg.setOnClickListener { this@AddCommentFragment.dismiss() }
            //Submit
            submitBtn.setOnClickListener {
                val rate = rateSlider.value.toInt().toString()
                val comment = commentEdt.text.toString()
                if (comment.isEmpty())
                    root.showSnackBar(getString(R.string.notEmptyComment))
                else {
                    body.rate = rate
                    body.comment = comment
                    viewModel.callAddNewComment(PRODUCT_ID, body)
                }
            }
        }
        //Load data
        loadSendCommentData()
    }

    private fun loadSendCommentData() {
        binding.apply {
            viewModel.sendCommentsData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        submitBtn.enableLoading(true)
                    }

                    is NetworkRequest.Success -> {
                        submitBtn.enableLoading(false)
                        response.data?.let { data ->
                            Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
                            this@AddCommentFragment.dismiss()
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.sendCommentsData.removeObservers(viewLifecycleOwner)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}