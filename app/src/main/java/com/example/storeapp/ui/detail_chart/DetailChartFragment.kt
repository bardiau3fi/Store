package com.example.storeapp.ui.detail_chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storeapp.databinding.FragmentDetailChartBinding
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.extensions.setupMyChart
import com.example.storeapp.utils.extensions.showSnackBar
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.viewmodel.DetailViewModel
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailChartFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentDetailChartBinding? = null
    private val binding get() = _binding!!

    //Other
    private val viewModel by activityViewModels<DetailViewModel>()
    private val daysList = arrayListOf<String>()
    private val daysListForTooltip = arrayListOf<String>()
    private val pricesList = ArrayList<Entry>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailChartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Get id
        viewModel.productID.observe(viewLifecycleOwner) {
            if (isNetworkAvailable)
                viewModel.callProductPriceChart(it)
        }
        //Load data
        loadPriceChartData()
    }

    private fun loadPriceChartData() {
        binding.apply {
            viewModel.priceChartData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        featuresLoading.isVisible(true, pricesChart)
                    }

                    is NetworkRequest.Success -> {
                        featuresLoading.isVisible(false, pricesChart)
                        response.data?.let { data ->
                            daysList.clear()
                            daysListForTooltip.clear()
                            pricesList.clear()

                            if (data.isNotEmpty()) {
                                for (i in data.indices) {
                                    daysListForTooltip.add(data[i].day!!)
                                    daysList.add(data[i].day!!.drop(5))
                                    if (data[i].price!! > 0)
                                        pricesList.add(Entry(i.toFloat(), data[i].price!!.toFloat()))
                                }
                                //Init chart
                                lifecycleScope.launch {
                                    delay(100)
                                    if (pricesList.isNotEmpty()) {
                                        pricesChart.setupMyChart(
                                            DaysFormatter(daysList), pricesList, daysList.size,
                                            daysListForTooltip
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        featuresLoading.isVisible(false, pricesChart)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    inner class DaysFormatter(private val daysList: ArrayList<String>) : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
            val index = value.toInt()
            return if (index < daysList.size) {
                daysList[index]
            } else {
                null
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}