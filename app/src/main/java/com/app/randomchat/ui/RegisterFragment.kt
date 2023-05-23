package com.app.randomchat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        return User(
            nome = binding.edtNome.text.toString(),
            email = binding.edtEmail.text.toString(),
            password = binding.edtPassword.text.toString(),
            id = FirebaseAuth.getInstance().uid
        )
    }

    private fun validation(): Boolean{
        var isValid = true
        if (binding.edtNome.text.toString().isNullOrEmpty()){
            isValid = false
            toast("verifique o campo nome!")
        }
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
        authViewModel.register.observe(viewLifecycleOwner){state->
            when(state){
                UiState.Loading -> {
                    binding.btnRegister.text = "Loading..."
                    binding.progressRegister.show()
                }
                is UiState.Success -> {
                    binding.btnRegister.text = "Register"
                    binding.progressRegister.hide()
                    toast(state.data)
                }
                is UiState.Failure -> {
                    binding.btnRegister.text = "Register"
                    binding.progressRegister.hide()
                    toast(state.error)
                }

            }
        }
    }

}

