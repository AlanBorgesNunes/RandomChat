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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private  var _binding: FragmentLoginBinding? = null
    private var user = FirebaseAuth.getInstance().currentUser

    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        toRegister()
        observador()
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
                    binding.tvLogin.text = ""
                    binding.loginProgress.show()
                }
                is UiState.Success -> {
                    binding.tvLogin.text = "Login"
                    binding.loginProgress.hide()
                    toast("Sucess login!")
                    findNavController().navigate(
                        R.id.action_loginFragment_to_geralActivity
                    )
                }
                is UiState.Failure -> {
                    binding.tvLogin.text = "Login"
                    binding.loginProgress.hide()
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

    private fun observador(){
        authViewModel.getSession {
            if (it != null){
                findNavController().navigate(
                    R.id.action_loginFragment_to_geralActivity
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}