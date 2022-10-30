package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.datasource.FoodDataSource
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentRegisterBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.RegisterClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.RegisterViewModel
import com.tuncaksoy.inviobitirmeprojesi.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment(), RegisterClickListener {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listener = this
    }

    override fun btnRegisterClick(view: View, email: String?, password: String?) {
        email?.let {
            password?.let { password ->
                if (it != "" && password != "") {
                    viewModel.register((activity as LogoutActivity), requireContext(), it, password)
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}