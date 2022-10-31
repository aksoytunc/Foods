package com.tuncaksoy.inviobitirmeprojesi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.model.Order
import com.tuncaksoy.inviobitirmeprojesi.databinding.OrderRecyclerRowBinding
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.OrderHistoryViewModel

class OrderAdapter(var viewModel: OrderHistoryViewModel) :
    ListAdapter<Order, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    class OrderViewHolder(var binding: OrderRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<OrderRecyclerRowBinding>(
            inflater, R.layout.order_recycler_row, parent, false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.binding.order = getItem(position)
        holder.binding.cardViewOrder.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.anim_two
            )
        )
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

}