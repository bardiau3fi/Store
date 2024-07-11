package com.example.storeapp.ui.shipping.adapters

import com.example.storeapp.data.models.shipping.ResponseShipping.Addresse
import com.example.storeapp.databinding.ItemAddressesDialogBinding
import com.example.storeapp.utils.base.BaseDiffUtils
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AddressesAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    RecyclerView.Adapter<AddressesAdapter.ViewHolder>() {

    private var items = emptyList<Addresse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddressesDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemAddressesDialogBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Addresse) {
            binding.apply {
                itemNameTxt.text = "${item.receiverFirstname} ${item.receiverLastname}"
                itemAddressTxt.text = item.postalAddress
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }
            }
        }
    }

    private var onItemClickListener: ((Addresse) -> Unit)? = null

    fun setOnItemCLickListener(listener: (Addresse) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<Addresse>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}