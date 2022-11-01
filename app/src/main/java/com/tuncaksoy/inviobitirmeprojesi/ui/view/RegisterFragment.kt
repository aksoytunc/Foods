package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentRegisterBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.RegisterClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.RegisterViewModel
import com.tuncaksoy.inviobitirmeprojesi.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(), RegisterClickListener {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
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

    fun observeLiveData() {
        viewModel.answer.observe(viewLifecycleOwner) { answer ->
            answer.success?.let {
                if (it == 1) {
                    (activity as LogoutActivity).login()
                    makeToast(requireContext(), getString(R.string.registered))
                    binding.progressBar.visibility = View.GONE
                } else {
                    makeToast(requireContext(), answer.message.toString())
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun btnRegisterClick(email: String?, password: String?, passwordAgain: String?) {
        if (email != "" && password != "" && passwordAgain != ""){
            email?.let { nEmail ->
                password?.let { nPassword->
                    if (password == passwordAgain){
                        viewModel.register(nEmail,nPassword)
                        binding.progressBar.visibility = View.VISIBLE
                        observeLiveData()
                    }else makeToast(requireContext(),getString(R.string.wrongPassword))
                }
            }
        }else makeToast(requireContext(),getString(R.string.sectionsBlank))
    }
}