package com.example.chatterbox.ui

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatterbox.R
import com.example.chatterbox.databinding.FragmentLoginBinding
import com.example.chatterbox.databinding.FragmentResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)

        binding.btnSendMail.setOnClickListener {
            if(binding.etEmailReset.text.toString().isNullOrBlank()){
                binding.etEmailReset.error = "Required"
            }
            else if(!isValidEmail(binding.etEmailReset.text.toString())){
                Toast.makeText(requireContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
            else{
                showProgressBar()
                FirebaseAuth.getInstance().sendPasswordResetEmail(binding.etEmailReset.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            hideProgressBar()
                            Toast.makeText(
                                requireContext(),
                                "Email sent successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                        }
                        else{
                            Toast.makeText(
                                requireContext(),
                                "Some error encountered. Try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        return binding.root
    }
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun hideProgressBar() {
        binding.pbFP.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.pbFP.visibility = View.VISIBLE

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}