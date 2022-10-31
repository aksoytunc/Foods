package com.tuncaksoy.inviobitirmeprojesi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.databinding.ProductRecyclerRowBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.AllFoodAdapterClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.view.HomePageFragmentDirections
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.HomePageViewModel

class AllFoodAdapter(
    var viewModel: HomePageViewModel
) :
    ListAdapter<Food, AllFoodAdapter.AllFoodViewHolder>(FoodDiffCallback()),
    AllFoodAdapterClickListener {

    class AllFoodViewHolder(var binding: ProductRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllFoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ProductRecyclerRowBinding>(
            inflater, R.layout.product_recycler_row, parent, false
        )
        return AllFoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllFoodViewHolder, position: Int) {
        holder.binding.listener = this
        holder.binding.food = getItem(position)
        holder.binding.cardViewProduct.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.anim_four
            )
        )
    }

    class FoodDiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun addToBasketClickListener(food: Food) {
        val foodBasketId = food.sepet_yemek_id
        val foodName = food.yemek_adi
        val foodImage = food.yemek_resim_adi
        val foodPrice = food.yemek_fiyat
        val foodNumber = 1
        if (food.yemek_sepet == true) viewModel.deleteToBasket(foodBasketId)
        else viewModel.addToBasket(foodName, foodImage, foodPrice, foodNumber)
    }

    override fun selectItemClickListener(view: View, food: Food) {
        val action = HomePageFragmentDirections.actionHomePageFragmentToDetailsFragment(food)
        Navigation.findNavController(view).navigate(action)
    }

    override fun saveFavoritesFoodClickListener(food: Food) {
        if (food.yemek_favori == true) viewModel.deleteFavoritesFood(food)
        else viewModel.saveFavoritesFood(food)
    }
}