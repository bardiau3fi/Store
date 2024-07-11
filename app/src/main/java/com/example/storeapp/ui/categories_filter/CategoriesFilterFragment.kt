package com.example.storeapp.ui.categories_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storeapp.R
import com.example.storeapp.data.models.search_filter.FilterModel
import com.example.storeapp.databinding.FragmentCategoriesFilterBinding
import com.example.storeapp.utils.extensions.moneySeparating
import com.example.storeapp.viewmodel.CategoryProductViewModel
import android.widget.HorizontalScrollView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFilterFragment : BottomSheetDialogFragment() {
    //Binding
    private var _binding: FragmentCategoriesFilterBinding? = null
    private val binding get() = _binding!!

    //Theme
    override fun getTheme() = R.style.RemoveDialogBackground

    //Other
    private val viewModel by activityViewModels<CategoryProductViewModel>()
    private var minPrice: String? = null
    private var maxPrice: String? = null
    private var sort: String? = null
    private var search: String? = null
    private var available: Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCategoriesFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Init views
        binding.apply {
            //Close
            closeImg.setOnClickListener { this@CategoriesFilterFragment.dismiss() }
            //Rtl scrollview
            lifecycleScope.launch {
                delay(100)
                sortScroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
            }
            //Click
            submitBtn.setOnClickListener {
                //Search
                if (searchEdt.text.isNotEmpty()) {
                    search = searchEdt.text.toString()
                }
                //Available
                available = availableCheck.isChecked
                //Send data
                viewModel.sendCategoryData(sort, search, minPrice, maxPrice, available)
                //Close
                this@CategoriesFilterFragment.dismiss()
            }
        }
        //Price range
        initPriceRange()
        //Sort
        viewModel.getFilterData()
        loadSortData()
    }

    private fun initPriceRange() {
        //Label format
        val formatter = LabelFormatter { value ->
            value.toInt().moneySeparating()
        }
        //Customize
        binding.priceRange.apply {
            setValues(7000000f, 21000000f)
            setLabelFormatter(formatter)
            //Listener
            addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {

                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    val values = slider.values
                    minPrice = values[0].toInt().toString()
                    maxPrice = values[1].toInt().toString()
                }
            })
        }
    }

    private fun loadSortData() {
        viewModel.filterData.observe(viewLifecycleOwner) {
            setupChip(it)
        }
    }

    private fun setupChip(list: MutableList<FilterModel>) {
        var tempList = mutableListOf<FilterModel>()
        tempList.clear()
        tempList = list
        tempList.forEach {
            val chip = Chip(requireContext())
            val drawable = ChipDrawable.createFromAttributes(
                requireContext(), null, 0,
                R.style.FilterChipsBackground
            )
            chip.setChipDrawable(drawable)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            chip.text = it.faName
            chip.id = it.id
            chip.setTextAppearanceResource(R.style.FilterChipsText)

            binding.sortChipGroup.apply {
                addView(chip)
                //Click
                setOnCheckedStateChangeListener { group, _ ->
                    sort = tempList[group.checkedChipId - 1].enName
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}