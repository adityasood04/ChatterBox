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
import com.example.chatterbox.databinding.FragmentSignUpBinding
import com.example.chatterbox.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    val TAG = "adi"
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.ivGSignup.setOnClickListener {
            signupGoogle()
        }
        binding.btnSignUp.setOnClickListener {
            signupUser()

        }

        //navigation to login screen
        binding.tvBackToSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        return binding.root
    }


    private fun signupUser() {
        showPB()
        if (binding.etEmailSignUp.text.toString().isBlank()
            || binding.etPasswordSignUp.text.toString().isBlank()
            || binding.etUserNameSignUp.text.toString().isBlank()
            || binding.etPassWordConfirmSignUp.text.toString().isBlank()
        ) {
            Toast.makeText(requireContext(), "Please fill all details", Toast.LENGTH_SHORT).show()
        } else if (binding.etPasswordSignUp.text.toString() != binding.etPassWordConfirmSignUp.text.toString()) {
            Toast.makeText(requireContext(), "Password don't match", Toast.LENGTH_SHORT).show()
        } else {

            //signup user with email password
            auth.createUserWithEmailAndPassword(
                binding.etEmailSignUp.text.toString(),
                binding.etPasswordSignUp.text.toString()
            )
                .addOnCompleteListener {
                    hidePB()
                    if (it.isSuccessful) {
                        //save user details
                        user = User(
                            auth.currentUser!!.uid,
                            binding.etUserNameSignUp.text.toString(),
                            binding.etEmailSignUp.text.toString(),
                            binding.etPasswordSignUp.text.toString()
                        )
                        saveUserDetails(user)
                        Toast.makeText(
                            requireContext(),
                            "User signed up successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i(TAG, it.exception.toString())
                    }
                }

        }
    }

    private fun saveUserDetails(user: User) {
        db.collection("User").document(user.id).set(user)
            .addOnCompleteListener {
                hidePB()
                if (it.isSuccessful) {

                    launchHomeActivity()
                } else {
                    Log.i(TAG, "saveUserDetails: ${it.exception!!.message}")
                }
            }
    }


    //sign up using google
    private fun signupGoogle() {
        showPB()
        val signinIntent = googleSignInClient.signInIntent
        launcher.launch(signinIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
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
            if (it.isSuccessful) {
                //save user details
                user = User(
                    auth.currentUser!!.uid,
                    account.displayName.toString(),
                    account.email.toString(),
                    "google"
                )
                saveUserDetails(user)
                Log.i(
                    "adi",
                    "updateUI: ${account.photoUrl} ${account.email} ${account.displayName}"
                )
            } else {
                hidePB()
                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun launchHomeActivity() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
    }

    private fun showPB() {
        binding.pbSignUP.visibility = View.VISIBLE

    }

    private fun hidePB() {
        binding.pbSignUP.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}