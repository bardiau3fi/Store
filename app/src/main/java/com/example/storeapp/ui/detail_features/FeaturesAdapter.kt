package com.example.storeapp.ui.detail_features

import com.example.storeapp.R
import com.example.storeapp.data.models.detail.ResponseProductFeatures.ResponseProductFeaturesItem
import com.example.storeapp.databinding.ItemFeaturesBinding
import com.example.storeapp.utils.base.BaseDiffUtils
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FeaturesAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    RecyclerView.Adapter<FeaturesAdapter.ViewHolder>() {

    private var items = emptyList<ResponseProductFeaturesItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFeaturesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemFeaturesBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseProductFeaturesItem) {
            binding.apply {
                titleTxt.text = item.featureTitle
                infoTxt.text = item.featureItemTitle
                //Background
                if (bindingAdapterPosition % 2 == 0)
                    root.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                else
                    root.setBackgroundColor(ContextCompat.getColor(context, R.color.snow))
            }
        }
    }

    fun setData(data: List<ResponseProductFeaturesItem>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}