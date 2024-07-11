package com.example.storeapp.ui.detail_comments

import com.example.storeapp.R
import com.example.storeapp.data.models.comments.ResponseCommentsList.Data
import com.example.storeapp.databinding.ItemCommentBinding
import com.example.storeapp.utils.base.BaseDiffUtils
import com.example.storeapp.utils.extensions.convertDateToFarsi
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CommentsAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private var items = emptyList<Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Data) {
            binding.apply {
                //User
                item.user?.let { user ->
                    if (user.firstname != null)
                        itemNameTxt.text = "${user.firstname} ${user.lastname}"
                    else
                        itemNameTxt.text = context.getString(R.string.withoutName)
                }
                //Date
                val date = item.createdAt!!.split("T")[0].convertDateToFarsi()
                val hour = "${context.getString(R.string.hour)} " +
                        item.createdAt.split("T")[1].split(".")[0].dropLast(3)
                itemDateTxt.text = "$date | $hour"
                //General
                itemRating.rating = item.rate.toString().toFloat()
                itemCommentTxt.text = item.comment
            }
        }
    }

    fun setData(data: List<Data>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}