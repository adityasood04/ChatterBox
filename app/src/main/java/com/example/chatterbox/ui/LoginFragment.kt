package com.example.chatterbox.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatterbox.R
import com.example.chatterbox.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.ivGLogin.setOnClickListener {
            loginUserWithGoogle()
        }
        binding.btnLogIn.setOnClickListener {
            loginUser()
        }

        binding.tvSignUpLoginActivity.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        return binding.root
    }

    //login using email password
    private fun loginUser() {
        showPB()
        if (binding.etEmailLogin.text.toString()
                .isBlank() || binding.etPasswordLogin.text.toString().isBlank()
        ) {
            Toast.makeText(requireContext(), "Please enter all the details", Toast.LENGTH_SHORT)
                .show()
            hidePB()
            return
        }
        auth.signInWithEmailAndPassword(
            binding.etEmailLogin.text.toString(),
            binding.etPasswordLogin.text.toString()
        )
            .addOnCompleteListener {
                hidePB()
                if (it.isSuccessful) {
                    launchHomeActivity()
                } else {
                    Toast.makeText(requireContext(), "Wrong email or password", Toast.LENGTH_SHORT)
                        .show()
                    Log.i("adi", it.exception.toString())
                }
            }
    }


    //login using Google
    private fun loginUserWithGoogle() {
        showPB()
        val signinIntent = googleSignInClient.signInIntent
        launcher.launch(signinIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                hidePB()
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            } else {
                hidePB()
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {

            val account: GoogleSignInAccount? = task.result
            if (account != null) {

                updateUI(account)
            }
        } else {
            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            hidePB()
            if (it.isSuccessful) {
                Log.i("adi", "updateUI: ${account.email} ${account.displayName}")

                launchHomeActivity()
//                Toast.makeText(requireContext(), it.result.toString(), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun launchHomeActivity() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))

    }


    private fun showPB() {
        binding.pbLogin.visibility = View.VISIBLE
    }

    private fun hidePB() {
        binding.pbLogin.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}