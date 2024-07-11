package com.example.storeapp.ui.shipping.adapters

import com.example.storeapp.data.models.shipping.ResponseShipping.Order.OrderItem
import com.example.storeapp.databinding.ItemShippingProductBinding
import com.example.storeapp.utils.BASE_URL_IMAGE
import com.example.storeapp.utils.base.BaseDiffUtils
import com.example.storeapp.utils.extensions.loadImageWithGlide
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ShippingAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    RecyclerView.Adapter<ShippingAdapter.ViewHolder>() {

    private var items = emptyList<OrderItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShippingProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemShippingProductBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: OrderItem) {
            binding.apply {
                val image = "$BASE_URL_IMAGE${item.productImage}"
                itemImg.loadImageWithGlide(image)
                //Title
                itemTitle.text = item.productTitle
            }
        }
    }

    fun setData(data: List<OrderItem>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}