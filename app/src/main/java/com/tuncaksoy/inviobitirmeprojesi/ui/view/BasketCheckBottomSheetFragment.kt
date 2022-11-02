package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.data.model.Order
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentBasketCheckBottomSheetBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.BasketCheckClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.ShoppingViewModel
import com.tuncaksoy.inviobitirmeprojesi.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@AndroidEntryPoint
class BasketCheckBottomSheetFragment(
    var fView: View,
    var total: Int,
    var number: Int,
    var viewModel: ShoppingViewModel
) :
    BottomSheetDialogFragment(),
    BasketCheckClickListener {
    private lateinit var binding: FragmentBasketCheckBottomSheetBinding
    val orderNo = Random.nextInt(100000000, 999999999)
    var wallet = 0
    var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getLiveUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_basket_check_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        binding.listener = this
        binding.total = total
        binding.number = number
    }

    fun observeLiveData() {
        viewModel.liveUser.observe(viewLifecycleOwner) {
            it.userBalance?.let {
                wallet = it
            }
            binding.wallet = wallet
            userEmail = it.userEmail
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun acceptClick(cardNumber: String?, adress: String?) {
        val current = LocalDateTime.now()
        val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val dateTime = current.format(formater).toString()

        if (!binding.switchCW.isChecked) {
            if (cardNumber?.length == 16) {
                if (binding.editTextAdress.text.toString().isNotEmpty()) {
                    val order = Order(
                        0,
                        orderNo,
                        1,
                        adress,
                        binding.switchCW.isChecked,
                        total,
                        number,
                        userEmail,
                        dateTime
                    )
                    val action =
                        ShoppingFragmentDirections.actionShoppingFragmentToAcceptedOrderFragment(
                            order
                        )
                    Navigation.findNavController(fView).navigate(action)
                    viewModel.basketFoodList.value?.let {
                        for (basketFood in it) {
                            viewModel.deleteToBasket(basketFood.sepet_yemek_id)
                        }
                    }
                    viewModel.saveOrder(order)
                    dismiss()
                } else makeToast(requireContext(), getString(R.string.noAddress))
            } else makeToast(requireContext(), getString(R.string.wrongCard))
        } else {
            if (binding.editTextAdress.text.toString().isNotEmpty()) {
                if (wallet >= total) {
                    val order = Order(
                        0,
                        orderNo,
                        1,
                        adress,
                        binding.switchCW.isChecked,
                        total,
                        number,
                        userEmail,
                        dateTime
                    )
                    val action =
                        ShoppingFragmentDirections.actionShoppingFragmentToAcceptedOrderFragment(
                            order
                        )
                    Navigation.findNavController(fView).navigate(action)
                    viewModel.basketFoodList.value?.let {
                        for (basketFood in it) {
                            viewModel.deleteToBasket(basketFood.sepet_yemek_id)
                        }
                    }
                    viewModel.saveOrder(order)
                    viewModel.updateBalance(wallet, total)
                    dismiss()
                } else makeToast(requireContext(), getString(R.string.noBalance))
            } else makeToast(requireContext(), getString(R.string.noAddress))
        }
    }

    override fun cancelClick() {
        this.dismiss()
    }
}