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
    private lateinit var auth: FirebaseAuth

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
        auth = FirebaseAuth.getInstance()
        obseveLiveData()
    }

    fun obseveLiveData() {
        viewModel.foodDataSource.userAnswer.observe(viewLifecycleOwner) {
            if (it.success == 1) {
                (activity as LogoutActivity).login()
                it.message?.let { message -> makeToast(requireContext(), message) }
            } else {
                it.message?.let { message -> makeToast(requireContext(), message) }
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun btnRegisterClick(view: View, email: String?, password: String?) {
        email?.let { email ->
            password?.let { password ->
                if (email != "" && password != "") {
                    viewModel.register(requireContext(), email, password)
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}