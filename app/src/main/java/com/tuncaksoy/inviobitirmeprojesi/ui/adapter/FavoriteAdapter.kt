package com.tuncaksoy.inviobitirmeprojesi.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.databinding.FavoriteRecyclerRowBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.FavoriteAdapterClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.view.FavoritesFragmentDirections
import com.tuncaksoy.inviobitirmeprojesi.ui.view.HomePageFragmentDirections
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.FavoritesViewModel

class FavoriteAdapter(var viewModel: FavoritesViewModel) :
    ListAdapter<Food, FavoriteAdapter.FavoriteViewHolder>(FavoriteFoodDiffCallback()),
    FavoriteAdapterClickListener {

    class FavoriteViewHolder(var binding: FavoriteRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<FavoriteRecyclerRowBinding>(
            inflater, R.layout.favorite_recycler_row, parent, false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.binding.listener = this
        holder.binding.food = getItem(position)
        holder.binding.cardViewFavori.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.anim_three
            )
        )
    }

    class FavoriteFoodDiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun deleteFoodClick(food: Food) {
        viewModel.deleteFavoritesFood(food)
    }

    override fun selectItemClick(view: View, food: Food) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(food)
        Navigation.findNavController(view).navigate(action)
    }
}