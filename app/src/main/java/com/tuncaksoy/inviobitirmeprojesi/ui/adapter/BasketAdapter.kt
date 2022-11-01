package com.tuncaksoy.inviobitirmeprojesi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.databinding.BasketRecyclerRowBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.BasketAdapterClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.view.BasketDeleteBottomSheetFragment
import com.tuncaksoy.inviobitirmeprojesi.ui.view.ShoppingFragmentDirections
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.ShoppingViewModel

class BasketAdapter(
    var fragmentManager: FragmentManager,
    var viewModel: ShoppingViewModel,
) :
    ListAdapter<Food, BasketAdapter.BasketViewHolder>(BasketFoodDiffCallback()),
    BasketAdapterClickListener {

    class BasketViewHolder(var binding: BasketRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    private lateinit var bottomSheetFragment: BasketDeleteBottomSheetFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<BasketRecyclerRowBinding>(
            inflater, R.layout.basket_recycler_row, parent, false
        )
        return BasketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        holder.binding.listener = this
        val item = getItem(position)
        holder.binding.food = item
        holder.binding.total = ((item.yemek_fiyat?.toInt() ?: 0) *
                (item.yemek_siparis_adet?.toInt() ?: 0))
        holder.binding.cardViewBasket.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.anim_two
            )
        )
    }

    class BasketFoodDiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun deleteClick(food: Food) {
        if (food.yemek_favori == true) viewModel.deleteToBasket(food.sepet_yemek_id)
        else {
            bottomSheetFragment = BasketDeleteBottomSheetFragment(viewModel, food)
            bottomSheetFragment.show(fragmentManager, "basket")
        }

    }

    override fun selectItemClick(view: View, food: Food) {
        val action = ShoppingFragmentDirections.actionShoppingFragmentToDetailsFragment(food)
        Navigation.findNavController(view).navigate(action)
    }
}