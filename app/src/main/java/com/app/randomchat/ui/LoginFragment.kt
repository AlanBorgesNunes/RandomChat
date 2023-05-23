package com.app.randomchat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.randomchat.R
import com.app.randomchat.databinding.FragmentLoginBinding
import com.app.randomchat.utils.*
import com.app.randomchat.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        toRegister()
        binding.btnLogin.setOnClickListener {
            if (validation()){
                authViewModel.login(
                    email = binding.edtEmail.text.toString(),
                    password = binding.edtPassword.text.toString()
                )
            }
        }
    }



    private fun validation(): Boolean{
        var isValid = true
        if (binding.edtEmail.text.toString().isNullOrEmpty()){
            isValid = false
            toast("verifique o campo email!")
        }else{
            if (!binding.edtEmail.text.toString().isValidEmail()){
                isValid = false
                toast("email invalido!")
            }
        }
        if (binding.edtPassword.text.toString().isNullOrEmpty()){
            isValid = false
            toast("verifique o campo password!")
        }else{
            if (binding.edtPassword.text.toString().length < 8){
                isValid = false
                toast("minimo de caracteres 8 digitos!")
            }
        }

        return isValid
    }

    private fun observer(){
        authViewModel.login.observe(viewLifecycleOwner){state->
            when(state){
                UiState.Loading -> {
                    binding.btnLogin.text = "Loading..."
                    binding.progressLogin.show()
                }
                is UiState.Success -> {
                    binding.btnLogin.text = "Login"
                    binding.progressLogin.hide()
                    toast("Sucess login!")
                }
                is UiState.Failure -> {
                    binding.btnLogin.text = "Login"
                    binding.progressLogin.hide()
                    toast(state.error)
                }

            }
        }
    }

    private fun toRegister(){
        binding.criarConta.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment
            )
        }
    }

}