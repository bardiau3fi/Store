package com.example.storeapp.ui.home

import com.example.storeapp.R
import com.example.storeapp.data.models.home.ResponseBanners.ResponseBannersItem
import com.example.storeapp.data.models.home.ResponseDiscount.ResponseDiscountItem
import com.example.storeapp.data.models.home.ResponseProducts
import com.example.storeapp.data.models.home.ResponseProducts.SubCategory.Products.Data
import com.example.storeapp.databinding.DialogCheckVpnBinding
import com.example.storeapp.databinding.FragmentHomeBinding
import com.example.storeapp.ui.home.adapters.BannerAdapter
import com.example.storeapp.ui.home.adapters.DiscountAdapter
import com.example.storeapp.ui.home.adapters.ProductsAdapter
import com.example.storeapp.utils.PRODUCT
import com.example.storeapp.utils.ProductsCategories
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.extensions.loadImage
import com.example.storeapp.utils.extensions.setupRecyclerview
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.extensions.transparentCorners
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.HomeViewModel
import com.example.storeapp.viewmodel.ProfileViewModel
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import coil.load
import com.example.storeapp.ui.categories.CategoriesFragmentDirections
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var bannerAdapter: BannerAdapter

    @Inject
    lateinit var discountAdapter: DiscountAdapter

    @Inject
    lateinit var mobileProductsAdapter: ProductsAdapter

    @Inject
    lateinit var shoesProductsAdapter: ProductsAdapter

    @Inject
    lateinit var stationeryProductsAdapter: ProductsAdapter

    @Inject
    lateinit var laptopProductsAdapter: ProductsAdapter

    @Inject
    lateinit var checkVpn: Flow<Boolean>

    //Other
    private val viewModel by activityViewModels<HomeViewModel>()
    private val profileViewModel by activityViewModels<ProfileViewModel>()
    private val pagerSnapHelper by lazy { PagerSnapHelper() }
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Navigate to profile
            avatarImg.setOnClickListener { findNavController().navigate(R.id.actionHomeToProfile) }
            //Navigate to search
            searchImg.setOnClickListener { findNavController().navigate(R.id.actionToSearch) }
            //Restore last state of scroll
            if (viewModel.lastStateOfScroll != null) {
                scrollLay.onRestoreInstanceState(viewModel.lastStateOfScroll)
            }
        }
        //Load data
        loadProfileData()
        loadBannerData()
        loadDiscountData()
        loadProductsData()
        //Check VPN
        lifecycleScope.launch {
            checkVpn.collect {
                showVpnDialog()
            }
        }
    }

    private fun loadProfileData() {
        binding.apply {
            profileViewModel.profileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        avatarLoading.isVisible = true
                    }

                    is NetworkRequest.Success -> {
                        avatarLoading.isVisible = false
                        response.data?.let { data ->
                            //Avatar
                            if (data.avatar != null) {
                                avatarImg.loadImage(data.avatar)
                                avatarBadgeImg.isVisible = false
                            } else {
                                avatarImg.load(R.drawable.placeholder_user)
                                avatarBadgeImg.isVisible = true
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        avatarLoading.isVisible = false
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun loadBannerData() {
        binding.apply {
            viewModel.bannerData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        bannerLoading.isVisible(true, bannerList)
                    }

                    is NetworkRequest.Success -> {
                        bannerLoading.isVisible(false, bannerList)
                        response.data?.let { data ->
                            if (data.isNotEmpty()) {
                                initBannerRecycler(data)
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        bannerLoading.isVisible(false, bannerList)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initBannerRecycler(data: List<ResponseBannersItem>) {
        bannerAdapter.setData(data)
        binding.bannerList.apply {
            adapter = bannerAdapter
            set3DItem(true)
            setAlpha(true)
            setInfinite(false)
        }
        //Click
        bannerAdapter.setOnItemClickListener { sendData, type ->
            if (type == PRODUCT) {

              val direction = HomeFragmentDirections.actionToDetail(sendData.toInt())
                findNavController().navigate(direction)

            } else {
                val direction = CategoriesFragmentDirections.actionToCategories(sendData)
                findNavController().navigate(direction)

            }
        }
        //Indicator
        binding.apply {
            pagerSnapHelper.attachToRecyclerView(bannerList)
            bannerIndicator.attachToRecyclerView(bannerList, pagerSnapHelper)
        }
    }

    private fun loadDiscountData() {
        binding.apply {
            viewModel.discountData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        discountList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        discountList.hideShimmer()
                        response.data?.let { data ->
                            if (data.isNotEmpty()) {
                                initDiscountRecycler(data)
                                //Discount
                                data[0].endTime?.let {
                                    val endTime = it.split("T")[0]
                                    discountTimer(endTime)
                                    countDownTimer.start()
                                }
                            } else {
                                discountCard.isVisible = false
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        discountList.hideShimmer()
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initDiscountRecycler(data: List<ResponseDiscountItem>) {
        discountAdapter.setData(data)
        binding.discountList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true), discountAdapter
        )
        //Click
        discountAdapter.setOnItemClickListener {
            val direction = HomeFragmentDirections.actionToDetail(it)
            findNavController().navigate(direction)
        }
    }

    private fun discountTimer(fullDate: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date: Date = formatter.parse(fullDate) as Date
        val currentMillis = System.currentTimeMillis()
        val finalMillis = date.time - currentMillis
        countDownTimer = object : CountDownTimer(finalMillis, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                //Calculate time
                var timer = millisUntilFinished
                val day: Long = TimeUnit.MILLISECONDS.toDays(timer)
                timer -= TimeUnit.DAYS.toMillis(day)
                val hours: Long = TimeUnit.MILLISECONDS.toHours(timer)
                timer -= TimeUnit.HOURS.toMillis(hours)
                val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(timer)
                timer -= TimeUnit.MINUTES.toMillis(minutes)
                val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(timer)
                //View
                try {
                    binding.timerLay.apply {
                        if (day > 0) {
                            dayLay.isVisible = true
                            dayTxt.text = day.toString()
                        } else {
                            dayLay.isVisible = false
                        }
                        hourTxt.text = hours.toString()
                        minuteTxt.text = minutes.toString()
                        secondTxt.text = seconds.toString()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFinish() {

            }
        }
    }

    private fun loadProductsData() {
        binding.apply {
            //Mobile
            if (mobileLay.parent != null) {
                    val mobileInflate = mobileLay.inflate()
                    viewModel.getProductsData(ProductsCategories.MOBILE).observe(viewLifecycleOwner) {
                    handleProductsRequest(it, mobileInflate.findViewById(R.id.mobileProductsList), mobileProductsAdapter)
                }
            }
            //Shoes
            if (shoesLay.parent != null) {
                val shoesInflate = shoesLay.inflate()
                viewModel.getProductsData(ProductsCategories.SHOES).observe(viewLifecycleOwner) {
                    handleProductsRequest(it, shoesInflate.findViewById(R.id.menShoesProductsList), shoesProductsAdapter)
                }
            }
            //Stationery
            val stationeryInflate = stationeryLay.inflate()
            viewModel.getProductsData(ProductsCategories.STATIONERY).observe(viewLifecycleOwner) {
                handleProductsRequest(it, stationeryInflate.findViewById(R.id.stationeryProductsList), stationeryProductsAdapter)
            }
            //Laptop
            val laptopInflate = laptopLay.inflate()
            viewModel.getProductsData(ProductsCategories.LAPTOP).observe(viewLifecycleOwner) {
                handleProductsRequest(it, laptopInflate.findViewById(R.id.laptopProductsList), laptopProductsAdapter)
            }
        }
    }

    private fun handleProductsRequest(
        request: NetworkRequest<ResponseProducts>, recyclerView: ShimmerRecyclerView, adapter: ProductsAdapter
    ) {
        when (request) {
            is NetworkRequest.Loading -> {
                recyclerView.showShimmer()
            }

            is NetworkRequest.Success -> {
                recyclerView.hideShimmer()
                request.data?.let { data ->
                    data.subCategory?.let { subCats ->
                        subCats.products?.let { products ->
                            products.data?.let { myData ->
                                if (myData.isNotEmpty()) {
                                    initProductsRecyclers(myData, recyclerView, adapter)
                                }
                            }
                        }
                    }
                }
            }

            is NetworkRequest.Error -> {
                recyclerView.hideShimmer()
                binding.root.showSnackBar(request.error!!)
            }
        }
    }

    private fun initProductsRecyclers(data: List<Data>, recyclerView: ShimmerRecyclerView, adapter: ProductsAdapter) {
        adapter.setData(data)
        recyclerView.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true),
            adapter
        )
        //Click
        adapter.setOnItemClickListener {
            val direction = HomeFragmentDirections.actionToDetail(it)
            findNavController().navigate(direction)
        }
    }

    private fun showVpnDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogCheckVpnBinding.inflate(layoutInflater)
        dialog.transparentCorners() 
        dialog.setContentView(dialogBinding.root)

        dialogBinding.yesBtn.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        viewModel.lastStateOfScroll = binding.scrollLay.onSaveInstanceState()
    }

    override fun onStop() {
        super.onStop()
      //  countDownTimer.cancel()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
