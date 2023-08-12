package com.example.chatterbox.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chatterbox.R
import com.example.chatterbox.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding:FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        binding.ivGLogin.setOnClickListener {
//            loginUserWithGoogle()
        }

        binding.tvSignUpLoginActivity.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        return binding.root
    }

    private fun loginUserWithGoogle() {
        TODO("Not yet implemented")
    }

    private fun loginUser() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}