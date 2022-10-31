package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentBasketDeleteBottomSheetBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.BasketDeleteBottomSheetClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.ShoppingViewModel

class BasketDeleteBottomSheetFragment(var viewModel: ShoppingViewModel,var food: Food) :
    BottomSheetDialogFragment(), BasketDeleteBottomSheetClickListener {
    private lateinit var binding: FragmentBasketDeleteBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_basket_delete_bottom_sheet,
            container,
            false
        )
        binding.foodName = food.yemek_adi
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listener = this
    }

    override fun favoriAndDeleteClick() {
        viewModel.deleteToBasket(food.sepet_yemek_id)
        viewModel.saveFavoritesFood(food)
        this.dismiss()
    }

    override fun deleteClick() {
        viewModel.deleteToBasket(food.sepet_yemek_id)
        this.dismiss()
    }

    override fun noClick() {
        this.dismiss()
    }
}