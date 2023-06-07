package com.app.randomchat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.randomchat.R
import com.app.randomchat.databinding.FragmentRegisterBinding
import com.app.randomchat.model.User
import com.app.randomchat.utils.*
import com.app.randomchat.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    val authViewModel: AuthViewModel  by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.btnRegister.setOnClickListener {
            if (validation()){
                authViewModel.register(
                    email = binding.edtEmail.text.toString(),
                    password = binding.edtPassword.text.toString(),
                    user =  getUserObj()
                )
            }
        }
    }

    private fun getUserObj(): User{
        var genero  = String()
        if (binding.checkHomem.isChecked){
            genero = binding.checkHomem.text.toString()
            binding.checkMulher.hide()
        }
        if (binding.checkMulher.isChecked){
            genero = binding.checkMulher.text.toString()
            binding.checkHomem.hide()
        }
        return User(
            nome = "Anonimo",
            email = binding.edtEmail.text.toString(),
            password = binding.edtPassword.text.toString(),
            id = "",
            genero = genero,
            idade = "18"
        )
    }

    private fun validation(): Boolean{
        var isValid = true
        if(binding.checkHomem.isChecked.equals(false) && binding.checkMulher.isChecked.equals(false)){
            isValid = false
            toast("Escolha o gênero antes de continuar!")
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
        authViewModel.register.observe(viewLifecycleOwner){state->
            when(state){
                UiState.Loading -> {
                    binding.tvRegister.text = ""
                    binding.registerProgress.show()
                }
                is UiState.Success -> {
                    binding.tvRegister.text = "Register"
                    binding.registerProgress.hide()
                    toast(state.data)
                    findNavController().navigate(
                        R.id.action_registerFragment_to_geralActivity
                    )
                }
                is UiState.Failure -> {
                    binding.tvRegister.text = "Register"
                    binding.registerProgress.hide()
                    toast(state.error)
                }

            }
        }
    }


}

