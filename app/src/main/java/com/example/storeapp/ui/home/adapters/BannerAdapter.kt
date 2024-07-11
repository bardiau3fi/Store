package com.example.storeapp.ui.home.adapters

import com.example.storeapp.data.models.home.ResponseBanners.ResponseBannersItem
import com.example.storeapp.databinding.ItemBannersBinding
import com.example.storeapp.utils.BASE_URL_IMAGE_WITH_STORAGE
import com.example.storeapp.utils.PRODUCT
import com.example.storeapp.utils.base.BaseDiffUtils
import com.example.storeapp.utils.extensions.loadImage
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

class BannerAdapter @Inject constructor() : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    private var items = emptyList<ResponseBannersItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerAdapter.ViewHolder {
        val binding = ItemBannersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerAdapter.ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemBannersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseBannersItem) {
            binding.apply {
                itemTitle.text = item.title
                //Image
                val imageUrl = "$BASE_URL_IMAGE_WITH_STORAGE${item.image}"
                itemImg.loadImage(imageUrl)
                //Click
                root.setOnClickListener {
                    val sendData = if (item.link!! == PRODUCT) {
                        item.linkId
                    } else {
                        item.urlLink?.slug
                    }
                    onItemClickListener?.let {
                        it(sendData!!, item.link)
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((String, String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String, String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<ResponseBannersItem>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}